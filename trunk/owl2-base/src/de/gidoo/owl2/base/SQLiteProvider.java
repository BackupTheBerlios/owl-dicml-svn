/*
 * SQLiteProvider.java
 *
 * Created on 5. August 2006, 11:18
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.base;

import java.io.*;
import java.sql.*;

/**
 * This class uses an SQLite database.
 * @author thomas
 */
public class SQLiteProvider implements IDictionaryProvider {
  
  public static final String DB_NAME = "owl.db";
  
  private Connection _conn;
  private Statement _stm;
  
  /**
   * Creates a new instance of SQLiteProvider. Will start a connection to 
   * the database.
   */
  public SQLiteProvider() {
    try
    {
      Class.forName("org.sqlite.JDBC");

      // open connection
      _conn = DriverManager.getConnection("jdbc:sqlite:" + SQLiteProvider.DB_NAME);
      _stm = _conn.createStatement();
      
      // if not existing, create a table with a list of all imported dictionaries
      try
      {
        _stm.executeQuery("SELECT name FROM imported_dics");
      }
      catch(Exception ex)
      {
        _stm.execute("CREATE TABLE imported_dics(name TEXT, author TEXT, date INTEGER)");
      }
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
      
  }

  /** finalize() will close the connection to the database. */
  protected void finalize()
  {
    try
    {
      _conn.close();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  public boolean importDictionary(String path, String name) {
    if(path == null || name == null || path.equals("") || name.equals(""))
    {
      return false;
    }
    
    try
    {
     
      // delete if necessary
      String lemma = "lemma_" + name;
      String entry = "entry_" + name;
      
      _stm.execute("DROP TABLE IF EXISTS " + lemma);
      _stm.execute("DROP TABLE IF EXISTS " + entry);
      
      // create new ones
      _stm.execute("CREATE TABLE " + lemma + "(lemma TEXT, id TEXT)");
      _stm.execute("CREATE TABLE " + entry + "(id TEXT, entry TEXT)");
      
      index(path);
      
      // new entry in list of dics (or update if existing)
      if(isImported(name))
      {
        _stm.execute("UPDATE imported_dics "
          + "SET "
          + "name=\"" + name + "\", "
          + "author=\"unknown\", "
          + "date=\"" + new java.util.Date().getTime() + "\" "
          + "WHERE name=\"" + name + "\"");
      }
      else
      {
        _stm.execute("INSERT INTO imported_dics (name,author,date) VALUES(\"" 
          + name + "\",\"unknown\"" + ","
          + new java.util.Date().getTime() + ")");        
      }
//      //ATTENTION: SQLite specific
//      ResultSet result = stm.executeQuery("SELECT name FROM sqlite_master");
//      while(result.next())
//      {
//        String lemma = "lemma_" + name;
//        String entry = "entry_" + name;
//        String table = result.getString(1);
//        
//        // delete if necessary
//        if(table.equals(lemma))
//        {
//          stm.executeQuery("SELECT");
//        }
        
//      }
      
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      return false;
    }
    
    // as long as no function implemented...
    return false;
  }

  /** Index a given file */
  private void index(String path)
  {
    try {
      InputStreamReader reader = new InputStreamReader(
        new FileInputStream(path));
      
      // search for "<entry"
      int c;
      int q1 = 0; // count of already read in characters of "<entry" (KMP)
      char[] P1 = new char[] {'<','e','n','t','r','y'};
      byte[] J1 = new byte[] {0,0,0,0,0,0};
      while((c = reader.read()) > -1)
      {
        while(q1 > 0 && P1[q1] != (char) c)
          q1 = J1[q1-1];
        
        if(P1[q1] == (char) c)
          q1++;
        
        if(q1 == P1.length)
        {
          q1 = 0;
          
          // found, now find the end ("</entry" and add it to the database
          String entry = "<entry";
          int q2 = 0; // again KMP
          char[] P2 = new char[] {'<', '/', 'e', 'n', 't', 'r', 'y'};
          byte[] J2 = new byte[] {0,0,0,0,0,0,0};
          boolean found = false;
          while(!found && (c = reader.read()) > -1)
          {
           
            while(q2 > 0 && P2[q2] != (char) c)
              q2 = J2[q2-1];
            
            if(P2[q2] == (char) c)
              q2++;
            
            if(q2 == P2.length)
            {
              found = true;
              entry = entry + "</entry>";
              
              //TODO: parse the xml (only entry)
              
              
              //TODO: add it to the database

              
            }
            else
            {
              entry = entry + c;
            }
          }
        }
        
      }
      
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    
  }
  
  public boolean isImported(String name) {
      try
      {
        ResultSet result = _stm.executeQuery("SELECT name FROM imported_dics"
          + " WHERE name=\"" + name + "\"");
        boolean wasFound = false;
        while(result.next() && !wasFound)
        {
          wasFound = true;
        }
        
        return wasFound;
      }
      catch(SQLException ex)
      {
        ex.printStackTrace();
      }
      
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
  
  
  
  public static void main(String[] args)
  {
    SQLiteProvider p = new SQLiteProvider();
    p.importDictionary("test/test.dicml", "test");
  }  

}
  
