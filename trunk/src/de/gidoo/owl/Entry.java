/*
 * Entry.java
 *
 * Created on 20. Februar 2005, 18:27
 */

package de.gidoo.owl;

/**
 * Describes the structure of an entry.
 * @author Thomas Krause
 */
public class Entry {
   
   /** contains the string which is presented to the user, for example "dictionary (2)" */ 
   public String lemma;
   /** contains the string which is used for a "normal" comparision, for example "dictionary" */
   public String compare;
   /** contains the string which is uses for a comparision by AlphabetOrderLight */
   public String compareProcessed;
   /** starting-position of the entry in the dicml-file */
   public long start;
   /** end-position of the entry in the dicml-file */
   public long end;
    
    /** Creates a new instance of Entry */
    public Entry() {
    }
   
    public String toString()
    {
      return this.lemma;
    }
}
