/**************************************************************************
 OmegaT - Computer Assisted Translation (CAT) tool 
          with fuzzy matching, translation memory, keyword search, 
          glossaries, and translation leveraging into updated projects.

 Copyright (C) 2000-2006 Keith Godfrey and Maxym Mykhalchuk
               2010 Alex Buloichik
               Home page: http://www.omegat.org/
               Support center: http://groups.yahoo.com/group/OmegaT/

 This file is part of OmegaT.

 OmegaT is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 OmegaT is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 **************************************************************************/

package org.omegat.filters2.master;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.omegat.core.Core;
import org.omegat.tokenizer.DefaultTokenizer;
import org.omegat.tokenizer.ITokenizer;
import org.omegat.tokenizer.Tokenizer;
import org.omegat.util.FileUtil;
import org.omegat.util.Language;
import org.omegat.util.Log;
import org.omegat.util.OStrings;
import org.omegat.util.StaticUtils;

/**
 * Static utilities for OmegaT filter plugins.
 * 
 * @author Maxym Mykhalchuk
 * @author Alex Buloichik (alex73mail@gmail.com)
 */
public final class PluginUtils {

    enum PLUGIN_TYPE {
        FILTER, TOKENIZER, MARKER, MACHINETRANSLATOR, BASE, GLOSSARY, UNKNOWN
    }

    protected static URLClassLoader pluginsClassLoader;
    protected static List<Class<?>> loadedPlugins = new ArrayList<>();

    /** Private constructor to disallow creation */
    private PluginUtils() {
    }

    /**
     * Loads all plugins from main classloader and from /plugins/ dir. We should
     * load all jars from /plugins/ dir first, because some plugin can use more
     * than one jar.
     */
    public static void loadPlugins(final Map<String, String> params) {
        File pluginsDir = new File(StaticUtils.installDir(), "plugins");
        File homePluginsDir = new File(StaticUtils.getConfigDir(), "plugins");
        try {
            // list all jars in /plugins/
            List<File> fs = FileUtil.findFiles(pluginsDir, new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".jar");
                }
            });
            List<File> fsHome = FileUtil.findFiles(homePluginsDir, new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".jar");
                }
            });
            fs.addAll(fsHome);
            URL[] urls = new URL[fs.size()];
            for (int i = 0; i < urls.length; i++) {
                urls[i] = fs.get(i).toURI().toURL();
                Log.logInfoRB("PLUGIN_LOAD_JAR", urls[i].toString());
            }
            boolean foundMain = false;
            // look on all manifests
            pluginsClassLoader = new URLClassLoader(urls, PluginUtils.class.getClassLoader());
            for (Enumeration<URL> mlist = pluginsClassLoader.getResources("META-INF/MANIFEST.MF"); mlist
                    .hasMoreElements();) {
                URL mu = mlist.nextElement();
                Manifest m;
                try (InputStream in = mu.openStream()) {
                    m = new Manifest(in);
                }
                if ("org.omegat.Main".equals(m.getMainAttributes().getValue("Main-Class"))) {
                    // found main manifest - not in development mode
                    foundMain = true;
                }
                loadFromManifest(m, pluginsClassLoader);
            }
            if (!foundMain) {
                // development mode - load main manifest template
                String manifests = params.get("dev-manifests");
                if (manifests == null) {
                    manifests = "manifest-template.mf";
                }
                for (String mf : manifests.split(File.pathSeparator)) {
                    Manifest m;
                    try (InputStream in = new FileInputStream(mf)) {
                        m = new Manifest(in);
                    }
                    loadFromManifest(m, pluginsClassLoader);
                }
            }
        } catch (Exception ex) {
            Log.log(ex);
        }
        
        // Sort tokenizer list for display in Project Properties dialog.
        Collections.sort(tokenizerClasses, new Comparator<Class<?>>() {
            @Override
            public int compare(Class<?> c1, Class<?> c2) {
                return c1.getName().compareTo(c2.getName());
            }
        });
        
        // run base plugins
        for (Class<?> pl : basePluginClasses) {
            try {
                pl.newInstance();
            } catch (Exception ex) {
                Log.log(ex);
            }
        }
    }

    public static List<Class<?>> getFilterClasses() {
        return filterClasses;
    }

    public static List<Class<?>> getTokenizerClasses() {
        return tokenizerClasses;
    }

    public static Class<?> getTokenizerClassForLanguage(Language lang) {
        if (lang == null) return DefaultTokenizer.class;
        
        // Prefer an exact match on the full ISO language code (XX-YY).
        Class<?> exactResult = searchForTokenizer(lang.getLanguage());
        if (isDefault(exactResult)) {
            return exactResult;
        }
        
        // Otherwise return a match for the language only (XX).
        Class<?> generalResult = searchForTokenizer(lang.getLanguageCode());
        if (isDefault(generalResult)) {
            return generalResult;
        } else if (exactResult != null) {
            return exactResult;
        } else if (generalResult != null) {
            return generalResult;
        }
        
        return DefaultTokenizer.class;
    }

    private static boolean isDefault(Class<?> c) {
        if (c == null) return false;
        Tokenizer ann = c.getAnnotation(Tokenizer.class);
        return ann == null ? false : ann.isDefault();
    }

    private static Class<?> searchForTokenizer(String lang) {
        if (lang.length() < 1) return null;
        
        lang = lang.toLowerCase();
        
        // Choose first relevant tokenizer as fallback if no
        // "default" tokenizer is found.
        Class<?> fallback = null;
        
        for (Class<?> c : tokenizerClasses) {
            Tokenizer ann = c.getAnnotation(Tokenizer.class);
            if (ann == null) continue;
            String[] languages = ann.languages();
            try {
                if (languages.length == 1 && languages[0].equals(Tokenizer.DISCOVER_AT_RUNTIME)) {
                    languages = ((ITokenizer) c.newInstance()).getSupportedLanguages();
                }
            } catch (Exception ex) {
                // Nothing
            }
            for (String s : languages) {
                if (lang.equals(s)) {
                    if (ann.isDefault()) return c; // Return best possible match.
                    else if (fallback == null) fallback = c;
                }
            }
        }
        
        return fallback;
    }

    public static List<Class<?>> getMarkerClasses() {
        return markerClasses;
    }

    public static List<Class<?>> getMachineTranslationClasses() {
        return machineTranslationClasses;
    }

    public static List<Class<?>> getGlossaryClasses() {
        return glossaryClasses;
    }

    protected static List<Class<?>> filterClasses = new ArrayList<>();

    protected static List<Class<?>> tokenizerClasses = new ArrayList<>();

    protected static List<Class<?>> markerClasses = new ArrayList<>();

    protected static List<Class<?>> machineTranslationClasses = new ArrayList<>();

    protected static List<Class<?>> glossaryClasses = new ArrayList<>();

    protected static List<Class<?>> basePluginClasses = new ArrayList<>();

    /**
     * Parse one manifest file.
     * 
     * @param m
     *            manifest
     * @param classLoader
     *            classloader
     * @throws ClassNotFoundException
     */
    protected static void loadFromManifest(final Manifest m, final ClassLoader classLoader)
            throws ClassNotFoundException {
        String pluginClasses = m.getMainAttributes().getValue("OmegaT-Plugins");
        if (pluginClasses != null) {
            for (String clazz : pluginClasses.split("\\s+")) {
                if (clazz.trim().isEmpty()) {
                    continue;
                }
                try {
                    Class<?> c = classLoader.loadClass(clazz);
                    Method load = c.getMethod("loadPlugins");
                    load.invoke(c);
                    loadedPlugins.add(c);
                    Log.logInfoRB("PLUGIN_LOAD_OK", clazz);
                } catch (Throwable ex) {
                    Log.logErrorRB(ex, "PLUGIN_LOAD_ERROR", clazz, ex.getClass().getSimpleName(), ex.getMessage());
                    Core.pluginLoadingError(StaticUtils.format(OStrings.getString("PLUGIN_LOAD_ERROR"), clazz, ex
                            .getClass().getSimpleName(), ex.getMessage()));
                }
            }
        }

        loadFromManifestOld(m, classLoader);
    }

    public static void unloadPlugins() {
        for(Class<?> p:loadedPlugins) {
            try {
                Method load = p.getMethod("unloadPlugins");
                load.invoke(p);
            } catch (Throwable ex) {
                Log.logErrorRB(ex, "PLUGIN_UNLOAD_ERROR", p.getClass().getSimpleName(), ex.getMessage());
            }
        }
    }

    /**
     * Old-style plugin loading.
     */
    protected static void loadFromManifestOld(final Manifest m, final ClassLoader classLoader)
            throws ClassNotFoundException {
        if (m.getMainAttributes().getValue("OmegaT-Plugin") == null) {
            return;
        }

        Map<String, Attributes> entries = m.getEntries();
        for (String key : entries.keySet()) {
            Attributes attrs = entries.get(key);
            String sType = attrs.getValue("OmegaT-Plugin");
            if ("true".equals(attrs.getValue("OmegaT-Tokenizer"))) {
                // TODO remove after release new tokenizers
                sType = "tokenizer";
            }
            if (sType == null) {
                // WebStart signing section, or other section
                continue;
            }
            PLUGIN_TYPE pType;
            try {
                pType = PLUGIN_TYPE.valueOf(sType.toUpperCase(Locale.ENGLISH));
            } catch (Exception ex) {
                pType = PLUGIN_TYPE.UNKNOWN;
            }
            switch (pType) {
            case FILTER:
                filterClasses.add(classLoader.loadClass(key));
                Log.logInfoRB("PLUGIN_LOAD_OK", key);
                break;
            case TOKENIZER:
                tokenizerClasses.add(classLoader.loadClass(key));
                Log.logInfoRB("PLUGIN_LOAD_OK", key);
                break;
            case MARKER:
                markerClasses.add(classLoader.loadClass(key));
                Log.logInfoRB("PLUGIN_LOAD_OK", key);
                break;
            case MACHINETRANSLATOR:
                machineTranslationClasses.add(classLoader.loadClass(key));
                Log.logInfoRB("PLUGIN_LOAD_OK", key);
                break;
            case BASE:
                basePluginClasses.add(classLoader.loadClass(key));
                Log.logInfoRB("PLUGIN_LOAD_OK", key);
                break;
            case GLOSSARY:
                glossaryClasses.add(classLoader.loadClass(key));
                Log.logInfoRB("PLUGIN_LOAD_OK", key);
                break;
            default:
                Log.logErrorRB("PLUGIN_UNKNOWN", key);
            }
        }
    }
}
