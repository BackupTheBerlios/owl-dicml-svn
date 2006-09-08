/*
 * SQLiteProvider.java
 *
 * Created on 5. August 2006, 11:18
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.base;

import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.xerces.internal.parsers.JAXPConfiguration;
import java.io.*;
import java.sql.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Result;
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
      _stm.execute("CREATE TABLE IF NOT EXISTS imported_dics(name TEXT, title TEXT,srcLanguage TEXT, " +
        "destLanguage TEXT, author TEXT, date INTEGER)");
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
      DictionaryHead head = parseHead(path);
      
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
          + "title=\"" + head.title + "\", "
          + "srcLanguage=\"" + head.srcLanguage + "\", "
          + "destLanguage=\"" + head.destLanguage + "\", "
          + "author=\"" + head.authorName + "\", "
          + "date=\"" + new java.util.Date().getTime() + "\" "
          + "WHERE name=\"" + name + "\"");
      }
      else
      {
        _stm.execute("INSERT INTO imported_dics (name, title, srcLanguage, destLanguage, author, date)" +
          " VALUES(\"" 
          + name +  "\", \""
          + head.title + "\", \""
          + head.srcLanguage + "\", \""
          + head.destLanguage + "\", \""
          + head.authorName + "\", " 
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

  /** Parse head-part of a dictionary and return all needed information */
  private DictionaryHead parseHead(String path)
  {
    DictionaryHead result = new DictionaryHead();
    try
    {
      InputStreamReader reader = new InputStreamReader(
        new FileInputStream(path));
      
      String head;
      byte[] piLeft = new byte[] {0,0,0,0,0,0,0,0,0,0};
      byte[] piRight = new byte[] {0,0,0,0,0,0,0,0,0,0,0}; 
      while((head = getInnerText(reader, "<meta:head", piLeft, "</meta:head", piRight)) != null)
      {
        head = head + ">";
        DocumentBuilder docBuilder = 
          javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource inputSrc = new InputSource(new java.io.ByteArrayInputStream(head.getBytes()));
        org.w3c.dom.Document doc = docBuilder.parse(inputSrc);
        
        // get the language
        NodeList langList = doc.getElementsByTagName("meta:lang");
        Node langNode = langList.item(0);
        if(langNode != null)
        {
          result.srcLanguage = langNode.getAttributes().getNamedItem("source").getNodeValue();
          result.destLanguage = langNode.getAttributes().getNamedItem("target").getNodeValue();
        }
        
        Node titleNode = doc.getElementsByTagName("meta:title").item(0);
        if(titleNode != null)
        {
          result.title = titleNode.getTextContent();
        }
        
        // TODO: something more senseful
        result.authorName = "";
        
      }
        
      reader.close();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
    return result;
  }
  
  /** Index a given file */
  private void index(String path, String name)
  {
    try 
    {
      
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
      String entry;

      byte[] piLeft = new byte[] {0,0,0,0,0,0};
      byte[] piRight = new byte[] {0,0,0,0,0,0,0};
      while((entry = getInnerText(reader, "<entry", piLeft, "</entry", piRight)) != null)
      {
        entry = entry + ">";
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
              ps_lemma.setString(4, l.toLowerCase()); // needed due comparison bug of SQLite
              ps_lemma.executeUpdate();
            }

          }

        }
        catch(SAXException saxEx)
        {
          saxEx.printStackTrace();
        }
      }
      
//      // search for "<entry"
//      int c;
//      int q1 = 0; // count of already read in characters of "<entry" (KMP)
//      char[] P1 = new char[] {'<','e','n','t','r','y'};
//      byte[] J1 = new byte[] {0,0,0,0,0,0};
//      while((c = reader.read()) > -1)
//      {
//        while(q1 > 0 && P1[q1] != (char) c)
//          q1 = J1[q1-1];
//        
//        if(P1[q1] == (char) c)
//          q1++;
//        
//        if(q1 == P1.length)
//        {
//          q1 = J1[q1-1];
//          
//          // found, now find the end ("</entry" and add it to the database
//          String entry = "<entry";
//          int q2 = 0; // again KMP
//          char[] P2 = new char[] {'<', '/', 'e', 'n', 't', 'r', 'y'};
//          byte[] J2 = new byte[] {0,0,0,0,0,0,0};
//          boolean found = false;
//          while(!found && (c = reader.read()) > -1)
//          {
//           
//            while(q2 > 0 && P2[q2] != (char) c)
//              q2 = J2[q2-1];
//            
//            if(P2[q2] == (char) c)
//              q2++;
//            
//            if(q2 == P2.length)
//            {
//              found = true;
//              q2 = J2[q2-1];
//              entry = entry + "y>";
//              
//              //parse the xml (only entry)
//              try
//              {
//                _saxHandler.lastLemmaInEntry.clear();
//                _saxHandler.lastIdInEntry = null;
//                InputSource src = new InputSource(new java.io.ByteArrayInputStream(entry.getBytes()));
//                xr.parse(src);
//                
//                // add it to the database
//                int size = _saxHandler.lastLemmaInEntry.size();
//                if(size != 0)
//                {
//                  if(_saxHandler.lastIdInEntry == null)
//                  {
//                    // random number
//                    _saxHandler.lastIdInEntry = name + "-" + rand.nextInt();
//                  }
//                  ps_entry.setString(1, _saxHandler.lastIdInEntry);
//                  ps_entry.setString(2, entry);
//                  ps_entry.executeUpdate();
//                  
//                  
//                  Iterator<String> it = _saxHandler.lastLemmaInEntry.iterator();
//                  
//                  while(it.hasNext())
//                  {
//                    String l = it.next();
//                    ps_lemma.setString(1, l);
//                    ps_lemma.setString(2, _saxHandler.lastIdInEntry);
//                    ps_lemma.setString(3, name);
//                    ps_lemma.setString(4, l.toLowerCase()); // needed due comparison bug of SQLite
//                    ps_lemma.executeUpdate();
//                  }
//                  
//                }
//                
//              }
//              catch(SAXException saxEx)
//              {
//                saxEx.printStackTrace();
//              }
//            }
//            else
//            {
//              entry = entry + (char) c;
//            }
//          }
//        }
//        
//      }
      
      ps_entry.close();
      ps_lemma.close();
      
      reader.close();
      
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    
  }
  
  /**
   * Retreive the text between two given search texts using KMP-algorithm.
   * @param searchLeft The left search term
   * @param piLeft  KMP uses the so called PI-function (at least I learned this name).
   *                You have to provide this function by yourself by using the byte-array.
   *                piLeft is the PI-function for the left search term
   * @param searchRight The right search term
   * @param piRight See explanation for piLeft 
   * @return  null if nothing was found or parameter were incorrect and the text between two given search texts
   *          including these ones.     
   */
  private String getInnerText(Reader reader, String searchLeft, byte[] piLeft, String searchRight, byte[] piRight)
    throws java.io.IOException
  {
    String result = null;

    if(reader == null || searchLeft == null || searchRight == null)
      return null;
    
    int c;
    int q1 = 0;
    char[] P1 = searchLeft.toCharArray();    
    byte[] J1 = piLeft;
    
    if(P1.length != J1.length)
      return null;
    
    while((c = reader.read()) > -1)
    {
      while(q1 > 0 && P1[q1] != (char) c)
        q1 = J1[q1-1];

      if(P1[q1] == (char) c)
        q1++;

      if(q1 == P1.length)
      {
        q1 = J1[q1-1];

        result = searchLeft;

        int q2 = 0; // again KMP

        char[] P2 = searchRight.toCharArray();
        byte[] J2 = piRight;

        if(P2.length != J2.length)
          return null;

        while((c = reader.read()) > -1)
        {

          while(q2 > 0 && P2[q2] != (char) c)
            q2 = J2[q2-1];

          if(P2[q2] == (char) c)
            q2++;

          if(q2 == P2.length)
          {
            // we found something and will stop searching until we are called again
            q2 = J2[q2-1];

            result = result + (char) c;
            
            return result;
            
          }
          else
          {
            result = result + (char) c;
          }
        }
      }
    }
    
    return null;
    
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

  public String[][] getEntry(String lemma) 
  {
    return getEntry(lemma, null);
  }

  public String[][] getEntry(String lemma, String from)
  {
    ArrayList<String[]> list = new ArrayList<String[]>();
    try {      
      // ask the database
      ResultSet res = null;
      if(from == null)
      {
        res = _stm.executeQuery("SELECT entry, origin FROM dic WHERE dic.lemma = \"" + lemma + "\"");
      }
      else
      {        
        res = _stm.executeQuery("SELECT e.entry, l.origin FROM entry_" + from + " e, "
          + "lemma_" + from + " l "
          + "WHERE l.lemma = \"" + lemma + "\" AND l.id = e.id");
      }
      
      while(res != null && res.next())
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
  
  public List<String[]> getMatchingLemma(String name)
  {
    return getMatchingLemma(name, null);
  }
  
  public List<String[]> getMatchingLemma(String text, String from) 
  {
    ArrayList<String[]> list = new ArrayList<String[]>();
    try {      
      // ask the database
      ResultSet res = null;
      if(from == null)
      {
        res = _stm.executeQuery("SELECT lemma,origin FROM dic WHERE lemma_lower LIKE \"" + text.toLowerCase() + "%\"");
      }
      else
      {
        res = _stm.executeQuery("SELECT e.entry, l.origin FROM entry_" + from + " e, "
          + "lemma_" + from + " l "
          + "WHERE l.lemma LIKE \"" + text.toLowerCase() + "%\" AND l.id = e.id");
      }
      
      while(res != null && res.next())
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

  public String getTitle(String name)
  {
    String result = null;
    
    if(isImported(name))
    {
      try 
      {
        ResultSet res = _stm.executeQuery("SELECT title FROM imported_dics WHERE name=\"" + name + "\"");
        // we should have only one unique entry for this name
        if(res.next())
        {
          result = res.getString(1);
        }
      } 
      catch (SQLException ex) 
      {
        ex.printStackTrace();
      }
    }
    
    return result;
  } 


  public void activateDictionary(String dic) 
  {
    // check wether already active (don't do anything in that case)
    if(isImported(dic) && !_activeDics.contains(dic))
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
    if(isImported(dic) && _activeDics.contains(dic))
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
     SQLiteProvider instance = new SQLiteProvider("owl.db");
    
    // don't exit with exceptions here
    instance.activateDictionary("NonSense");
    
    instance.importDictionary("test/testC.dicml", "C");
    instance.importDictionary("test/testD.dicml", "D");
    instance.importDictionary("test/testAB.dicml", "AB");
    
    instance.activateDictionary("C");
    
    String[][] result = instance.getEntry("C");
    if(result.length == 0)
    {
      //fail("activated \"C\" was not found ");
    }
    
  }
  
  /** A data structure that holds general information about a dictionary. */
  private class DictionaryHead
  {
    public String srcLanguage;
    public String destLanguage;
    public String authorName;
    public String title;
  }
  
}
  
