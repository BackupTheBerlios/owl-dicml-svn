/*
 * SQLiteProvider.java
 *
 * Created on 5. August 2006, 11:18
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.base;

/**
 * This class uses an SQLite database 
 * @author thomas
 */
public class SQLiteProvider implements IDictionaryProvider {
  
  /** Creates a new instance of SQLiteProvider */
  public SQLiteProvider() {
  }

  public boolean importDictionary(String path, String name) {
    return false;
  }

  public boolean isImported(String name) {
    return false;
  }

  public boolean openDictionary(String name) {
    return false;
  }

  public String getEntry(String lemma) {
    return null;
  }

  public String[] getMatchingLemma(String name) {
    return new String[0];
  }
  
}
