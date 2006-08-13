/*
 * Login.java
 *
 * Created on 13. August 2006, 10:11
 *
 * See Licence.txt for more information according to this file.
 */

package de.gidoo.owl2.web.reader;

import wicket.markup.html.WebPage;
import wicket.markup.html.basic.Label;
import wicket.markup.html.form.*;
import wicket.markup.html.form.Button;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.model.Model;
import wicket.Session;

/**
 * Pages that displays a login interface
 *@author <a href="mailto:krause@informatik.hu-berlin.de">Thomas Krause</a>
 */
public class Login extends WebPage {
  
  TextField _txtUser;
  PasswordTextField _txtPassword;
  Form _form;
  FeedbackPanel _feedbackPanel;
    
  /** Creates a new instance of Login */
  public Login() 
  {
    add(new Label("lblTitle", getString("lblTitle")));
    
     _feedbackPanel = new FeedbackPanel("feedback");
     add(_feedbackPanel);
    
    _form = new LoginForm("formMain");
    _txtUser = new TextField("txtUser", new Model());
    _txtPassword = new PasswordTextField("txtPassword", new Model());

    _txtUser.setRequired(true);
    _txtPassword.setRequired(true);
    _txtUser.setLabel(new Model(getString("lblUser")));
    _txtPassword.setLabel(new Model(getString("lblPassword")));
    
    _form.add(_txtUser);
    _form.add(_txtPassword); 
    
    _form.add(new Label("lblUser", getString("lblUser") + ":"));
    _form.add(new Label("lblPass", getString("lblPassword") + ":"));
    
    _form.add(new Button("btLogin"));
    _form.add(new Button("btReset"));
    
    add(_form);
  }

  /////////////////////
  // CLASS LoginFORM //
  /////////////////////
  
  private final class LoginForm extends Form
  {
    public LoginForm(String id)
    {
      super(id);
    }

    protected void onSubmit() 
    {
      // try to login
      SignInSession session = (SignInSession) Session.get();
      if(session.login(_txtUser.getInput(), _txtPassword.getInput()))
      {
        setResponsePage(Admin.class);
      }
      else
      {
        error(getString("errorNoMatch"));
      }
    }
    

  }

  
}
