/*
 * IDictionaryProvider.java
 *
 * Created on 5. August 2006, 11:09
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.base;

/**
 * An Interface which represents an abstract handler to provide data from a 
 * dicML-dictionary.
 *
 * @author thomas
 */
public interface IDictionaryProvider {
    
    /** 
     * Imports an given dicML-file.
     * @param path The path to the dicML-file you want to import
     * @param name The name which shall be given to this dictionary. Should be unique,
     *             so the path is recommend.
     * @return true if import was successfull, false if not.
     */        
    public boolean importDictionary(String path, String name);
    
    /**
     * Check if a dictionary is already imported.
     * @param name The name of the dictionary
     * @return true if already imported, false if not.
     */
    public boolean isImported(String name);
    
    /**
     * Open an already imported dictionary for further use.
     * @param name The name of the dictionary
     * @return true is successfull, false if not
     */
    public boolean openDictionary(String name);
    
    /**
     * Gets all dicML-entries for a given lemma.
     * @param lemma The lemma.
     * @return If a lemma which matches <b>exactly</b> exist, the corresponding
     *        entries (in dicML, including the "<entry>"-tag). Otherwise null.
     */
    public String[] getEntry(String lemma);
    
    /**
     * Finds a list of lemma which first characters match a given string.
     * @param name The string used for matching.
     * @return All matching lemma (count might be 0).
     */
    public String[] getMatchingLemma(String name);
    
    /** Returns a list of available dictionaries that can be (de)activated */
    public String[] getAvailableDictionaries();
    
    /** 
     * An activated dictionary will be visible when using 
     * {@link #getEntry} and 
     * {@link #getMatchingLemma}.
     */
    public void activateDictionary(String dic);
    
    
    /** 
     * An deactivated dictionary will <b>not</b> be visible when using 
     * {@link #getEntry} and 
     * {@link #getMatchingLemma}.
     */
    public void deactivateDictionary(String dic);
}