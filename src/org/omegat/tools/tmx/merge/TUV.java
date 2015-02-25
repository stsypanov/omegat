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
import java.util.List;

/**
  * Represents a translation unit variant.
  */
public final class TUV {

    /**
      * Default constructor, hidden.
      */
    private TUV() {}
    
    /**
      * Default constructor.
      *
      */
    protected TUV(String language) {
        super();
        initialize(language, "", null, null);
    }

    /**
      * Default constructor.
      */
    protected TUV(String language, String text) {
        super();
        initialize(language, text, null, null);
    }

    protected TUV(String language, String text, String changeDate, String changeID) {
        super();
        initialize(language, text, changeDate, changeID);
    }
    
    /**
      * Initializes a TUV.
      */
    private void initialize(String language, String text, String changeDate, String changeID) {
        this.language   = language;
        this.text       = text;
        this.changeDate = changeDate;
        this.changeID   = changeID;
        subSegments     = new ArrayList(0);
        // FIX: set subSegments to null, instead of creating
        //      a new ArrayList. Very often there aren't any
        //      sub segments, so creating an ArrayList every
        //      time is a waste of both memory and speed.
        //      If subSegments is set to null however, checks
        //      need to be made when adding and retrieving
        //      sub segments.
    }
    
    /**
      * True if the TUV is a source TUV.
      * Set in TMX when the file is read.
      */
    protected boolean isSource;
    
    /**
      * True if the TUV is a source TUV.
      */
    public boolean isSource() {
        return isSource;
    }

    /**
      * Language and (optional) country code: LL(-CC).
      */
    protected String language;
    
    /**
      * Returns the language and (optional) country code of the TUV
      * in LL(-CC) format, where LL is the language code and CC is
      * the country code.
      */
    public String getLanguage() {
        return language;
    }
    
    /**
      * Segment text
      */
    protected String text;
    
    /**
      * Returns the TUV's segment text.
      */
    public String getText() {
        return text;
    }
    
    /**
      * Sets the TUV's segment text.
      *
      * If the TUV is a source TUV (isSource() returns true), then
      * the segment text cannot be set, since the source TUV should
      * be immutable, and an UnsupportedOperationExcecption will be
      * thrown.
      */
    public void setText(String text) {
        // if this TUV is a source TUV, throw an exception 
        if (isSource)
            throw new UnsupportedOperationException();
        
        // set the new text
        this.text = text;
    }

    protected String changeDate;
    public String getChangeDate() {
       return changeDate;
    }

    protected String changeID;
    public String getChangeID() {
       return changeID;
    }
    
    /**
      * Contains the text of subsegments
      */
    protected List subSegments;
    
    /**
      * Returns a list of sub segment texts.
      */
    public List getSubSegments() {
        return new ArrayList(subSegments);
    }
        
}
