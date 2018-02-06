package model;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


@ManagedBean(name="admin")
public class AdminBean {
	
	private final DataQuery query=new DataQuery();
	List<User> results;	
	private HtmlDataTable datatable;
	private boolean datatable_renderer;
	
	private String username;
	private String name;
	private String surname;
	private String phone;
	private String email;
	private String role;
	private String profile_pic;
	private String a_approve;
	
	private UIComponent messages_UI;
	private UIComponent messages2_UI;
	private UIComponent messages3_UI;
	
	private boolean render_admin=false;
	private boolean render_datatable=true;
	
	@PostConstruct
	public void init() {
		results= query.find_all();
		FacesContext context =FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		System.out.println("ADMIN "+query.findUser(session.getAttribute("username").toString()).getRole());
		if(session.getAttribute("username")!=null && query.findUser(session.getAttribute("username").toString()).getRole().equals("A")) {
			System.out.println("ADMIN");		
			render_admin=true;
		}
		else	
			render_admin=false;
		if(session.getAttribute("username_2")!=null) {	
			render_datatable=false;
			username=(String) session.getAttribute("username_2");
			User u=query.findUser(username);

			name=u.getName();
			surname=u.getSurname();
			email=u.getEmail();
			phone=u.getPhone();
			role=u.getRole();
			profile_pic=u.getProfilePic();
			a_approve=u.getAApprove();
			if(a_approve==null) {
				if(role.equals("H"))
					a_approve="Not Approved";
				else
					a_approve="-";
				
			}
			
		}
		else
			render_datatable=true;
		
	}
	
	
	//allazei thn selida kai thn kanei na fainetai san profile
	public String view_profile() {
		User u=(User) datatable.getRowData();
		
		FacesContext context =FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		session.setAttribute("username_2",u.getUsername());
		
		init();
		setDatatable_renderer(false);

		return "";
	}
	
	
	//paragei ta arxeia xml me tous users sto fakelo resources/xml
	public String produce_userXml() throws IOException {
		
		File file = new File("/home/giwrgosmandi/eclipse-workspace/Airbnb/WebContent/resources/xml/User.xml"); //(?)
		
		if(file.delete())
			System.out.println(file.getName() + " is deleted!");
		
		List<User> users=query.find_all();
		 
		 Element users_el = new Element("users");
		 Document doc = new Document(users_el);
		 doc.setRootElement(users_el);
		 
		 for(User u : users) {
				Element user = new Element("user");
				user.setAttribute(new Attribute("user_id", Integer.toString(u.getUserId())));
				user.addContent(new Element("username").setText(u.getUsername()));
				user.addContent(new Element("name").setText(u.getName()));
				user.addContent(new Element("surname").setText(u.getSurname()));
				user.addContent(new Element("email").setText(u.getEmail()));
				user.addContent(new Element("phone").setText(u.getPhone()));
				user.addContent(new Element("role").setText(u.getRole()));
				user.addContent(new Element("profile_picture").setText(u.getProfilePic()));
				user.addContent(new Element("password").setText(u.getPassword()));
				
				List<Room> room_list=u.getRooms();
				for(Room r :room_list) {
					Element room = new Element("room");
					user.addContent(room);
					room.setAttribute(new Attribute("room_id", Integer.toString(r.getRoomId())));
					room.addContent(new Element("address").setText(r.getAddress()));
					room.addContent(new Element("area").setText(Integer.toString(r.getArea())));
					room.addContent(new Element("location").setText(r.getLocation()));
					room.addContent(new Element("cost_per_person").setText(Integer.toString(r.getCpp())));
					room.addContent(new Element("date").setText(r.getDate()));
					room.addContent(new Element("description").setText(r.getDescription()));
					room.addContent(new Element("elevator").setText(r.getElevator()));
					room.addContent(new Element("events").setText(r.getEvents()));
					room.addContent(new Element("heating").setText(r.getHeating()));
					room.addContent(new Element("info").setText(r.getInfo()));
					room.addContent(new Element("kitchen").setText(r.getKitchen()));
					room.addContent(new Element("lat").setText(String.valueOf(r.getLat())));
					room.addContent(new Element("ling").setText(String.valueOf(r.getLing())));
					room.addContent(new Element("livingroom").setText(r.getLivingroom()));
					room.addContent(new Element("minimum_charge").setText(Integer.toString(r.getMCharge())));
					room.addContent(new Element("minimum_days").setText(Integer.toString(r.getMDays())));
					room.addContent(new Element("main_picture").setText(r.getMainPic()));
					room.addContent(new Element("no_baths").setText(Integer.toString(r.getNoBaths())));
					room.addContent(new Element("no_beds").setText(Integer.toString(r.getNoBeds())));
					room.addContent(new Element("maximum_people").setText(Integer.toString(r.getNoPeople())));
					room.addContent(new Element("no_rooms").setText(Integer.toString(r.getNoRooms())));
					room.addContent(new Element("parking").setText(r.getParking()));
					room.addContent(new Element("pets").setText(r.getPets()));
					room.addContent(new Element("pictures_folder").setText(r.getPicturesFolder()));
					room.addContent(new Element("refrigerator").setText(r.getRefrigerator()));
					room.addContent(new Element("room_name").setText(r.getRoomName()));
					room.addContent(new Element("smoking").setText(r.getSmoking()));
					room.addContent(new Element("tv").setText(r.getTv()));
					room.addContent(new Element("type").setText(r.getType()));
					room.addContent(new Element("wifi").setText(r.getWifi()));
					
				}
				
				doc.getRootElement().addContent(user);
		
		 }
		 // new XMLOutputter().output(doc, System.out);
		XMLOutputter xmlOutput = new XMLOutputter();

		// display nice nice
		xmlOutput.setFormat(Format.getPrettyFormat());
		xmlOutput.output(doc, new FileWriter("/home/giwrgosmandi/eclipse-workspace/Airbnb/WebContent/resources/xml/User.xml")); //(?)
		
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage facesMessage = new FacesMessage("User.xml was created Successfully");
	    context.addMessage(messages_UI.getClientId(context), facesMessage);
		return "";
	}
	
	//paragei ta room xml
	public String produce_roomXml() throws IOException {
		File file = new File("/home/giwrgosmandi/eclipse-workspace/Airbnb/WebContent/resources/xml/Room.xml"); //(?)
		
		if(file.delete())
			System.out.println(file.getName() + " is deleted!");

		List<Room> rooms=query.find_all_rooms();
		
		Element rooms_el = new Element("rooms");
		Document doc = new Document(rooms_el);
		doc.setRootElement(rooms_el);
		
		 for(Room r : rooms) {
				Element room = new Element("room");
				room.setAttribute(new Attribute("room_id", Integer.toString(r.getRoomId())));
				room.addContent(new Element("address").setText(r.getAddress()));
				room.addContent(new Element("area").setText(Integer.toString(r.getArea())));
				room.addContent(new Element("location").setText(r.getLocation()));
				room.addContent(new Element("cost_per_person").setText(Integer.toString(r.getCpp())));
				room.addContent(new Element("date").setText(r.getDate()));
				room.addContent(new Element("description").setText(r.getDescription()));
				room.addContent(new Element("elevator").setText(r.getElevator()));
				room.addContent(new Element("events").setText(r.getEvents()));
				room.addContent(new Element("heating").setText(r.getHeating()));
				room.addContent(new Element("info").setText(r.getInfo()));
				room.addContent(new Element("kitchen").setText(r.getKitchen()));
				room.addContent(new Element("lat").setText(String.valueOf(r.getLat())));
				room.addContent(new Element("ling").setText(String.valueOf(r.getLing())));
				room.addContent(new Element("livingroom").setText(r.getLivingroom()));
				room.addContent(new Element("minimum_charge").setText(Integer.toString(r.getMCharge())));
				room.addContent(new Element("minimum_days").setText(Integer.toString(r.getMDays())));
				room.addContent(new Element("main_picture").setText(r.getMainPic()));
				room.addContent(new Element("no_baths").setText(Integer.toString(r.getNoBaths())));
				room.addContent(new Element("no_beds").setText(Integer.toString(r.getNoBeds())));
				room.addContent(new Element("maximum_people").setText(Integer.toString(r.getNoPeople())));
				room.addContent(new Element("no_rooms").setText(Integer.toString(r.getNoRooms())));
				room.addContent(new Element("parking").setText(r.getParking()));
				room.addContent(new Element("pets").setText(r.getPets()));
				room.addContent(new Element("pictures_folder").setText(r.getPicturesFolder()));
				room.addContent(new Element("refrigerator").setText(r.getRefrigerator()));
				room.addContent(new Element("room_name").setText(r.getRoomName()));
				room.addContent(new Element("smoking").setText(r.getSmoking()));
				room.addContent(new Element("tv").setText(r.getTv()));
				room.addContent(new Element("type").setText(r.getType()));
				room.addContent(new Element("wifi").setText(r.getWifi()));
		 
				
				User u=r.getUser();
				Element user = new Element("user");
				room.addContent(user);
				
				
				user.setAttribute(new Attribute("user_id", Integer.toString(u.getUserId())));
				user.addContent(new Element("username").setText(u.getUsername()));
				user.addContent(new Element("name").setText(u.getName()));
				user.addContent(new Element("surname").setText(u.getSurname()));
				user.addContent(new Element("email").setText(u.getEmail()));
				user.addContent(new Element("phone").setText(u.getPhone()));
				user.addContent(new Element("role").setText(u.getRole()));
				user.addContent(new Element("profile_picture").setText(u.getProfilePic()));
				user.addContent(new Element("password").setText(u.getPassword()));
				
				doc.getRootElement().addContent(room);
		 
		 }
		 // new XMLOutputter().output(doc, System.out);
		XMLOutputter xmlOutput = new XMLOutputter();

		// display nice nice
		xmlOutput.setFormat(Format.getPrettyFormat());
		xmlOutput.output(doc, new FileWriter("/home/giwrgosmandi/eclipse-workspace/Airbnb/WebContent/resources/xml/Room.xml")); //(?)
			
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage facesMessage = new FacesMessage("Room.xml was created Successfully");
		context.addMessage(messages2_UI.getClientId(context), facesMessage);
	
		return "";
	}
	
	
	
	//to URI.xml periexei tis ajiologhseis tis bathmologies kai ta booking
	public String produce_URIXml() throws IOException {
		File file = new File("/home/giwrgosmandi/eclipse-workspace/Airbnb/WebContent/resources/xml/URI.xml"); //(?)
		
		if(file.delete())
			System.out.println(file.getName() + " is deleted!");

		List<UserRoomInteraction> uri_list=query.find_allURI();
		
		Element uris_el = new Element("UserRoomInteractions");
		Document doc = new Document(uris_el);
		doc.setRootElement(uris_el);
		for(UserRoomInteraction uri : uri_list) {
			Element uri_el = new Element("UserRoomInteractions");
			uri_el.setAttribute(new Attribute("interaction_id", Integer.toString(uri.getInteractionId())));
			uri_el.addContent(new Element("booked_date").setText(uri.getBookedDate()));
			uri_el.addContent(new Element("comment").setText(uri.getComment()));
			uri_el.addContent(new Element("comment_date").setText(uri.getCommentDate()));
			uri_el.addContent(new Element("rating").setText(Integer.toString(uri.getRating())));
			
			User u=uri.getUser();
			Element user = new Element("user");
			uri_el.addContent(user);
	
			user.setAttribute(new Attribute("user_id", Integer.toString(u.getUserId())));
			user.addContent(new Element("username").setText(u.getUsername()));
			user.addContent(new Element("name").setText(u.getName()));
			user.addContent(new Element("surname").setText(u.getSurname()));
			user.addContent(new Element("email").setText(u.getEmail()));
			user.addContent(new Element("phone").setText(u.getPhone()));
			user.addContent(new Element("role").setText(u.getRole()));
			user.addContent(new Element("profile_picture").setText(u.getProfilePic()));
			user.addContent(new Element("password").setText(u.getPassword()));
			
			Room r=uri.getRoom();
			Element room = new Element("room");
			uri_el.addContent(room);
			
			room.setAttribute(new Attribute("room_id", Integer.toString(r.getRoomId())));
			room.addContent(new Element("address").setText(r.getAddress()));
			room.addContent(new Element("area").setText(Integer.toString(r.getArea())));
			room.addContent(new Element("location").setText(r.getLocation()));
			room.addContent(new Element("cost_per_person").setText(Integer.toString(r.getCpp())));
			room.addContent(new Element("date").setText(r.getDate()));
			room.addContent(new Element("description").setText(r.getDescription()));
			room.addContent(new Element("elevator").setText(r.getElevator()));
			room.addContent(new Element("events").setText(r.getEvents()));
			room.addContent(new Element("heating").setText(r.getHeating()));
			room.addContent(new Element("info").setText(r.getInfo()));
			room.addContent(new Element("kitchen").setText(r.getKitchen()));
			room.addContent(new Element("lat").setText(String.valueOf(r.getLat())));
			room.addContent(new Element("ling").setText(String.valueOf(r.getLing())));
			room.addContent(new Element("livingroom").setText(r.getLivingroom()));
			room.addContent(new Element("minimum_charge").setText(Integer.toString(r.getMCharge())));
			room.addContent(new Element("minimum_days").setText(Integer.toString(r.getMDays())));
			room.addContent(new Element("main_picture").setText(r.getMainPic()));
			room.addContent(new Element("no_baths").setText(Integer.toString(r.getNoBaths())));
			room.addContent(new Element("no_beds").setText(Integer.toString(r.getNoBeds())));
			room.addContent(new Element("maximum_people").setText(Integer.toString(r.getNoPeople())));
			room.addContent(new Element("no_rooms").setText(Integer.toString(r.getNoRooms())));
			room.addContent(new Element("parking").setText(r.getParking()));
			room.addContent(new Element("pets").setText(r.getPets()));
			room.addContent(new Element("pictures_folder").setText(r.getPicturesFolder()));
			room.addContent(new Element("refrigerator").setText(r.getRefrigerator()));
			room.addContent(new Element("room_name").setText(r.getRoomName()));
			room.addContent(new Element("smoking").setText(r.getSmoking()));
			room.addContent(new Element("tv").setText(r.getTv()));
			room.addContent(new Element("type").setText(r.getType()));
			room.addContent(new Element("wifi").setText(r.getWifi()));		
			
			doc.getRootElement().addContent(uri_el);
			 
		 }
		 // new XMLOutputter().output(doc, System.out);
		XMLOutputter xmlOutput = new XMLOutputter();

		// display nice nice
		xmlOutput.setFormat(Format.getPrettyFormat());
		xmlOutput.output(doc, new FileWriter("/home/giwrgosmandi/eclipse-workspace/Airbnb/WebContent/resources/xml/URI.xml")); //(?)
			
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage facesMessage = new FacesMessage("URI.xml was created Successfully");
		context.addMessage(messages3_UI.getClientId(context), facesMessage);
		
		return "";
	}
	
	
	//kanei disable to button
	public boolean enable_button() {
		if(role.equals("H"))
			if(a_approve.equals("Not Approved"))
				return true;
		return false;
	}
	
	//kanei kapoion host dekto
	public String approve() {
		User u=query.findUser(username);
		u.setAApprove("Approved");
		query.commit();
		
		return "admin_page.xhtml?faces-redirect=true";
	}
	
	public boolean display_panel() {
		return datatable_renderer;
	}

	
	public String getA_approve() {
		return a_approve;
	}


	public void setA_approve(String a_approve) {
		this.a_approve = a_approve;
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


	public boolean isRender_admin() {
		return render_admin;
	}


	public void setRender_admin(boolean render_admin) {
		this.render_admin = render_admin;
	}


	public boolean isRender_datatable() {
		return render_datatable;
	}


	public void setRender_datatable(boolean render_datatable) {
		this.render_datatable = render_datatable;
	}


	public String getProfile_pic() {
		return profile_pic;
	}


	public UIComponent getMessages_UI() {
		return messages_UI;
	}


	public void setMessages_UI(UIComponent messages_UI) {
		this.messages_UI = messages_UI;
	}
	
	public UIComponent getMessages2_UI() {
		return messages2_UI;
	}


	public void setMessages2_UI(UIComponent messages2_UI) {
		this.messages2_UI = messages2_UI;
	}
	
	public UIComponent getMessages3_UI() {
		return messages3_UI;
	}


	public void setMessages3_UI(UIComponent messages3_UI) {
		this.messages3_UI = messages3_UI;
	}


	public void setProfile_pic(String profile_pic) {
		this.profile_pic = profile_pic;
	}

	public HtmlDataTable getDatatable() {
		return datatable;
	}


	public void setDatatable(HtmlDataTable datatable) {
		this.datatable = datatable;
	}
	public List<User> getResults() {
		return results;
	}


	public void setResults(List<User> results) {
		this.results = results;
	}

	public boolean isDatatable_renderer() {
		return datatable_renderer;
	}


	public void setDatatable_renderer(boolean datatable_renderer) {
		this.datatable_renderer = datatable_renderer;
	}
	
	
	
}
