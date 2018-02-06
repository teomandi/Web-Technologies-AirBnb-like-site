package model;

import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.UploadedFile;

@ManagedBean(name="profile")

public class ProfileBean {
	private User u;
	
	private String username;
	private String name;
	private String surname;
	private String phone;
	private String email;
	private String role;
	private String profile_pic;
	private String editing_field;
	private String old_value;
	private String new_value;
	private String c_pwd;
	
	List<UserRoomInteraction> uri_list;
	
	private UIComponent button1_UI;
	private UIComponent button2_UI;
	private UIComponent button3_UI;
	private UploadedFile new_pic;
	
	private final DataQuery query=new DataQuery();

	
	
	public boolean render_adminPage() {
		
		if(role.equals("A"))
			return true;
		return false;
	}

	@PostConstruct
	public void init() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		setUsername(session.getAttribute("username").toString());
		
		u=query.findUser(username);
		
		this.name=u.getName();
		this.surname=u.getSurname();
		this.email=u.getEmail();
		this.phone=u.getPhone();
		this.role=u.getRole();
		this.profile_pic=u.getProfilePic();
		
		uri_list=u.getUserRoomInteractions();
		
	}

	
	/*public String edit_username() {

		System.out.println("Edit_username "+username+" "+name);
		editing_field="username";
		
		FacesContext context =FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		session.setAttribute("editing_field",editing_field);
		
		old_value=username;
		session.setAttribute("old_value",old_value);
		return "edit_page.xhtml?faces-redirect=true";
	}*/
	
	/*
	 * ta edit_X kratane tis pallies times tou arxeiou 
	 * kai kathorizoun to pedio editing value*/
	public String edit_name() {

		System.out.println("Edit_name "+this.getName());
		editing_field="name";
		
		FacesContext context =FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		session.setAttribute("editing_field",editing_field);
		
		old_value=name;
		session.setAttribute("old_value",old_value);
		return "edit_page.xhtml?faces-redirect=true";
	}
	
	
	public String edit_surname() {
		System.out.println("Edit_surname");
		editing_field="surname";
		
		FacesContext context =FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		session.setAttribute("editing_field",editing_field);
		
		old_value=surname;
		session.setAttribute("old_value",old_value);
		return "edit_page.xhtml?faces-redirect=true";
	}

	public String edit_phone() {
		
		System.out.println("Edit_phone");
		editing_field="phone";
		
		FacesContext context =FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		session.setAttribute("editing_field",editing_field);
		
		old_value=phone;
		session.setAttribute("old_value",old_value);
		return "edit_page.xhtml?faces-redirect=true";
	}

	public String edit_email() {
		
		System.out.println("Edit_email");
		editing_field="email";
		
		FacesContext context =FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		session.setAttribute("editing_field",editing_field);
		
		old_value=email;
		session.setAttribute("old_value",old_value);
		return "edit_page.xhtml?faces-redirect=true";
	}

	public String edit_role() {
		
		System.out.println("Edit_role");
		editing_field="role";

		FacesContext context =FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		session.setAttribute("editing_field",editing_field);
		
		if(role.equals("H"))
			old_value="HOST";
		else
			old_value="USER";
		session.setAttribute("old_value",old_value);
		return "edit_page.xhtml?faces-redirect=true";
	}
	
	public String edit_pic() {
		
		System.out.println("Edit_role");
		editing_field="picture";

		FacesContext context =FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		session.setAttribute("editing_field",editing_field);
		
		old_value=profile_pic;
		session.setAttribute("old_value",old_value);
		return "edit_page.xhtml?faces-redirect=true";
	}
	
	public String edit_pwd() {
		
		System.out.println("Edit_password");
		editing_field="password";

		FacesContext context =FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		session.setAttribute("editing_field",editing_field);
		session.setAttribute("old_value","");
		return "edit_page.xhtml?faces-redirect=true";

	}
	
	//analogws to poia timh ginetai edit tote dexnei to katallhlo panel
	public boolean other_panel() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		setEditing_field(session.getAttribute("editing_field").toString());
		setOld_value(session.getAttribute("old_value").toString());
		
		if(editing_field.equals("role") || editing_field.equals("password") || editing_field.equals("picture"))
			return false;
		return true;
	}
	
	public boolean role_panel() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		setEditing_field(session.getAttribute("editing_field").toString());
		setOld_value(session.getAttribute("old_value").toString());
		System.out.println("role_panel "+editing_field);
		if(editing_field.equals("role") )
			return true;
		return false;
	}
	
	public boolean pwd_panel() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		setEditing_field(session.getAttribute("editing_field").toString());
		
		System.out.println("pwd_panel "+editing_field);
		if(editing_field.equals("password") )
			return true;
		return false;

	}
	
	public boolean pic_panel() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		setEditing_field(session.getAttribute("editing_field").toString());
		
		System.out.println("pic_panel "+editing_field);
		if(editing_field.equals("picture") )
			return true;
		return false;
	}
	
	//kanei to edit!...elegxei tis times pou exoun dwthei
	public String edit() {
		System.out.println("EDIT");
		
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		setEditing_field(session.getAttribute("editing_field").toString());
		
		
		System.out.println("edit "+ editing_field+" new -"+new_value+ "- "+ username);
		if(!editing_field.equals("picture"))
			if(new_value.equals(""))
				return "profile.xhtml?faces-redirect=true";
		
		if(editing_field.equals("username")) {
			setOld_value(session.getAttribute("old_value").toString());
			System.out.println("editing username");
			if(query.Username_exist(new_value)){
				FacesMessage message = new FacesMessage("Username exist!");
		        FacesContext context = FacesContext.getCurrentInstance();
		        context.addMessage(button1_UI.getClientId(context), message);
				return "";
			}
			u.setUsername(new_value);
			FacesContext context =FacesContext.getCurrentInstance();
			session = (HttpSession) context.getExternalContext().getSession(true);
			session.setAttribute("username",new_value);
		}
		else if(editing_field.equals("name")) {
			setOld_value(session.getAttribute("old_value").toString());
			System.out.println("editing name");
			u.setName(new_value);
		}
		else if(editing_field.equals("surname")) {
			setOld_value(session.getAttribute("old_value").toString());
			System.out.println("editing surname");
			u.setSurname(new_value);
		}
		else if(editing_field.equals("email")) {
			setOld_value(session.getAttribute("old_value").toString());
			System.out.println("editing email");
			u.setEmail(new_value);
		}
		else if(editing_field.equals("phone")) {
			setOld_value(session.getAttribute("old_value").toString());
			System.out.println("editing phone");
			u.setPhone(new_value);
		}
		else if(editing_field.equals("role")) {
			setOld_value(session.getAttribute("old_value").toString());
			System.out.println("editing role");
			u.setRole(new_value);
			if(new_value.equals("H")){
				String special_button="Host Page";
				session.setAttribute("special_button",special_button);
				
				
				if(u.getAApprove()!=null) {
					if(!u.getAApprove().equals("Approved")) {
						u.setAApprove("Not Approved");
						String message_2="Admin will approve your registration as a host, till then you can use the site as a Typical User. ";
						FacesMessage facesMessage_2 = new FacesMessage(FacesMessage.SEVERITY_INFO, message_2, null);
						FacesContext context_2=FacesContext.getCurrentInstance();
						context_2.addMessage("msg",facesMessage_2);
						context_2.getExternalContext().getFlash().setKeepMessages(true);
						
						
					}
					else {
						String message_2="You're already approved as a Host. ";
						FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, message_2, null);
						FacesContext context=FacesContext.getCurrentInstance();
						context.addMessage(null,facesMessage);
						context.getExternalContext().getFlash().setKeepMessages(true);
					}
				}
				else {
					u.setAApprove("Not Approved");
					
					String message_2="Admin will approve your registration as a host, till then you can use the site as a Typical User. ";
					FacesMessage facesMessage_2 = new FacesMessage(FacesMessage.SEVERITY_INFO, message_2, null);
					FacesContext context_2=FacesContext.getCurrentInstance();
					context_2.addMessage("msg",facesMessage_2);
					context_2.getExternalContext().getFlash().setKeepMessages(true);
				}
				
				
				
			}
			else
				session.setAttribute("special_button","");
		}
		else if(editing_field.equals("password")) {
			System.out.println("editing password-"+old_value+"-"+new_value+"-"+c_pwd);
			
			String hashtext=null;
			try {
				byte[] bytesOfMessage;
				bytesOfMessage = old_value.getBytes("UTF-8");
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
			
			if(hashtext.equals(u.getPassword())) {
				if(new_value.equals(c_pwd)) {
					try {
						byte[] bytesOfMessage;
						bytesOfMessage = new_value.getBytes("UTF-8");
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
					u.setPassword(hashtext);
					System.out.println("Success");
				}
				else {
					FacesMessage message = new FacesMessage("Password didn't match");
			        FacesContext context = FacesContext.getCurrentInstance();
			        context.addMessage(button2_UI.getClientId(context), message);
					return "";
				}
			}
			else {
				FacesMessage message = new FacesMessage("Incorrect old Password");
		        FacesContext context = FacesContext.getCurrentInstance();
		        context.addMessage(button2_UI.getClientId(context), message);
				return "";
			}
		}
		else if(editing_field.equals("picture")) {
			String profile_url;
			try{
				
				String[] parts=new_pic.getFileName().split("\\.");
				if(parts[parts.length-1].equals("gif") || 
						parts[parts.length-1].equals("png") ||
						parts[parts.length-1].equals("jpe") ||
						parts[parts.length-1].equals("jpg") ) {
					//(?)											
					Path folder = Paths.get("/home/giwrgosmandi/eclipse-workspace/Airbnb/WebContent/resources/user_images");
					
					String filename = FilenameUtils.getBaseName(new_pic.getFileName()); 
					String extension = FilenameUtils.getExtension(new_pic.getFileName());
					Path file = Files.createTempFile( folder,filename+"-" , "." + extension);
					System.out.println("folder: "+folder.toString()+"\n"+"filename: "+filename+"\n"
					 	+ "file: "+file.toString());
					
					try (InputStream input =new_pic.getInputstream()) {
					    Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
					    profile_url=file.toString().split("/")[file.toString().split("/").length-1];
					}

					System.out.println("Uploaded picture successfully saved in " + profile_url);
					u.setProfilePic(profile_url);
					
				}
				else {
					FacesMessage message = new FacesMessage("Please insert an image file (.jpg, .jpe, .gif, .png)");
			        FacesContext context = FacesContext.getCurrentInstance();
			        context.addMessage(button3_UI.getClientId(context), message);
			        return "";
				}
			}
				
			catch(Exception e) {
				System.out.println("2 profile pic was not given--"+e);
				FacesMessage message = new FacesMessage("Picture was not given");
		        FacesContext context = FacesContext.getCurrentInstance();
		        context.addMessage(button3_UI.getClientId(context), message);
		        return "";
			}

			
		}
		query.commit();
		//apothikeuei kai dhmiourgei mhnuma
		
		String message="Field \""+editing_field+"\" has changed Successfully";
		
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
		FacesContext context=FacesContext.getCurrentInstance();
		context.addMessage(null,facesMessage);
		context.getExternalContext().getFlash().setKeepMessages(true);
		System.out.println("Succes message");
		return "profile.xhtml?faces-redirect=true";
	}
	
	
	public UIComponent getButton1_UI() {
		return button1_UI;
	}

	public void setButton1_UI(UIComponent button_UI) {
		this.button1_UI = button_UI;
	}

	public UIComponent getButton2_UI() {
		return button2_UI;
	}

	public void setButton2_UI(UIComponent button2_UI) {
		this.button2_UI = button2_UI;
	}

	public UIComponent getButton3_UI() {
		return button3_UI;
	}

	public void setButton3_UI(UIComponent button3_UI) {
		this.button3_UI = button3_UI;
	}

	public String getNew_value() {
		return new_value;
	}

	public void setNew_value(String new_value) {
		this.new_value = new_value;
	}

	public String getOld_value() {
		return old_value;
	}

	public void setOld_value(String old_value) {
		this.old_value = old_value;
	}

	public String getEditing_field() {
		return editing_field;
	}

	public void setEditing_field(String editing_field) {
		this.editing_field = editing_field;
	}	

	public User getU() {
		return u;
	}

	public void setU(User u) {
		this.u = u;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
	public String getSurname() {
		return surname;
	}

	public List<UserRoomInteraction> getUri_list() {
		return uri_list;
	}

	public void setUri_list(List<UserRoomInteraction> uri_list) {
		this.uri_list = uri_list;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getProfile_pic() {
		return profile_pic;
	}

	public void setProfile_pic(String profile_pic) {
		this.profile_pic = profile_pic;
	}
	
	public String getC_pwd() {
		return c_pwd;
	}

	public void setC_pwd(String c_pwd) {
		this.c_pwd = c_pwd;
	}

	public UploadedFile getNew_pic() {
		return new_pic;
	}

	public void setNew_pic(UploadedFile new_pic) {
		this.new_pic = new_pic;
	}
}