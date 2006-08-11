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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * This class uses an SQLite database to provide the functionality of {@link IDictionaryProvider}.
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class SQLiteProvider implements IDictionaryProvider {
  
 
  private Connection _conn;
  private Statement _stm;
  
  private java.util.ArrayList<String> _activeDics;
  
  private SaxHandler _saxHandler;
  
  /**
   * Creates a new instance of SQLiteProvider. Will start a connection to 
   * the database.
   */
  public SQLiteProvider(String pathToDBFile)
  {
    try
    {
      _activeDics = new java.util.ArrayList<String>();


      Class.forName("org.sqlite.JDBC");
            
      // open connection
      _conn = DriverManager.getConnection("jdbc:sqlite:" + pathToDBFile);
      _stm = _conn.createStatement();
      
      // if not existing, create a table with a list of all imported dictionaries
      _stm.execute("CREATE TABLE IF NOT EXISTS imported_dics(name TEXT, author TEXT, date INTEGER)");
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
      _stm.execute("CREATE TABLE " + lemma + "(lemma TEXT, lemma_lower TEXT, origin TEXT,id TEXT)");
      _stm.execute("CREATE TABLE " + entry + "(id TEXT, entry TEXT)");
      
      // we have to add a lot of data
      _conn.setAutoCommit(false);
      
      index(path, name);
      
      _conn.commit();
      _conn.setAutoCommit(true);
      
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
    
    return true;
  }

  /** Index a given file */
  private void index(String path, String name)
  {
    try {
      
      // initialisation
      InputStreamReader reader = new InputStreamReader(
        new FileInputStream(path));
      
      _saxHandler = new SaxHandler();
      
      Random rand = new Random();
      XMLReader xr = XMLReaderFactory.createXMLReader();
      xr.setContentHandler(_saxHandler);
      
      
      PreparedStatement ps_entry = _conn.prepareStatement("INSERT INTO entry_" 
        + name + "(id,entry) VALUES(?,?)");
      PreparedStatement ps_lemma = _conn.prepareStatement("INSERT INTO lemma_" + name
                      + "(lemma,id,origin,lemma_lower) VALUES(?,?,?,?)");
                    
      
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
                _saxHandler.lastLemmaInEntry.clear();
                _saxHandler.lastIdInEntry = null;
                InputSource src = new InputSource(new java.io.ByteArrayInputStream(entry.getBytes()));
                xr.parse(src);
                
                // add it to the database
                int size = _saxHandler.lastLemmaInEntry.size();
                if(size != 0)
                {
                  if(_saxHandler.lastIdInEntry == null)
                  {
                    // random number
                    _saxHandler.lastIdInEntry = name + "-" + rand.nextInt();
                  }
                  ps_entry.setString(1, _saxHandler.lastIdInEntry);
                  ps_entry.setString(2, entry);
                  ps_entry.executeUpdate();
                  
                  
                  Iterator<String> it = _saxHandler.lastLemmaInEntry.iterator();
                  
                  while(it.hasNext())
                  {
                    String l = it.next();
                    ps_lemma.setString(1, l);
                    ps_lemma.setString(2, _saxHandler.lastIdInEntry);
                    ps_lemma.setString(3, name);
                    ps_lemma.setString(4, l.toLowerCase()); // neede due comparison bug of SQLite
                    ps_lemma.executeUpdate();
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
      
      ps_entry.close();
      ps_lemma.close();
      
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

  public String[][] getEntry(String lemma) {
    ArrayList<String[]> list = new ArrayList<String[]>();
    try {      
      // ask the database
      ResultSet res = _stm.executeQuery("SELECT entry, origin FROM dic WHERE dic.lemma = \"" + lemma + "\"");
      while(res.next())
      {
        list.add(new String[] {res.getString(1), res.getString(2)});
      }
    } 
    catch (SQLException ex) 
    {
      ex.printStackTrace();
    }
    
    String[][] a = new String[0][2];
    a = list.toArray(a);
    
    return a;
  }

  public List<String[]> getMatchingLemma(String name) {
    ArrayList<String[]> list = new ArrayList<String[]>();
    try {      
      // ask the database
      ResultSet res = _stm.executeQuery("SELECT lemma,origin FROM dic WHERE lemma_lower LIKE \"" + name.toLowerCase() + "%\"");
      while(res.next())
      {
        list.add(new String[] {res.getString(1), res.getString(2)});
      }
    } 
    catch (SQLException ex) 
    {
      ex.printStackTrace();
    }
    
    return list;
  }
  
  public String[] getAvailableDictionaries() {
    
    java.util.ArrayList<String> list = new java.util.ArrayList<String>();
    
    try
    {
      
      // query the database
      ResultSet res = _stm.executeQuery("SELECT name FROM imported_dics");
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
    return new String[0];
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
          + "SELECT l.lemma AS lemma, e.entry AS entry, e.id AS id, l.origin AS origin, l.lemma_lower AS lemma_lower "
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
    if(_activeDics.contains(dic))
    {
      _activeDics.remove(dic);
      rebuildDBView();
    }
  }

  public boolean deleteDictionary(String name) 
  {
    if(isImported(name))
    {
      try
      {
        // delete the tables
        _stm.execute("DROP TABLE lemma_" + name);
        _stm.execute("DROP TABLE entry_" + name);
        
        // delete entry from imported_dics
        _stm.execute("DELETE FROM imported_dics WHERE name = \"" + name + "\"");
        
        if(_activeDics.contains(name))
        {
          _activeDics.remove(name);
          rebuildDBView();
        }
        
        return true;        
      }
      catch(Exception ex)
      {
        ex.printStackTrace();
      }
      
    }
    
    return false;
  }
  
  public static void main(String[] args)
  {
    SQLiteProvider dic = new SQLiteProvider("owl.db");
    //dic.importDictionary("/home/thomas/projekte/owl/dicts/de-en.dicml", "de_en");
    dic.importDictionary("test/testMatch.dicml", "de_en");
    dic.activateDictionary("de_en");
    dic.getMatchingLemma("sönder");
  }
  
}
  
