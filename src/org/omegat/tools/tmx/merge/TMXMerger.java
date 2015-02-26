/*
 * TMXMerger - Merges two or more TMX files
 * Copyright(c) 2005-2010, Henry Pijffers (henry.pijffers@saxnot.com)
 *
 * This program is licensed to you under the terms of version 2 or later of
 * the GNU General Public License (the "GPL"), as published by the Free Software
 * Foundation.
 */

package org.omegat.tools.tmx.merge;

import java.util.ArrayList;

/**
 * Merges two or more TMX files
 *
 * @author Henry Pijffers (henry.pijffers@saxnot.com)
 */
public class TMXMerger {

    public static void main(String[] arguments) {
        // FIX: check arguments
        
        // check number of arguments
        if (arguments.length < 3) {
            // print usage info
            System.err.println("Usage: TMXMerger [source=LL(-CC)] <tmx1> <tmx2> [<tmx3> ...] <output>");
            System.err.println("If no source language is specified, the source language as");
            System.err.println("specified in the source language attribute in the first TMX");
            System.err.println("file will be used. To specify a source language, add the");
            System.err.println("option source=LL(-CC), where LL is the language code, and");
            System.err.println("CC is the country code (optional). This *must* be the first");
            System.err.println("argument.");
            
            // halt program
            return;
        }
        
        // get the source language (first argument)
        String sourceLanguage = arguments[0].startsWith("source=")
                                    ? arguments[0].substring(7)
                                    : null;
                                    
        // if the source language is not specified, notify the user
        if (sourceLanguage == null)
            System.err.println("Source language not specified, using source language from first TMX file.");
        
        // determine the first file argument
        int firstFile = (sourceLanguage == null) ? 0 : 1;  
        
        try {
            // load the first TMX file
            TMX tmx = new TMX(sourceLanguage);
            tmx.load(arguments[firstFile]);
            
            // merge in all other TMX files
            for (int i = firstFile + 1; i < (arguments.length - 1); i++)
                tmx.merge(arguments[i]);
            
            // save the merged TMX to the file specified in the last argument
            // FIX: check if the file already exists, and ask for permission to overwrite
            tmx.save(arguments[arguments.length - 1]);
        }
        catch (java.io.IOException exception) {
            System.err.println(exception.getLocalizedMessage());
            exception.printStackTrace(System.err);
        }
    }

}
