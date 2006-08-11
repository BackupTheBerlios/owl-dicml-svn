/*
 * IDictionaryProvider.java
 *
 * Created on 5. August 2006, 11:09
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.base;

import java.util.List;

/**
 * An Interface which represents an abstract handler to provide data from a 
 * dicML-dictionary.
 *
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public interface IDictionaryProvider {
    
    /** 
     * Imports a given dicML-file.
     * @param path The path to the dicML-file you want to import
     * @param name The name which shall be given to this dictionary. Should be unique,
     *             so the path is recommend.
     * @return true if import was successfull, false if not.
     */        
    public boolean importDictionary(String path, String name);
    
    /**
     * Delete an imported dictionary.
     * @param name The name of the dictionary to delete.
     * @return true if successful, false otherwise.
     */
    public boolean deleteDictionary(String name);
    
    /**
     * Check if a dictionary is already imported.
     * @param name The name of the dictionary
     * @return true if already imported, false if not.
     */
    public boolean isImported(String name);

    
    /**
     * Gets all dicML-entries for a given lemma.
     * @param lemma The lemma.
     * @return If a lemma which matches <b>exactly</b> exist, the corresponding
     *        entries (in dicML, including the "&ltentry&gt"-tag). Otherwise null.<br>
     *        Each array-entry is an array by itself with the following meaning:
     *        0 - the markup-text of the entry <br>
     *        1 - the dictionary where it comes from<br>
     *        
     */
    public String[][] getEntry(String lemma);
    
    /**
     * Finds a list of lemma which first characters match a given string.
     * @param name The string used for matching.
     * @return An list with one-dimensional arrays:<br>
     *         0 - the matching lemma <br>
     *         1 - the dictionary where it comes from<br>
     *         Count might be 0.
     */
    public List<String[]> getMatchingLemma(String name);
    
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