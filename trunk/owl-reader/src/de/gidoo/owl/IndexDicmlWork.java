/**
IndexDicmlWork.java

(c) 2004 by Thomas Krause
All rights reserved.

http://owl.gidoo.de
*/

package de.gidoo.owl;

import com.sun.org.apache.xpath.internal.axes.IteratorPool;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * Does all the work for IndexDicml
 * @author Thomas Krause
 */
class IndexDicmlWork extends Thread
{
  /** The file to index. */   
  private File fileDicml;
  
  private IndexDicml owner;
  
  private LinkedList<Entry> entryList;
  
  private AlphabetOrderLight order;
  
  /** Can be used by the calling gui-thread to abort everything. */
  public boolean abort;
  
  /**
   * Create a new instance of IndexDicmlWork.
   * @param dicmlFile the file which shall be indexed
   * @param dialog the parent dialog
   */
  IndexDicmlWork(File dicmlFile, IndexDicml dialog)
  {
    fileDicml = dicmlFile;
    owner = dialog;
  }
  
  /**
   * Start the thread and begin indexing.
   */
  public void run()
  {
    abort = false;
    index();
  }
  
  /**
   * Index a dicml-File
   */
  private boolean index()
  {
    File fileId = new File(fileDicml.getAbsolutePath() + ".id");
    //FileReader dicmlReader;
    //FileWriter idWriter;
    InputStreamReader dicmlReader;
    OutputStreamWriter idWriter;
    String dic_buf;
    String entry_buf = new String();
    String lemma, num;
    int c;
    int i=0;
    int to_do = -2;
    double alreadyDone = 0, i_buf;
    long file_size = fileDicml.length();
    long entry_start = -1, entry_end = -1;
    
    String sourceLanguage = "";
    
    Entry curEntry;
    
    try
    {
      //create new id-file
      
      entryList = new LinkedList<Entry>();
        
      fileId.createNewFile();
      //idWriter = new FileWriter(fileId);
      //dicmlReader = new FileReader(fileDicml);    
      idWriter = new OutputStreamWriter(new FileOutputStream(fileId), "UTF-8");
      dicmlReader = new InputStreamReader(new FileInputStream(fileDicml), "UTF-8");
      dic_buf = new String();
      
      while( ((c = dicmlReader.read()) != -1) && abort == false )
      {
        // search for the source-language
        if(to_do == -2)
        {
          dic_buf += (char) c;
          if(dic_buf.indexOf("<dic.lang-s>") != -1)
          {
            to_do = -1;
          }
        }
        else if(to_do == -1)
        {
          if((char) c == '<')
          {
            
            // init the AlphabetOrder
            order = new AlphabetOrderLight(new File("res/" + sourceLanguage + ".order.xml"));                    
            //order = new AlphabetOrderLight(new java.util.Locale(sourceLanguage));
            
            to_do = 0;
          }
          else
          {
            sourceLanguage += (char) c;    
          }
        }
        //<entry>
        else if(to_do == 0)
        {
          dic_buf += (char) c;
          if(dic_buf.indexOf("<entry") != -1)
          {
            //a new entry
           entry_buf = new String();
           entry_buf += (char) c;
           entry_start = i-5;
           
           to_do = 1;
         }
        }
        
        //</entry>
        else if(to_do == 1)
        {
          entry_buf += (char) c;
          if(entry_buf.indexOf("</entry>") != -1)
          {
           entry_end = i;
           //create a new entry in the id-file
            
            //first get the name value of the num-tag
            int begin, end;
            begin = entry_buf.indexOf("<num>")+5;
            end = entry_buf.indexOf("</num>");
            if((begin >= 0) && (end >=0))
            {
              num = entry_buf.substring( begin, end);
            }
            else
            {
              num = "";
            }
            //now the lemma itself
            begin = entry_buf.indexOf("<l>")+3;
            end = entry_buf.indexOf("</l>");
            if((begin >= 0) && (end >=0))
            {
              lemma = entry_buf.substring(begin, end);
            }
            else
            {
             lemma = "not found"; 
            }
            
            lemma = UnicodeHelpers.glyphToUnicode(lemma);
            num = UnicodeHelpers.glyphToUnicode(num);
            
            curEntry = new Entry();
            curEntry.start = entry_start;
            curEntry.end = entry_end;
            //curEntry.compare = lemma;
            curEntry.compareProcessed = order.getCompareable(lemma);
            //curEntry.collationKey = order.getCollationKey(lemma);
            
            //add num to lemma
            if(!num.equals(""))
            {  
              lemma = lemma + " (" + num + ")";
            }
                        
            curEntry.lemma = lemma;
            
            // add to list
            entryList.add(curEntry);
            
            dic_buf = new String();
            to_do = 0;
          }
        }
        // get position in file 
        String str = "" + (char) c;
        i += str.getBytes("UTF-8").length;
      
        if(i % 100 == 0)
        {
          //update status-bar
          i_buf = (double) i;
          alreadyDone =  (i_buf / file_size) * 80;
          owner.setStatusBarValue((int) alreadyDone);
          
        }
        
      }
    
    // sort the list
    if(abort == false)
    {
      owner.setStatusBarValue(80);
      Collections.sort(entryList, order);
      owner.setStatusBarValue(90);
      // write the source language
      idWriter.write(sourceLanguage + "\n");
      
      //for(int z = 0; z < el.length; z++)
      ListIterator iterator = entryList.listIterator(0);
      while(iterator.hasNext())
      {
        Entry en = (Entry) iterator.next();
        idWriter.write(en.lemma + "\n" + en.start + "\n" + en.end + "\n");
      }
    
      owner.setStatusBarValue(90);
    }
      
    idWriter.close();
    dicmlReader.close();
    
    if(abort)
    {
      fileId.delete();
    }
        
    owner.setStatusBarValue(101);
    }
    catch (Exception e)
    {
      owner.errorDlg.showError(owner.i18n.getString("errorWhenIndexing"), e);
      owner.setStatusBarValue(201);
      fileId.delete();
    }
    
    return true;
  }
  
}
