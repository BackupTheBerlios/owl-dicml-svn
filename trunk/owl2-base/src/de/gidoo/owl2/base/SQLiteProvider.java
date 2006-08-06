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
import java.util.Iterator;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * This class uses an SQLite database to provide the functionality of {@link IDictionaryProvider}.
 * @author thomas
 */
public class SQLiteProvider implements IDictionaryProvider {
  
  public static final String DB_NAME = "owl.db";
  
  private Connection _conn;
  private Statement _stm;
  
  private java.util.ArrayList<String> _activeDics;
  
  /**
   * Creates a new instance of SQLiteProvider. Will start a connection to 
   * the database.
   */
  public SQLiteProvider() 
  {
    try
    {
      _activeDics = new java.util.ArrayList<String>();
      
      Class.forName("org.sqlite.JDBC");

      // open connection
      _conn = DriverManager.getConnection("jdbc:sqlite:" + SQLiteProvider.DB_NAME);
      _stm = _conn.createStatement();
      
      // if not existing, create a table with a list of all imported dictionaries
      _stm.execute("CREATE TABLE IF NOT EXISTS imported_dics(name TEXT, author TEXT, date INTEGER)");
//      try
//      {
//        _stm.executeQuery("SELECT name FROM imported_dics");
//      }
//      catch(Exception ex)
//      {
//        _stm.execute("CREATE TABLE imported_dics(name TEXT, author TEXT, date INTEGER)");
//      }
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
      
      this.
      
      // create new ones
      _stm.execute("CREATE TABLE " + lemma + "(lemma TEXT, id TEXT)");
      _stm.execute("CREATE TABLE " + entry + "(id TEXT, entry TEXT)");
      
      index(path, name);
      
      // we need some indexes
      _stm.execute("CREATE INDEX IF NOT EXISTS idx_lemma_" + name + " ON lemma_"
        + name + " (lemma)"); 
      _stm.execute("CREATE INDEX IF NOT EXISTS idx_entry_" + name + " ON entry_"
        + name + " (id)");
      
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
  private void index(String path, String name)
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
              q2 = 0;
              entry = entry + "y>";
              
              //parse the xml (only entry)
              try
              {
                org.w3c.dom.Document doc = 
                  javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                  new java.io.ByteArrayInputStream(entry.getBytes()));
                
                NodeList entryNodes = doc.getElementsByTagName("entry");


                //add it to the database
                if(entryNodes.getLength() == 1)
                {
                  Node id = entryNodes.item(0).getAttributes().getNamedItem("id");
                  if(id != null)
                  {
                    // add to entry-table
                    
                    PreparedStatement ps = _conn.prepareStatement("INSERT INTO entry_" 
                      + name + "(id,entry) VALUES(?,?)");
                    ps.setString(1, id.getNodeValue());
                    ps.setString(2, entry);
                    ps.executeUpdate();
                    ps.close();
                    
                    // add available lemma to lemma-table
                    NodeList lNodes = doc.getElementsByTagName("l");
                    NodeList lAltNodes = doc.getElementsByTagName("l.alt");
                    ps = _conn.prepareStatement("INSERT INTO lemma_" + name
                      + "(lemma,id) VALUES(?,?)");
                    
                    for(int i=0; i < lNodes.getLength(); i++)
                    {
                      ps.setString(1, lNodes.item(i).getTextContent());
                      ps.setString(2, id.getTextContent());
                      ps.executeUpdate();
                    }
                    for(int i=0; i < lAltNodes.getLength(); i++)
                    {
                      ps.setString(1, lAltNodes.item(i).getTextContent());
                      ps.setString(2, id.getTextContent());
                      ps.executeUpdate();
                    }
                    
                    ps.close();
                  }
                }
              }
              catch(SAXException saxEx)
              {
                saxEx.printStackTrace();
              }
            }
            else
            {
              entry = entry + (char) c;
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

  public String[] getEntry(String lemma) {
    return null;
  }

  public String[] getMatchingLemma(String name) {
    return new String[0];
  }
  
  
  
  public static void main(String[] args)
  {
    SQLiteProvider p = new SQLiteProvider();
    p.importDictionary("test/testAB.dicml", "AB");    
    p.importDictionary("test/testC.dicml", "C");
    p.importDictionary("test/testD.dicml", "D");
    
    p.activateDictionary("AB");
    p.activateDictionary("C");
    p.activateDictionary("D");
    
  }  

  public String[] getAvailableDictionaries() {
    
    java.util.ArrayList<String> list = new java.util.ArrayList<String>();
    
    try
    {
      
      // query the database
      ResultSet res = _stm.executeQuery("SELECT name IN imported_dics");
      while(res.next())
      {
        list.add(res.getString(1));
      }
      String[] result = new String[0];
      result = list.toArray(result);
      return result;
      
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
    return null;
  }

  public void activateDictionary(String dic) 
  {
    // check wether already active (don't do anything in that case)
    if(!_activeDics.contains(dic))
    {
      _activeDics.add(dic);
      
      // we need a new view
      rebuildDBView();
    }
  }

  /** Create a view inside a database which make it easy to access all importent features */
  private void rebuildDBView()
  {
    try
    {
      _stm.execute("DROP VIEW IF EXISTS allEntry");
      _stm.execute("DROP VIEW IF EXISTS allLemma");
      // union all lemma_* and entry_*
      String statementLemma = "CREATE VIEW allLemma AS ";
      String statementEntry = "CREATE VIEW allEntry AS ";
      Iterator<String> it = _activeDics.iterator();
      while(it.hasNext())
      {
        String dic = it.next();
        statementLemma += "SELECT * FROM lemma_" + dic;
        statementEntry += "SELECT * FROM entry_" + dic;
        
        if(it.hasNext())
        {
          statementLemma += " UNION ";
          statementEntry += " UNION ";
        }
        
      }
      
      _stm.execute(statementEntry);    
      _stm.execute(statementLemma);
      
      // and now, the "all-knowing" view
      try
      {
        _stm.execute("CREATE VIEW dic AS "
          + "SELECT l.lemma, e.entry, e.id "
          + "FROM allLemma l, allEntry e "
          + "WHERE l.id = e.id");
      }
      catch(SQLException sqlEx)
      {
        // don't care until I find out how to know wether a view already exists
      }
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
  }
  
  public void deactivateDictionary(String dic) 
  {
  }

}
  
