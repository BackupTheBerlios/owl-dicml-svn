/*
 * Main.java
 *
 * Created on 22. Februar 2005, 17:25
 */

package dicmlconv;

import java.awt.Dialog;
import java.io.*;

/**
 *
 * @author thomas
 */
public class Main {
  
    
  
    /** Creates a new instance of Main */
    public Main()
    {
    }
    
    
    void printHelp() 
    {
      System.out.print("\ndicml-conv vers. 0.01\n"+
                       "---------------------\n\n" +
                       "usage: ask me: tho.krause@gmail.com\n\n");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Main app = new Main();
        
        if(args.length > 0)
        {
          if(args[0].equals("-chemnitz"))
          {
            ChemnitzConverter conv = new ChemnitzConverter();
            if(args.length >= 4)
            {
              ChemnitzConverter.TranslationDirection dir;
              if(args[3].equals("right"))
              {
                dir = ChemnitzConverter.TranslationDirection.RIGHT_SIDE;
              }
              else
              {
                dir = ChemnitzConverter.TranslationDirection.LEFT_SIDE;
              }
                      
              conv.convertChemnitz(args[1], args[2], "", dir);
              return;
            }
          }
          else if(args[0].equals(("-toASCII")))
          {
            UnicodeTransform trans = new UnicodeTransform();
            if(args.length >= 3)
            {
              trans.transformToASCII(args[1], args[2]);
              return;
            }
          }
          else if(args[0].equals(("-removeMasked")))
          {
            UnicodeTransform trans = new UnicodeTransform();
            if(args.length >= 4)
            {
              boolean winEndOfLine = Boolean.parseBoolean(args[3]);
              trans.removeMasked(args[1], args[2], winEndOfLine);
              return;
            }
          }
        }
        
        app.printHelp();
    }
    
}
