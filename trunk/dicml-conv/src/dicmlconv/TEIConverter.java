/*
 * TEIConverter.java
 *
 * Created on 26. September 2006, 20:19
 *
 * See Licence.txt for more information according to this file.
 */

package dicmlconv;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author thomas
 */
public class TEIConverter 
{
  public final String XSLT_FILE_PATH = "tei2dicml.xsl";
  
  /** Creates a new instance of TEIConverter */
  public TEIConverter() 
  {
  }
  
  public void convertTEI(String In, String Out)
  {
    System.out.println("INFO: converting from TEI format");
    TransformerFactory tFactory = TransformerFactory.newInstance();
    try
    {
      URL url = getClass().getResource(XSLT_FILE_PATH);
      
      Transformer trans = tFactory.newTransformer(new StreamSource(url.toExternalForm()));
      System.out.println("INFO: starting conversion by XSLT");
      trans.transform(new StreamSource(new FileInputStream(In)), new StreamResult(new FileOutputStream(Out)));
    }
    catch(Exception ex)
    {
      System.out.println("ERROR: an exception was thrown: message follows\n");
      ex.printStackTrace();
    }
    System.out.println("INFO: finished");
  }
  
}
