/*
 * SQLiteProviderTest.java
 * JUnit based test
 *
 * Created on 5. August 2006, 12:54
 */

package de.gidoo.owl2.base;

import junit.framework.*;

/**
 *
 * @author thomas
 */
public class SQLiteProviderTest extends TestCase {
  
  public SQLiteProviderTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
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
    SQLiteProvider instance = new SQLiteProvider();
    
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
    path = "test/test.dicml";
    name = "test";
    
    expResult = true;
    result = instance.importDictionary(path, name);
    assertEquals(expResult, result);
    
  }

  /**
   * Test of isImported method, of class de.gidoo.owl2.base.SQLiteProvider.
   */
  public void testIsImported() {
    System.out.println("isImported");
    
    String name = "";
    SQLiteProvider instance = new SQLiteProvider();
    
    boolean expResult = true;
    boolean result = instance.isImported(name);
    assertEquals(expResult, result);
    
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of openDictionary method, of class de.gidoo.owl2.base.SQLiteProvider.
   */
  public void testOpenDictionary() {
    System.out.println("openDictionary");
    
    String name = "";
    SQLiteProvider instance = new SQLiteProvider();
    
    boolean expResult = true;
    boolean result = instance.openDictionary(name);
    assertEquals(expResult, result);
    
    
    
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getEntry method, of class de.gidoo.owl2.base.SQLiteProvider.
   */
  public void testGetEntry() {
    System.out.println("getEntry");
    
    String lemma = "";
    SQLiteProvider instance = new SQLiteProvider();
    
    String expResult = "";
    String result = instance.getEntry(lemma);
    assertEquals(expResult, result);
    
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getMatchingLemma method, of class de.gidoo.owl2.base.SQLiteProvider.
   */
  public void testGetMatchingLemma() {
    System.out.println("getMatchingLemma");
    
    String name = "";
    SQLiteProvider instance = new SQLiteProvider();
    
    String[] expResult = null;
    String[] result = instance.getMatchingLemma(name);
    assertEquals(expResult, result);
    
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
  
}
