/*
 * PrintHTML.java
 *
 * Created on 27. Juli 2005, 18:24
 *
 */

package de.gidoo.owl;

import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.io.*;
import javax.swing.JEditorPane;
import java.net.*;

/**
 * Printing of HTML-pages.<br><br>
 * This class will ask for the desired PageFormat and then
 * create a new window with this size, add a JEditorPane
 * and print it.<br><br>
 *
 * For rendering SyncHTMLEditorKit is used which will render
 * synchronously and wait until everthing is painted
 * (so you don't get empty pages)
 *
 * @author Thomas Krause
 */
public class PrintHTML implements Printable {
  
  /** JEditorPane used for rendering. */
  private JEditorPane editor;
  
  /** Creates a new instance of PrintHTML */
  public PrintHTML()
  {
    editor = new JEditorPane();
    editor.setEditorKit(new SyncHTMLEditorKit());
    editor.setEditable(false);
  }
  
  /**
   * Perform the printing.
   *
   * @param fileToPrint the file which shall be printed
   */
  public boolean print(URL fileToPrint) throws PrinterException, IOException {
   
    PrinterJob printJob = PrinterJob.getPrinterJob();
    printJob.setPrintable(this);
    if (printJob.printDialog())
    {
      editor.setPage(fileToPrint);
      printJob.print();
      if(printJob.isCancelled())
      {
        throw(new PrinterException("print-job was cancelled"));
      }
      return true;
    }
    return false;
  }
  
  /**
   * Callback-function to paint on the printer.
   * Will only print the first page. All other pages are refused. 
   *
   * @param g the graphics to paint on
   * @param pageFormat the page-format which was choosen
   * @param pageIndex the page-index which shall be printed, printing will only be done when
   * index is zero
   */  
  public int print(Graphics g, PageFormat pageFormat, int pageIndex)
  {
    
    if (pageIndex > 0)
    {
      return(java.awt.print.Printable.NO_SUCH_PAGE);
    } 
    else
    {
      PrintUtilities.disableDoubleBuffering(editor);
      
      // create a new dialog with the dimensions of the page
      JDialog dialog = new JDialog();
      dialog.setLayout(new BorderLayout());
      int width = (int) pageFormat.getImageableWidth();
      int height = (int) pageFormat.getImageableHeight();
      dialog.setSize(width , height);
      dialog.setLocation(0, 10000);
      dialog.add(editor, BorderLayout.CENTER);
      dialog.setVisible(true);
            
      Graphics2D g2d = (Graphics2D)g;
      g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
      
      editor.paint(g2d);      
      
      PrintUtilities.enableDoubleBuffering(editor);
      
      dialog.dispose();
      return(java.awt.print.Printable.PAGE_EXISTS);
    }     
   }
}
