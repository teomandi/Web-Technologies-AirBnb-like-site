package model;

import java.math.BigInteger;
import java.security.MessageDigest;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpSession;



@ManagedBean(name="login")
public class LoginBean {
	
	private String password;
	private String msg;
	private String username;
	private String special_button;
	private final DataQuery query=new DataQuery();
	private UIComponent  username_UI;
	
	//ektelei to login kai dhmiourgei session
	public String validateUsernamePassword() {
		String hashtext=null;
		try {
			//metatrepei ton kwdiko se md5
			byte[] bytesOfMessage;
			bytesOfMessage = password.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			BigInteger bigInt = new BigInteger(1, thedigest);
			hashtext = bigInt.toString(16);
			
			while(hashtext.length() < 32 ){
				hashtext = "0"+hashtext;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//pairnei ta dedomena apth vash kai ta checkare to DAO
		boolean valid = query.login_validation(username, hashtext);
		
		FacesContext context =FacesContext.getCurrentInstance();
		if (valid) {
			HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
			session.setAttribute("username", username);
			
			
			User u=query.findUser(username);
			session.setAttribute("user_id", u.getUserId());
			
			//an einai admin emfanizei to button pou ton odhgei sth selida tou
			//kai me to login ton odhgei sth selida tou
			if(u.getRole().equals("A")) {
				special_button="Admin Page";
				session.setAttribute("special_button",special_button);
				return "admin_page.xhtml?faces-redirect=true";
			}
			//omoiws me ton admin
			else if(u.getRole().equals("H")) {
				special_button="Host Page";
				session.setAttribute("special_button",special_button);
				return "host_page.xhtml?faces-redirect=true\";";
			}
			session.setAttribute("special_button","");
			return "";
			
			
		} else {
			System.out.println("message");
	        context.addMessage(username_UI.getClientId(context), 
	        		new FacesMessage(FacesMessage.SEVERITY_WARN, "Incorrect Username or Passowrd", null)); 
			return ""; 
		}
	}
	
	
	//log out
	public String logout() {
		System.out.println("logout");
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "welcome.xhtml?faces-redirect=true";
    }
	
	
	public String getUserName() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		return session.getAttribute("username").toString();
	}
	
	
	public String get_profile() {
		return "profile.xhtml?faces-redirect=true";
	}
	
	
	
	public boolean isLoged() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		try{
			if(session.getAttribute("username").toString() != null)
				return true;
			else return false;
			}
		catch(Exception e) {
			return false;
		}
		
	}
	
	//energopoiei ta special button
	public boolean enable_special_button(){
		username=getUserName();
		User u=query.findUser(username);
		if(u.getRole().equals("U"))
			return false;
		return true;
			
	}
	
	//kateuthynei analogws an einai admin h host
	public String special_button_action() {
		String s_button=special_button_value();
		if(s_button.equals("Admin Page")) {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
					.getExternalContext().getSession(true);
			if(session.getAttribute("username_2")!=null);
				session.removeAttribute("username_2");
			return "admin_page.xhtml?faces-redirect=true";
		}
		else 
			return "host_page.xhtml?faces-redirect=true";
	}
	
	//epistrefei to value tou button
	public String special_button_value() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		return session.getAttribute("special_button").toString();
		
	}
	

	public UIComponent getUsername_UI() {
		return username_UI;
	}
	public void setUsername_UI(UIComponent username_UI) {
		this.username_UI = username_UI;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
		
	public String getSpecial_button() {
		return special_button;
	}
	
	public void setSpecial_button(String special_button) {
		this.special_button = special_button;
	}
	

}
