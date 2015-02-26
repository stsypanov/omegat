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
import java.util.Iterator;
import java.util.List;

/**
  * Represents translation units.
  */
public final class TU {

    /**
      * Contains all translation unit variants (TUVs).
      */
    protected List tuvs;
    
    /**
      * Returns a list of all translation unit variants.
      */
    public List getTUVs() {
        return new ArrayList(tuvs);
    }
    
    /**
      * Direct reference to the TUV in the set source language
      */
    protected TUV source;
    
    /**
      * Returns the source TUV.
      */
    public TUV getSource() {
        return source;
    }
    
    /**
      * Direct reference to the TUV in the set target language.
      */
    protected TUV target;
    
    /**
      * Returns the target TUV (if set).
      *
      * If a target could not be located when reading the XML file,
      * and it is not set later, this will be null.
      */
    public TUV getTarget() {
        return target;
    }
    
    /**
      * Sets the target TUV.
      *
      * @param target -- The new target.
      */
    public void setTarget(TUV target) {
        setTarget(target, true);
    }
    
    /**
      * Sets the target TUV.
      *
      * @param target -- The new target.
      * @param add    -- If true, adds the TUV to the TUVs list,
      *                  if it's not already in it.
      */
    protected void setTarget(TUV target, boolean add) {
        // set the new target
        this.target = target;

        if (add) {        
        }
    }
    
    /**
      * Adds a TUV to the TU.
      *
      * @param tuv -- The TUV to add.
      * @return The added TUV, or an already present TUV with
      *         the exact same language and content.
      */
    public TUV addTUV(TUV tuv) {
        // check if the new target is already in the TUVs list 
        boolean found   = false;
        TUV existingTUV = null;
        for (Iterator i = tuvs.iterator(); i.hasNext();) {
            // get the next TUV
            existingTUV = (TUV)i.next();
            
            // check if language and contents match
            if (   tuv.language.equalsIgnoreCase(existingTUV.language)
                && tuv.text.equals(existingTUV.text)) {
                found = true;
                break;
            }
        }
        
        // add the new target to the TUVs list, if it's not in it
        if (!found)
            tuvs.add(tuv);
        
        return found ? existingTUV : tuv;
    }
    
    /**
      * Adds a new TUV to the TU.
      *
      * @param language -- The language of the TUV.
      */
    public TUV newTUV(String language) {
        return newTUV(language, "");
    }
    
    /**
      * Adds a new TUV to the TU.
      *
      * @param language -- The language of the TUV.
      * @param text     -- The segment text.
      *
      * @return The newly created TUV, or existing TUV if a TUV
      *         with matching language and text is found.
      */
    public TUV newTUV(String language, String text) {
        return addTUV(new TUV(language, text));
    }
    
    /**
      * Removes a TUV from the TU.
      *
      * @param language -- The language of the TUV to remove.
      */
    public void removeTUVs(String language) {
        for (Iterator i = tuvs.iterator(); i.hasNext();) {
            // get the next TUV
            TUV tuv = (TUV)i.next();
            
            // remove the TUV if the language matches
            if (tuv.language.equalsIgnoreCase(language))
                i.remove();
        }
    }
    
    /**
      * Removes any TUV that matches the TUV's language and contents from the TU.
      *
      * @param tuv -- The TUV to remove.
      */
    public void removeTUV(TUV tuv) {
        for (Iterator i = tuvs.iterator(); i.hasNext();) {
            // get the next TUV
            TUV existingTUV = (TUV)i.next();
            
            // check if language and contents match
            if (   tuv.language.equalsIgnoreCase(existingTUV.language)
                && tuv.text.equals(existingTUV.text)) {
                // remove the TUV and stop
                i.remove();
                break;
            }
        }
    }
    
    /**
      * Default constructor
      */
    protected TU() {
        super();
        tuvs = new ArrayList(2);
    }
    
}
