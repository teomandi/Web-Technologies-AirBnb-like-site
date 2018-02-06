package model;

import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.model.UploadedFile;
//import org.primefaces.model.UploadedFile;

@ManagedBean(name="register")
public class RegisterBean {
	
	private String username;
	private String password;
	private String c_password;
	private String name;
	private String surname;
	private String phone;
	private String email;
	private String role;
	private UploadedFile profile_pic;
	
	private UIComponent username_UI;
	private UIComponent password_UI;
	private UIComponent c_password_UI;
	private UIComponent name_UI;
	private UIComponent surname_UI;
	private UIComponent phone_UI;
	private UIComponent email_UI;
	private UIComponent role_UI;
	private UIComponent profile_pic_UI;
	
	private final DataQuery query=new DataQuery();
	
	
	

	
//--------------------------------------------------------------------------------------------------------------------------------
	
	/*kanei tous elegxous kai mono an to 
	 * return_val einai "success" tote exei dwsei swsta stoixeia..
	 * alliws dhmiourgei katallhlo mhnuma error
	 * */
	public String registerControl() {
		String profile_url=null;
		String return_val="";
		if(password != null && !password.isEmpty() ) {
			if(c_password!=null && password.equals(c_password)) {
				if(username != null && !username.isEmpty() && !query.Username_exist(username))
					if(name != null && !name.isEmpty()) {
						if(surname != null && !surname.isEmpty()) {
							if(email != null && !email.isEmpty()) {
								if(phone != null && !phone.isEmpty()) {
									if(role!="") {
										
										try{
											if(profile_pic.getSize()>0) {
												String[] parts=profile_pic.getFileName().split("\\.");					
												if(parts[parts.length-1].equals("gif") || 
														parts[parts.length-1].equals("png") ||
														parts[parts.length-1].equals("jpe") ||
														parts[parts.length-1].equals("jpg") ) {									
													Path folder = Paths.get("/home/giwrgosmandi/eclipse-workspace/Airbnb/WebContent/resources/user_images");
													//(?)
													String filename = FilenameUtils.getBaseName(profile_pic.getFileName()); 
													String extension = FilenameUtils.getExtension(profile_pic.getFileName());
													Path file = Files.createTempFile( folder,filename+"-" , "." + extension);
													System.out.println("folder: "+folder.toString()+"\n"+"filename: "+filename+"\n"
															+ "file: "+file.toString());
												
													try (InputStream input =profile_pic.getInputstream()) {
														Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
														profile_url=file.toString().split("/")[file.toString().split("/").length-1];
													}

													System.out.println("Uploaded picture successfully saved in " + profile_url);
													return_val="success";
												
												}
												else {
													return_val="Please insert an image file (.jpg, .jpe, .gif, .png)";
													FacesMessage message = new FacesMessage(return_val);
													FacesContext context = FacesContext.getCurrentInstance();
													context.addMessage(profile_pic_UI.getClientId(context), message);
												}
											}
											else {
												
												profile_url="default.jpg";
												return_val="success";
											}
										}
											
										catch(Exception e) {
											System.out.println("2 profile pic was not given--"+e);
											profile_url="default.jpg";
											return_val="success";
											
										}
										
									}
									else {
										return_val="Is required to choose a Role";
										FacesMessage message = new FacesMessage(return_val);
								        FacesContext context = FacesContext.getCurrentInstance();
								        context.addMessage(role_UI.getClientId(context), message);
									}
								}
								else {
									return_val="Phone Number is required";
									FacesMessage message = new FacesMessage(return_val);
							        FacesContext context = FacesContext.getCurrentInstance();
							        context.addMessage(phone_UI.getClientId(context), message);
								}
							}
							else {
								return_val="Email is required";
								FacesMessage message = new FacesMessage(return_val);
						        FacesContext context = FacesContext.getCurrentInstance();
						        context.addMessage(email_UI.getClientId(context), message);
							}
						}
						else {
							return_val="Surname is required";
							FacesMessage message = new FacesMessage(return_val);
					        FacesContext context = FacesContext.getCurrentInstance();
					        context.addMessage(surname_UI.getClientId(context), message);
						}
					}
					else {
						return_val="Name is required";
						FacesMessage message = new FacesMessage(return_val);
		        		FacesContext context = FacesContext.getCurrentInstance();
		        		context.addMessage(name_UI.getClientId(context), message);
					}
				else {
					return_val="Username exists";
					FacesMessage message = new FacesMessage(return_val);
			        FacesContext context = FacesContext.getCurrentInstance();
			        context.addMessage(username_UI.getClientId(context), message);
				}
			}
			else {
				return_val="Password didn't match";
				FacesMessage message = new FacesMessage(return_val);
		        FacesContext context = FacesContext.getCurrentInstance();
		        context.addMessage(c_password_UI.getClientId(context), message);
			}
		}
		else { 
			return_val="Password is required";
			FacesMessage message = new FacesMessage(return_val);
	        FacesContext context = FacesContext.getCurrentInstance();
	        context.addMessage(password_UI.getClientId(context), message);
		}
		
		if(return_val.equals("success")) {
			//dhmiourgei ton user ..to arxikopoei kai ton vazei sth vash
			//kanei kai login
			
			User u=new User();
			u.setUsername(username);
			String hashtext=null;
			try {
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
			System.out.println(hashtext);
			u.setPassword(hashtext);
			u.setEmail(email);
			u.setName(name);
			u.setSurname(surname);
			u.setPhone(phone);
			u.setRole(role);
			u.setProfilePic(profile_url);
			if(role.equals("H"))
				u.setAApprove("Not Approved");
			else
				u.setAApprove("-");
			
			//apothikeush
			query.persist(u);
			
			FacesContext context=FacesContext.getCurrentInstance();
			HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
			session.setAttribute("username", username);
			
			session.setAttribute("user_id",u.getUserId());
			
			String message="Registration completed succesfully";
			FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
			
			context.addMessage(null,facesMessage);
			context.getExternalContext().getFlash().setKeepMessages(true);
			session.setAttribute("special_button","");
			if(role.equals("H")) {
				//dhmiourgei mhnyma pws anamenetai egkrish apton admin
				String message_2="Admin will approve your registration as a host, till then you can use the site as a Typical User ";
				FacesMessage facesMessage_2 = new FacesMessage(FacesMessage.SEVERITY_INFO, message_2, null);
				FacesContext context_2=FacesContext.getCurrentInstance();
				context_2.addMessage("msg",facesMessage_2);
				context_2.getExternalContext().getFlash().setKeepMessages(true);
				
				session.setAttribute("special_button","Host Page");
				
			}
		
			System.out.println("Sussessfull");
			
			
			return "welcome.xhtml?faces-redirect=true";
		}
		return "";
    }
	

	
	public UIComponent getUsername_UI() {
		return username_UI;
	}

	public void setUsername_UI(UIComponent username_UI) {
		this.username_UI = username_UI;
	}

	public UIComponent getPassword_UI() {
		return password_UI;
	}

	public void setPassword_UI(UIComponent password_UI) {
		this.password_UI = password_UI;
	}

	public UIComponent getC_password_UI() {
		return c_password_UI;
	}

	public void setC_password_UI(UIComponent c_password_UI) {
		this.c_password_UI = c_password_UI;
	}

	public UIComponent getName_UI() {
		return name_UI;
	}

	public void setName_UI(UIComponent name_UI) {
		this.name_UI = name_UI;
	}

	public UIComponent getSurname_UI() {
		return surname_UI;
	}

	public void setSurname_UI(UIComponent surname_UI) {
		this.surname_UI = surname_UI;
	}

	public UIComponent getPhone_UI() {
		return phone_UI;
	}

	public void setPhone_UI(UIComponent phone_UI) {
		this.phone_UI = phone_UI;
	}

	public UIComponent getEmail_UI() {
		return email_UI;
	}

	public void setEmail_UI(UIComponent email_UI) {
		this.email_UI = email_UI;
	}

	public UIComponent getRole_UI() {
		return role_UI;
	}

	public void setRole_UI(UIComponent role_UI) {
		this.role_UI = role_UI;
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

	public UploadedFile getProfile_pic() {
		return profile_pic;
	}

	public void setProfile_pic(UploadedFile profile_pic) {
		this.profile_pic = profile_pic;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getC_password() {
		return c_password;
	}

	public void setC_password(String c_password) {
		this.c_password = c_password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public UIComponent getProfile_pic_UI() {
		return profile_pic_UI;
	}

	public void setProfile_pic_UI(UIComponent profile_pic_UI) {
		this.profile_pic_UI = profile_pic_UI;
	}
		
}
