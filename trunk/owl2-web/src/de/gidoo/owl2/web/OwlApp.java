/*
 * OwlApp.java
 *
 * Created on July 17, 2006, 9:06 PM
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.web;

/**
 * The application class for owl2
 * @author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class OwlApp extends wicket.protocol.http.WebApplication {
    
    /** Creates a new instance of OwlApp */
    public OwlApp() {
    }

    public Class getHomePage() {
        // start with OwlReader in test-phase
        return OwlReader.class;
    }
    
}
