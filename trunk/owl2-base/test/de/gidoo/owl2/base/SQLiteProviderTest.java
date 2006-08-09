/*
 * SQLiteProviderTest.java
 * JUnit based test
 *
 * Created on 5. August 2006, 12:54
 */

package de.gidoo.owl2.base;

import junit.framework.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 *
 * @author thomas
 */
public class SQLiteProviderTest extends TestCase {
  
  public SQLiteProviderTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
    // remove "owl.db" in order to get a clean enviroment
    File f = new File("owl.db");
    f.delete();
  }

  protected void tearDown() throws Exception {
  }

  public static Test suite() {
    TestSuite suite = new TestSuite(SQLiteProviderTest.class);
    
    return suite;
  }

  /**
   * Test of importDictionary method, of class de.gidoo.owl2.base.SQLiteProvider.
   */
  public void testImportDictionary() {
    System.out.println("importDictionary");
    
    
    // test for a not existing file
    String path = "not existing!!!";
    String name = "";
    SQLiteProvider instance = new SQLiteProvider("owl.db");
    
    boolean expResult = false;
    boolean result = instance.importDictionary(path, name);
    assertEquals(expResult, result);
    
    // test on invalid parameters 1
    path = null;
    name = "";
    
    expResult = false;
    result = instance.importDictionary(path, name);
    assertEquals(expResult, result);
    
    // test on invalid parameters 2
    path = "";
    name = null;
    
    expResult = false;
    result = instance.importDictionary(path, name);
    assertEquals(expResult, result);
    
    
    // test on real existing file
    path = "test/testAB.dicml";
    name = "AB";
    
    expResult = true;
    result = instance.importDictionary(path, name);
    assertEquals(expResult, result);
    
    // check wether import was successfull
    instance.activateDictionary("AB");
    String[] r = instance.getEntry("A");
    if(r.length == 0)
      fail("the imported lemma \"A\" was not found");
    
    r = instance.getEntry("b");
    if(r.length == 0)
      fail("the imported lemma \"b\" was not found");
    
  }

  /**
   * Test of isImported method, of class de.gidoo.owl2.base.SQLiteProvider.
   */
  public void testIsImported() {
    System.out.println("isImported");
    
    String name = "C";
    SQLiteProvider instance = new SQLiteProvider("owl.db");
    
    // this should not fail!!!
    instance.isImported("nonsense");
    
    instance.importDictionary("test/testC.dicml", name);
    
    boolean expResult = true;
    boolean result = instance.isImported(name);
    assertEquals(expResult, result);
    
    expResult = false;
    result = instance.isImported("S");
    assertEquals(expResult, result);
       
  }

  
  /**
   * Test of getEntry method, of class de.gidoo.owl2.base.SQLiteProvider.
   */
  public void testGetEntry() {
    System.out.println("getEntry");
    
    String lemma = "Match";
    SQLiteProvider instance = new SQLiteProvider("owl.db");
    
    instance.importDictionary("test/testMatch.dicml", "Get");
    instance.activateDictionary("Get");
    
    String[] result = instance.getEntry(lemma);
    if(result.length != 1)
      fail("there should be *exactly* one \"Match\"");
    
    lemma = "C";
    result = instance.getEntry(lemma);
    if(result.length != 0)
      fail("there should be *no* \"C\"");
    
    lemma = "short";
    String[] expResult = new String[] {"<entry id=\"test-short\"><lemma><l>short</l></lemma></entry>"}; 
    result = instance.getEntry(lemma);
    if(result.length != 1)
      fail("there should be a match for \"short\"");
    assertEquals(expResult[0], result[0]);
    
  }

  /**
   * Test of getMatchingLemma method, of class de.gidoo.owl2.base.SQLiteProvider.
   */
  public void testGetMatchingLemma() {
    System.out.println("getMatchingLemma");
    
    String name = "Ma";
    SQLiteProvider instance = new SQLiteProvider("owl.db");
    
    instance.importDictionary("test/testMatch.dicml", "Match");
    instance.activateDictionary("Match");
    
    List<String> result = instance.getMatchingLemma(name);
    if(result.size() != 1)
      fail("there should be *exactly* one match for \"Ma\"");
    
    name = "Mis";
    result = instance.getMatchingLemma(name);
    if(result.size() != 1)
      fail("there should be *exactly* one match for \"Mis\"");
    
    name = "M";
    result = instance.getMatchingLemma(name);
    if(result.size() != 2)
      fail("there should be *exactly* one match for \"M\"");
    
    name = "Nons";
    result = instance.getMatchingLemma(name);
    if(result.size() != 0)
      fail("there should be *no* match for \"Nons\"");
    
    name = "short";
    String expResult = "short"; 
    result = instance.getMatchingLemma(name);
    if(result.size() != 1)
      fail("there should be a match for \"short\"");
    assertEquals(expResult, result.get(0));

  }

  /**
   * Test of finalize method, of class de.gidoo.owl2.base.SQLiteProvider.
   */
  public void testFinalize() {
    System.out.println("finalize");
    
    SQLiteProvider instance = new SQLiteProvider("owl.db");
    
    instance.finalize();
        
    // nothing to test here (except that no Exception is thrown)
  }

  /**
   * Test of getAvailableDictionaries method, of class de.gidoo.owl2.base.SQLiteProvider.
   */
  public void testGetAvailableDictionaries() {
    System.out.println("getAvailableDictionaries");
    
    SQLiteProvider instance = new SQLiteProvider("owl.db");
        
    String[] expResult = new String[0];
    String[] result = instance.getAvailableDictionaries();
    assertEquals(expResult.length, result.length);
    
    instance.importDictionary("test/testC.dicml", "C");
    
    expResult = new String[] {"C"};
    result = instance.getAvailableDictionaries();
    assertEquals(expResult[0], result[0]);
  }

  /**
   * Test of activateDictionary method, of class de.gidoo.owl2.base.SQLiteProvider.
   */
  public void testActivateDictionary() {
    System.out.println("activateDictionary");
    
    SQLiteProvider instance = new SQLiteProvider("owl.db");
    
    instance.importDictionary("test/testC.dicml", "C");
    instance.importDictionary("test/testD.dicml", "D");
    instance.importDictionary("test/testAB.dicml", "AB");
    
    instance.activateDictionary("C");
    
    String[] result = instance.getEntry("C");
    if(result.length == 0)
      fail("activated \"C\" was not found ");
    
    result = instance.getEntry("D");
    if(result.length != 0)
      fail("\"C\" was found even if it wasn't activated");
    
    instance.activateDictionary("AB");
    result = instance.getEntry("A");
    if(result.length == 0)
      fail("activated \"A\" was not found ");

  }

  /**
   * Test of deactivateDictionary method, of class de.gidoo.owl2.base.SQLiteProvider.
   */
  public void testDeactivateDictionary() {
    System.out.println("deactivateDictionary");
    

    SQLiteProvider instance = new SQLiteProvider("owl.db");
    
    instance.importDictionary("test/testC.dicml", "C");
    instance.importDictionary("test/testD.dicml", "D");
    instance.importDictionary("test/testAB.dicml", "AB");
    
    instance.activateDictionary("C");
    
    String[] result = instance.getEntry("A");
    if(result.length != 0)
      fail("deactivated \"A\" was found ");
    
    instance.deactivateDictionary("C");
    
    result = instance.getEntry("C");
    if(result.length != 0)
      fail("deactivated \"C\" was found");
    
    instance.activateDictionary("AB");
    result = instance.getEntry("A");
    if(result.length == 0)
      fail("activated \"A\" was not found ");
  }

  /**
   * Test of deleteDictionary method, of class de.gidoo.owl2.base.SQLiteProvider.
   */
  public void testDeleteDictionary() {
    System.out.println("deleteDictionary");
    
    String name = "C";
    SQLiteProvider instance = new SQLiteProvider("owl.db");
    
    instance.importDictionary("test/testC.dicml", "C");
    
    boolean expResult = true;
    boolean result = instance.deleteDictionary(name);
    assertEquals(expResult, result);
    
    // additionally test, wether really removed
    String[] r1 = instance.getEntry("C");
    if(r1.length != 0)
      fail("removed \"C\" still exists ");
    
    r1 = instance.getAvailableDictionaries();
    if(r1.length != 0)
      fail("deleted dic still in list of dictionaries");
    
    // I don't trust you
    instance.importDictionary("test/testC.dicml", "C");
    instance.importDictionary("test/testD.dicml", "D");
    instance.activateDictionary("D");
    
    expResult = true;
    result = instance.deleteDictionary(name);
    assertEquals(expResult, result);
    
    expResult = false;
    result = instance.deleteDictionary("NOTEXISTING");
    assertEquals(expResult, result);
    
    // additionally test, wether really removed
    r1 = instance.getEntry("C");
    if(r1.length != 0)
      fail("removed \"C\" still exists ");
    
    r1 = instance.getAvailableDictionaries();
    if(r1.length != 1)
      fail("deleted dic still in list of dictionaries");
    
    // make sure that the rest is still there
    r1 = instance.getEntry("D");
    if(r1.length == 0)
      fail("\"D\" was accidently removed");
            
  }
 
}
