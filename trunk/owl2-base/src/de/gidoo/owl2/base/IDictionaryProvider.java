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
     *             so the path is recommend. Do not use any of the following characters
     *             (will return false): "
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
     * Gets all dicML-entries for a given lemma from all activated dictionaries.
     * @param lemma The lemma.
     * @return If a lemma which matches <b>exactly</b> exist, the corresponding
     *        entries (in dicML, including the "&lt;entry&gt"-tag). Otherwise null.<br>
     *        Each array-entry is an array by itself with the following meaning:<br>
     *        0 - the markup-text of the entry <br>
     *        1 - the dictionary where it comes from
     *        
     */
    public String[][] getEntry(String lemma);
    
    /**
     * Finds a list of lemma from all activated dictionaries which first characters match a given string.
     * @param text The string used for matching.
     * @return An list with one-dimensional arrays:<br>
     *         0 - the matching lemma <br>
     *         1 - the dictionary where it comes from<br>
     *         Count might be 0.
     */
    public List<String[]> getMatchingLemma(String text);
    
        /**
     * Gets all dicML-entries for a given lemma.
     * @param lemma The lemma.
     * @param from From which dictionary the entries can come from. null means any activated.
     * @return If a lemma which matches <b>exactly</b> exist, the corresponding
     *        entries (in dicML, including the "&lt;entry&gt"-tag). Otherwise null.<br>
     *        Each array-entry is an array by itself with the following meaning:<br>
     *        0 - the markup-text of the entry <br>
     *        1 - the dictionary where it comes from
     *        
     */
    public String[][] getEntry(String lemma, String from);
    
    /**
     * Finds a list of lemma which first characters match a given string.
     * @param text The string used for matching.
     * @param from From which dictionary the entries can come from. null means any activated.
     * @return An list with one-dimensional arrays:<br>
     *         0 - the matching lemma <br>
     *         1 - the dictionary where it comes from<br>
     *         Count might be 0.
     */
    public List<String[]> getMatchingLemma(String text, String from);
    
    /** Returns a list of available dictionaries that can be (de)activated */
    public String[] getAvailableDictionaries();
    
    /** 
     * Get the title of an imported dictionary.
     * @param name The name of the dictionary.
     * @return  A string containing the title of this dictionary as written in <meta:title> inside dicML.
     *          Might be null if dictionary is not imported.
     */
    public String getTitle(String name);
    
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
    
    /**
     * Get information about the progress of importing a dictionary.
     * @return How much of the importing process is done in percent or -1 if nothing is done right now;
     */
    public double getImportingProgress();
    
    /**
     * Wether to stop importing process at the next possible point
     */
    public void setStopImporting(boolean stop);
}