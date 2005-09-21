/**
DicmlRead.java

(c) 2004-2005 by Thomas Krause
All rights reserved.

http://owl.gidoo.de/
*/

package de.gidoo.owl;

import com.sun.imageio.spi.FileImageInputStreamSpi;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.print.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.html.HTMLEditorKit;

import javax.xml.transform.*;
import java.net.*;
import java.io.*;

import java.util.jar.*;
import java.util.*;

/** 
 * An extended JPanel to display entries of dicML-dictionaries
 * @see <a href="http://owl.gidoo.de">Information about dicML available at http://owl.gidoo.de</a>.
 * @author Thomas Krause
 */
class DicmlRead extends JPanel implements HyperlinkListener
{
  /** Shows the generated HTML-file. */
  private JEditorPane dicmlView;
  /** XSLT-Transformer */
  private Transformer transformer;
  private TransformerFactory tFactory;
  /** A HTML-file which is displayed if no dictionary is opened and used to 
   * update the JEditorPane on changes at the HTML-file containing the entry.*/
  private File emptyFile;
  private ResourceBundle i18n;
  private ClassLoader loader;
  private ErrorDialog errorDlg;
  private String parsedText;
  private boolean readyToPrint;
  
  /**
   * Create a new instance of DicmlRead.
   * This constructor will init the internal XSLT-processor so loading might fail.
   * @param i18n A resource-bundle which will be used for displaying of internationalized text
   */
  DicmlRead(ResourceBundle i18n)
  { 
    loader = this.getClass().getClassLoader();
        
    this.i18n = i18n;
    errorDlg = new ErrorDialog(null, true, i18n);
        
    setLayout(new GridLayout());
    dicmlView = new JEditorPane();
    dicmlView.setVisible(true);
    dicmlView.setEditable(false);
    HTMLEditorKit kit = new HTMLEditorKit();
    dicmlView.setEditorKit(kit);
    setBackground(new Color(255, 0, 0));
    
    dicmlView.addHyperlinkListener(this);
    
    JScrollPane scrollPane = new JScrollPane(dicmlView);
    add(scrollPane);    
    
   
    //init XSLT
    try
    { 
      // copy needed files
      File dir = new File(System.getProperty("user.home") + "/.owl/");
      if(!dir.exists())
      {
        dir.mkdir();
      }
      File f1 = copyFileToHome("etc/css.css", "css.css");
      File f2 = copyFileToHome("etc/entry.html", "entry.html");
      File f3 = copyFileToHome("res/owl.png", "owl.png");
      emptyFile = copyFileToHome("res/empty.html", "empty.html");
      f1.deleteOnExit();
      f2.deleteOnExit();
      f3.deleteOnExit();
      emptyFile.deleteOnExit();
      
      // other initialisation
      dicmlView.setPage(emptyFile.toURL());
     
      tFactory = TransformerFactory.newInstance();
      String lang = i18n.getLocale().getLanguage();
      File xslFile = new File("xsl/style_" + lang + ".xsl");
      if(!xslFile.exists())
      {
        IOException ioEx = new IOException("couldn't find the file \"" + xslFile.getAbsolutePath()  + "\"");
        throw(ioEx);
      }
      transformer =
        tFactory.newTransformer
         (new javax.xml.transform.stream.StreamSource(xslFile));
    }
    catch(IOException ioE)
    {
      errorDlg.showError(i18n.getString("errorLoadXSLTFile"), ioE);
    }
    catch (Exception e)
    {
      errorDlg.showError(i18n.getString("errorXSLTTransform"), e);    
    }
  }  
  
  /**
   * Used to get informed about links and show tooltips when mouse is over abbrivations.
   */
  public void hyperlinkUpdate(HyperlinkEvent evt)
  {
    String address = "";
    String title = "";
    java.awt.Graphics gc;
    java.awt.Point mousePos;
        
    // look what kind of action
        
    HyperlinkEvent.EventType type = evt.getEventType();
    if(type == HyperlinkEvent.EventType.ENTERED)
    {
      // just ENTERED
      address = evt.getDescription();
                   
      if(address.startsWith("?abbr:"))
      {
        title = address.substring(6);
              
        gc = getGraphics();
        mousePos = getMousePosition();
            
        update(gc);
        java.awt.Color oldColor = gc.getColor();
        java.awt.Color newColor = new java.awt.Color(255, 200, 0);
         
        // inner rectangle
        gc.setColor(newColor);
        java.awt.FontMetrics fm = gc.getFontMetrics();
          
        int rectX = mousePos.x;
        int rectY = mousePos.y - fm.getHeight() + fm.getDescent();
        int rectHeight = fm.getHeight();
        int rectWidth = fm.stringWidth(title);
            
        gc.fillRect(rectX, rectY, rectWidth, rectHeight);
            
        // bounds
        newColor = new java.awt.Color(0, 0, 0);
        gc.setColor(newColor);
        gc.drawRect(rectX - 1, rectY - 1, rectWidth + 1, rectHeight + 1);
           
        // text
        newColor = new java.awt.Color(0, 0, 0);
        gc.setColor(newColor);            
        gc.drawString(title, mousePos.x, mousePos.y);
        gc.setColor(oldColor);
      }
    }
    else if(type == HyperlinkEvent.EventType.EXITED)
    {
      // just EXITED
      address = evt.getDescription();
      if(address.startsWith("?abbr:"))
      {
        gc = getGraphics();
        update(gc);
      }
    }
        
  }
  
  /**
   * Parses a text containing the entry and display it.
   * Currently the dicML-specifications 0.92 and 1.00 are supported.
   * @param text The text that should be parsed. Mostly begins with &lt;entry&gt; and 
   * ends with &lt;/entry&gt;
   * @see <a href="http://owl.gidoo.de">Information about dicML available at http://owl.gidoo.de</a>
   */
  public void parse(String text)
  {
    try
    {   
        File resultFile = new File(System.getProperty("user.home") + "/.owl/entry.html");        
        
        transformer.transform
            (new javax.xml.transform.stream.StreamSource(new StringReader(text)),
                new javax.xml.transform.stream.StreamResult
                    (new FileOutputStream(System.getProperty("user.home") + "/.owl/entry.html"))); 
        // check wether an other owl-instance has deleted the file
        if(!emptyFile.exists())
        {
          File f1 = copyFileToHome("etc/css.css", "css.css");
          emptyFile = copyFileToHome("res/empty.html", "empty.html");
          f1.deleteOnExit();
          emptyFile.deleteOnExit();
        }
        dicmlView.setPage(emptyFile.toURL());
        dicmlView.setPage(resultFile.toURL());
        parsedText = text;
    }
    catch (Exception e)
    {
      errorDlg.showError(i18n.getString("errorCallEntry"), e);
    }
    
  }
  
  /**
   * Private helper-function for copying files from the application-directory 
   * to the user's home-directory.
   * @param oldPath path to the source-file relative to the application-directory
   * @param newPath path to the destination-file relative to the home-dir-directory 
   */
  private File copyFileToHome(String oldPath, String newPath)
  {
    try
    {
      File file = new File(System.getProperty("user.home") + "/.owl/" + newPath);
      if(!file.exists())
      {
        file.createNewFile();
        FileOutputStream writer = new FileOutputStream(file);
        FileInputStream reader = new FileInputStream(oldPath);
        //FileReader reader = new FileReader(oldPath);
        byte[] buffer = new byte[1];
        while((reader.read(buffer)) > -1)
        {
          writer.write(buffer);
        }
        writer.close();
      }
      return file;
    }
    catch(IOException ioE)
    {
      errorDlg.showError(i18n.getString("errorCopyHomeFile"), ioE);
      return null;
    }
  }
  
  /**
   * Retreives the text which is currently used for rendering.
   * @return the text which is currently used for rendering
   */
  public String getText()
  {
    return new String(parsedText);
  }
  
  /**
   * Retreives the URL to the page currently displayed.
   * @return the URL to the page which is currently displayed
   */
  public URL getPage()
  {
    return dicmlView.getPage();
  }
  
}
