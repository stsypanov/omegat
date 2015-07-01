package org.omegat.gui.lf;

import com.sun.istack.internal.NotNull;
import com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel;
import org.omegat.gui.lf.ui.laf.ColorUtil;
import org.omegat.util.Log;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Properties;

public class DarculaLaf extends BasicLookAndFeel {
  public static final String NAME = "Darcula";
  BasicLookAndFeel base;

  public DarculaLaf() {
    try {
//      if (SystemInfo.isWindows || SystemInfo.isLinux) {
        base = new WindowsClassicLookAndFeel();
//      } else {
//        final String name = UIManager.getSystemLookAndFeelClassName();
//        base = (BasicLookAndFeel)Class.forName(name).newInstance();
//      }
    }
    catch (Exception e) {
      log(e);
    }
  }

  private void callInit(String method, UIDefaults defaults) {
    try {
      final Method superMethod = BasicLookAndFeel.class.getDeclaredMethod(method, UIDefaults.class);
      superMethod.setAccessible(true);
      superMethod.invoke(base, defaults);
    }
    catch (Exception e) {
      log(e);
    }
  }

  @SuppressWarnings("UnusedParameters")
  private static void log(Exception e) {
//    everything is gonna be alright
      Log.log(e);
  }

  @Override
  public UIDefaults getDefaults() {
    try {
      final Method superMethod = BasicLookAndFeel.class.getDeclaredMethod("getDefaults");
      superMethod.setAccessible(true);
      final UIDefaults metalDefaults = (UIDefaults)superMethod.invoke(new MetalLookAndFeel());
      final UIDefaults defaults = (UIDefaults)superMethod.invoke(base);
//      if (SystemInfo.isLinux && !Registry.is("darcula.use.native.fonts.on.linux")) {
//        Font font = findFont("DejaVu Sans");
//        if (font != null) {
//          for (Object key : defaults.keySet()) {
//            if (key instanceof String && ((String)key).endsWith(".font")) {
//              defaults.put(key, new FontUIResource(font.deriveFont(13f)));
//            }
//          }
//        }
//      }

      initIdeaDefaults(defaults);
      patchStyledEditorKit(defaults);
      patchComboBox(metalDefaults, defaults);
      defaults.remove("Spinner.arrowButtonBorder");
      defaults.put("Spinner.arrowButtonSize", new Dimension(16, 5));
      MetalLookAndFeel.setCurrentTheme(createMetalTheme());
//      if (SystemInfo.isWindows) {
//        JFrame.setDefaultLookAndFeelDecorated(true);
//      }
      defaults.put("EditorPane.font", defaults.getFont("TextField.font"));
      return defaults;
    }
    catch (Exception e) {
      log(e);
    }
    return super.getDefaults();
  }

  protected DefaultMetalTheme createMetalTheme() {
    return new DarculaMetalTheme();
  }

  private static Font findFont(String name) {
    for (Font font : GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()) {
      if (font.getName().equals(name)) {
        return font;
      }
    }
    return null;
  }

  private static void patchComboBox(UIDefaults metalDefaults, UIDefaults defaults) {
    defaults.remove("ComboBox.ancestorInputMap");
    defaults.remove("ComboBox.actionMap");
    defaults.put("ComboBox.ancestorInputMap", metalDefaults.get("ComboBox.ancestorInputMap"));
    defaults.put("ComboBox.actionMap", metalDefaults.get("ComboBox.actionMap"));
  }

  @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
  private void patchStyledEditorKit(UIDefaults defaults) {
//    URL url = getClass().getResource(getPrefix() + ".css");
//    StyleSheet styleSheet = UIUtil.loadStyleSheet(url);
//    defaults.put("StyledEditorKit.JBDefaultStyle", styleSheet);
//    try {
//      Field keyField = HTMLEditorKit.class.getDeclaredField("DEFAULT_STYLES_KEY");
//      keyField.setAccessible(true);
//      AppContext.getAppContext().put(keyField.get(null), UIUtil.loadStyleSheet(url));
//    }
//    catch (Exception e) {
//      log(e);
//    }
  }

  protected String getPrefix() {
    return "darcula";
  }

  private void call(String method) {
    try {
      final Method superMethod = BasicLookAndFeel.class.getDeclaredMethod(method);
      superMethod.setAccessible(true);
      superMethod.invoke(base);
    }
    catch (Exception ignore) {
      log(ignore);
    }
  }

  @Override
  public void initComponentDefaults(UIDefaults defaults) {
    callInit("initComponentDefaults", defaults);
  }

  @SuppressWarnings({"HardCodedStringLiteral"})
  protected void initIdeaDefaults(UIDefaults defaults) {
    loadDefaults(defaults);
    defaults.put("Table.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] {
      "ctrl C", "copy",
      "ctrl V", "paste",
      "ctrl X", "cut",
      "COPY", "copy",
      "PASTE", "paste",
      "CUT", "cut",
      "control INSERT", "copy",
      "shift INSERT", "paste",
      "shift DELETE", "cut",
      "RIGHT", "selectNextColumn",
      "KP_RIGHT", "selectNextColumn",
      "LEFT", "selectPreviousColumn",
      "KP_LEFT", "selectPreviousColumn",
      "DOWN", "selectNextRow",
      "KP_DOWN", "selectNextRow",
      "UP", "selectPreviousRow",
      "KP_UP", "selectPreviousRow",
      "shift RIGHT", "selectNextColumnExtendSelection",
      "shift KP_RIGHT", "selectNextColumnExtendSelection",
      "shift LEFT", "selectPreviousColumnExtendSelection",
      "shift KP_LEFT", "selectPreviousColumnExtendSelection",
      "shift DOWN", "selectNextRowExtendSelection",
      "shift KP_DOWN", "selectNextRowExtendSelection",
      "shift UP", "selectPreviousRowExtendSelection",
      "shift KP_UP", "selectPreviousRowExtendSelection",
      "PAGE_UP", "scrollUpChangeSelection",
      "PAGE_DOWN", "scrollDownChangeSelection",
      "HOME", "selectFirstColumn",
      "END", "selectLastColumn",
      "shift PAGE_UP", "scrollUpExtendSelection",
      "shift PAGE_DOWN", "scrollDownExtendSelection",
      "shift HOME", "selectFirstColumnExtendSelection",
      "shift END", "selectLastColumnExtendSelection",
      "ctrl PAGE_UP", "scrollLeftChangeSelection",
      "ctrl PAGE_DOWN", "scrollRightChangeSelection",
      "ctrl HOME", "selectFirstRow",
      "ctrl END", "selectLastRow",
      "ctrl shift PAGE_UP", "scrollRightExtendSelection",
      "ctrl shift PAGE_DOWN", "scrollLeftExtendSelection",
      "ctrl shift HOME", "selectFirstRowExtendSelection",
      "ctrl shift END", "selectLastRowExtendSelection",
      "TAB", "selectNextColumnCell",
      "shift TAB", "selectPreviousColumnCell",
      //"ENTER", "selectNextRowCell",
      "shift ENTER", "selectPreviousRowCell",
      "ctrl A", "selectAll",
      "meta A", "selectAll",
      //"ESCAPE", "cancel",
      "F2", "startEditing"
    }));
  }

  @SuppressWarnings("IOResourceOpenedButNotSafelyClosed")
  protected void loadDefaults(UIDefaults defaults) {
    final Properties properties = new Properties();
//    final String osSuffix = SystemInfo.isMac ? "mac" : SystemInfo.isWindows ? "windows" : "linux";
    final String osSuffix = "windows";
    try {
      InputStream stream = getClass().getResourceAsStream(getPrefix() + ".properties");
      properties.load(stream);
      stream.close();

      stream = getClass().getResourceAsStream(getPrefix() + '_' + osSuffix + ".properties");
      properties.load(stream);
      stream.close();

      HashMap<String, Object> darculaGlobalSettings = new HashMap<>();
      final String prefix = getPrefix() + '.';
      for (String key : properties.stringPropertyNames()) {
        if (key.startsWith(prefix)) {
          darculaGlobalSettings.put(key.substring(prefix.length()), parseValue(key, properties.getProperty(key)));
        }
      }

      for (Object key : defaults.keySet()) {
        if (key instanceof String && ((String)key).contains(".")) {
          final String s = (String)key;
          final String darculaKey = s.substring(s.lastIndexOf('.') + 1);
          if (darculaGlobalSettings.containsKey(darculaKey)) {
            defaults.put(key, darculaGlobalSettings.get(darculaKey));
          }
        }
      }

      for (String key : properties.stringPropertyNames()) {
        final String value = properties.getProperty(key);
        defaults.put(key, parseValue(key, value));
      }
    }
    catch (IOException e) {log(e);}
  }

  protected Object parseValue(String key, @NotNull String value) {
    if ("null".equals(value)) {
      return null;
    }

    if (key.endsWith("Insets")) {
//      final java.util.List<String> numbers = StringUtil.split(value, ",");
//      return new InsetsUIResource(Integer.parseInt(numbers.get(0)),
//                                             Integer.parseInt(numbers.get(1)),
//                                             Integer.parseInt(numbers.get(2)),
//                                             Integer.parseInt(numbers.get(3)));
    } else if (key.endsWith(".border")) {
      try {
        return Class.forName(value).newInstance();
      } catch (Exception e) {log(e);}
    } else {
      final Color color = parseColor(value);
      final Integer invVal = getInteger(value);
      final Boolean boolVal = "true".equals(value) ? Boolean.TRUE : "false".equals(value) ? Boolean.FALSE : null;
      Icon icon = null;
      if (icon == null && value.endsWith(".png")) {
        icon = null;
      }
      if (color != null) {
        return  new ColorUIResource(color);
      } else if (invVal != null) {
        return invVal;
      } else if (icon != null) {
        return new IconUIResource(icon);
      } else if (boolVal != null) {
        return boolVal;
      }
    }
    return value;
  }

  @SuppressWarnings("UseJBColor")
  private static Color parseColor(String value) {
    if (value != null && value.length() == 8) {
      final Color color = ColorUtil.fromHex(value.substring(0, 6));
      if (color != null) {
        try {
          int alpha = Integer.parseInt(value.substring(6, 8), 16);
          return new ColorUIResource(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
        } catch (Exception ignore){}
      }
      return null;
    }
    return ColorUtil.fromHex(value, null);
  }

  private static Integer getInteger(String value) {
    try {
      return Integer.parseInt(value);
    }
    catch (NumberFormatException e) {
      return null;
    }
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getID() {
    return getName();
  }

  @Override
  public String getDescription() {
    return "IntelliJ Dark Look and Feel";
  }

  @Override
  public boolean isNativeLookAndFeel() {
    return true;
  }

  @Override
  public boolean isSupportedLookAndFeel() {
    return true;
  }

  @Override
  protected void initSystemColorDefaults(UIDefaults defaults) {
    callInit("initSystemColorDefaults", defaults);
  }

  @Override
  protected void initClassDefaults(UIDefaults defaults) {
    callInit("initClassDefaults", defaults);
  }

  @Override
  public void initialize() {
    call("initialize");
  }

  @Override
  public void uninitialize() {
    call("uninitialize");
  }

  @Override
  protected void loadSystemColors(UIDefaults defaults, String[] systemColors, boolean useNative) {
    try {
      final Method superMethod = BasicLookAndFeel.class.getDeclaredMethod("loadSystemColors",
                                                                   UIDefaults.class,
                                                                   String[].class,
                                                                   boolean.class);
      superMethod.setAccessible(true);
      superMethod.invoke(base, defaults, systemColors, useNative);
    }
    catch (Exception ignore) {
      log(ignore);
    }
  }

  @Override
  public boolean getSupportsWindowDecorations() {
    return true;
  }
}