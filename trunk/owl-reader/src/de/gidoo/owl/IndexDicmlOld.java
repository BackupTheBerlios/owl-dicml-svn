/**
IndexDicmlOld.java

(c) 2004-2005 by Thomas Krause
All rights reserved.

http://owl.gidoo.de
*/

package de.gidoo.owl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * Display the indexing-dialog.
 * Indexing of a dictionary is done to decrease the loading time of the dicml-file.
 * @author Thomas Krause
 */
class IndexDicmlOld extends JDialog implements ActionListener 
{
  public ResourceBundle i18n;
  
  JLabel lInfo;
  JProgressBar pbStatus;
  JButton btOk, btAbort;
  
  /** Can be used by the the called worker-thread to tell that something went wrong*/
  public boolean errorOccured;
  
  private File dicmlFile;
  
  /** A seperate thread is used to perform the actual indexing. */
  private IndexDicmlWork workerThread;
  
  public ErrorDialog errorDlg;
  
  /**
   * Create a new instance of IndexDicml and start indexing.
   * @param pathToFile path to the dicml-file which shall be indexed
   * @param owner the frame which own this dialog
   * @param modal wether indexing should be done in the background or the user has to wait
   * @param i18n the resource-bundle which is used to show internationalized messages
   */
  IndexDicmlOld(String pathToFile, Frame owner, boolean modal, ResourceBundle i18n)
  {
    super(owner, modal);
    
    errorOccured = false;
    
    this.i18n = i18n;
    errorDlg = new ErrorDialog(owner, true, i18n);
    
    //setPreferredSize(new Dimension(500, 100));
    
    GridBagLayout gbl = new GridBagLayout();
    GridBagConstraints gbc;
    setLayout(gbl);
    
    gbc = new GridBagConstraints();
    lInfo = new JLabel();
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(1, 1, 1, 1);
    gbl.setConstraints(lInfo, gbc);
    add(lInfo);
    
    dicmlFile = new File(pathToFile);
    lInfo.setText(i18n.getString("indexFile1") + dicmlFile.getName() + i18n.getString("indexFile2"));
    
    gbc = new GridBagConstraints();
    pbStatus = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(1, 1, 1, 1);
    gbl.setConstraints(pbStatus, gbc);
    pbStatus.setStringPainted(true);
    pbStatus.setString("0%");
    pbStatus.setValue(0);
    add(pbStatus);

    gbc = new GridBagConstraints();
    btOk = new JButton(i18n.getString("indexOk"));
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.insets = new Insets(1, 1, 1, 1);
    gbl.setConstraints(btOk, gbc);
    btOk.setEnabled(false);
    btOk.addActionListener(this);
    btOk.setActionCommand("ok");
    add(btOk);

    gbc = new GridBagConstraints();
    btAbort = new JButton(i18n.getString("indexAbort"));
    gbc.gridx = 1;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.insets = new Insets(1, 1, 1, 1);
    btAbort.setEnabled(true);
    btAbort.addActionListener(this);
    btAbort.setActionCommand("abort");
    gbl.setConstraints(btAbort, gbc);
    add(btAbort);
    
    pack();
    
    setTitle(i18n.getString("indexState"));
    this.setLocation(100, 100);
    
    //start new worker-thread
 /*
    workerThread = new IndexDicmlWork(dicmlFile, this);
    workerThread.start();
   */ 
    setVisible(true);
  }
  
  /**
   * Reacts when one of the buttons has been pressed.
   * The window is closed afer after a sucessful indexing (ok) or the indexing is
   * aborted (abort).
   */
  public void actionPerformed(ActionEvent e)
  {
    if(e.getActionCommand().equals("ok"))
    {
      setVisible(false);
      dispose();
    }
    else if(e.getActionCommand().equals("abort"))
    {
      workerThread.abort = true;
    }
}
    
  /**
   * Set the the status-bar and display informations depending on the actual state.
   * This is called by the worker-thread<br><br>
   * states: <br>
   * - read in the file (0-79%)<br>
   * - sort the entries (80%)<br>
   * - write the id-file (90%)<br>
   * - finished (101-200%)<br>
   * - an error occured (more than 200%)
   * @param alreadyDone how much work is done in percent
   */
  public void setStatusBarValue(int alreadyDone)
  {
    if(alreadyDone > 200)
    {
      // an error occured
      errorOccured = true;
      
      btAbort.setEnabled(false);
      btOk.setEnabled(true);
      lInfo.setText(i18n.getString("indexFinishedError"));
      pbStatus.setIndeterminate(false);
      pbStatus.setValue(100);
      pbStatus.setString("100%");
      pack();
    }
    else if(alreadyDone > 100)
    {
      btAbort.setEnabled(false);
      btOk.setEnabled(true);
      lInfo.setText(i18n.getString("indexFinished"));
      pbStatus.setIndeterminate(false);
      pbStatus.setValue(100);
      pbStatus.setString("100%");
      pack();
    }
    else if(alreadyDone == 80)
    {
      pbStatus.setIndeterminate(true);
      btAbort.setEnabled(false);
      pbStatus.setValue(80);
      pbStatus.setString("");
      lInfo.setText(i18n.getString("indexSorting"));
      pack();     
    }
    else if(alreadyDone == 90)
    {
      btAbort.setEnabled(false);
      pbStatus.setIndeterminate(true);
      pbStatus.setValue(80);
      pbStatus.setString("");
      lInfo.setText(i18n.getString("indexWriting"));
      pack();
    }
    else
    {
      pbStatus.setValue(alreadyDone);
      pbStatus.setString(new String().valueOf(alreadyDone) + "%");
    }
  }
  
}
