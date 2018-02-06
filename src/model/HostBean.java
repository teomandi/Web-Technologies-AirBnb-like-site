package model;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.annotation.PostConstruct;




import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.map.GeocodeEvent;
import org.primefaces.event.map.ReverseGeocodeEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.GeocodeResult;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;


@ManagedBean(name="host")

public class HostBean {
	
	private MapModel geoModel;
    private MapModel revGeoModel;
    private String centerGeoMap = "37.9838096 ,23.727538800000048";
    private String centerRevGeoMap = "37.9838096, 23.727538800000048";
    private double lat=0;
    private double ling=0;
    
    private boolean render_page;
    
    private String username;
    private String user_id;
    
    private String info;
    private String address;
    private String type;
    private String description;
    private String location;
    private String name;
  
    
    private Integer no_people;
    private Integer m_charge;
    private Integer cpp;
    private Integer beds;
    private Integer rooms;
    private Integer area;
    private Integer baths;
    private Integer s_year;
    private Integer s_month;
    private Integer s_day;
    private Integer f_year;
    private Integer f_month;
    private Integer f_day;
    private Integer m_day;
    

	private String smoking_rule;
    private String pet_rule;
    private String event_rule;
    private String living_r;
    private String wifi;
    private String refrigerator;
    private String heating;
    private String kitchen;
    private String tv;
    private String parking;
    private String elevator;
    
    private UIComponent map_UI;
    private UIComponent info_UI;
    private UIComponent no_people_UI;
    private UIComponent m_charge_UI;
    private UIComponent cpp_UI;
    private UIComponent smoking_rule_UI;
    private UIComponent pet_rule_UI;
    private UIComponent event_rule_UI;
    private UIComponent type_UI;
    private UIComponent description_UI;
    private UIComponent about_ap_UI;
    private UIComponent beds_UI;
    private UIComponent main_pic_UI;
    private UIComponent f_date_UI;
    private UIComponent s_date_UI;
    private UIComponent files_upload_UI;
    private UIComponent location_UI;
	private UIComponent name_UI;
    private UIComponent m_day_UI;
    
    private UploadedFile main_pic;
    
    private List<Room> apartments;
    private boolean show_apartments=false;
    //(?)
    private String folder_path="/home/giwrgosmandi/eclipse-workspace/Airbnb/WebContent/resources/apartments/";
    


	private final DataQuery query=new DataQuery();
    
    
    @PostConstruct
    public void init() {  
    	
    	 HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
 				.getExternalContext().getSession(true);
 		setUsername(session.getAttribute("username").toString());
 		setUser_id(session.getAttribute("user_id").toString());
    	
 		//dhmiourgei fakelo gia ton xrhsth opou tha apothikeuwntai oi eikones
 		folder_path=folder_path+user_id;
    	File folder=new File(folder_path);
    	 if (folder.exists() && folder.isDirectory()) {
    		System.out.println("folder Exists");
    		 
    		//kanei init ta dwmatia tou
 			apartments= query.find_user_apartments(username);
 			if(apartments!=null) {
 				show_apartments=true;
 				for(Room r: apartments)
 					System.out.println("--->>"+r.getAddress()+" "+r.getMainPic());	
 			}
    	 }
 			
    	 else {
    		 folder.mkdir();
    		 show_apartments=false;
    		 System.out.println("folder didnt Exists");
    	 }
    	 
    	
     	//arxikopoiei to geomode
        geoModel = new DefaultMapModel();
        revGeoModel = new DefaultMapModel();

        //kanei katallhlo render
		User u=query.findUser(username);
		if(u.getAApprove().equals("Approved")) {
			render_page=true;
		}
		else
			render_page=false;
		
		
    
    }
    
     //gia to geocode!
    public void onGeocode(GeocodeEvent event) {
        List<GeocodeResult> results = event.getResults();
         
        if (results != null && !results.isEmpty()) {
            LatLng center = results.get(0).getLatLng();
            centerGeoMap = center.getLat() + "," + center.getLng();
            lat=center.getLat();
            ling=center.getLng();
            System.out.println("-->"+lat+" "+ling+" " +address);
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
     				.getExternalContext().getSession(true);
            //ta vazei se session gia na ta parei otan ta kanei create
     		session.setAttribute("lat", lat);
     		session.setAttribute("ling", ling);
     	
            for (int i = 0; i < results.size(); i++) {
                GeocodeResult result = results.get(i);
                geoModel.addOverlay(new Marker(result.getLatLng(), result.getAddress()));
            }
        }
    }
     
    public void onReverseGeocode(ReverseGeocodeEvent event) {
        List<String> addresses = event.getAddresses();
        LatLng coord = event.getLatlng();
         
        if (addresses != null && !addresses.isEmpty()) {
            centerRevGeoMap = coord.getLat() + "," + coord.getLng();
            revGeoModel.addOverlay(new Marker(coord, addresses.get(0)));
        }
    }
    
    
    //ftiaxnei to room
    public String save() {
    	
    	System.out.println("Validate");
    	
    	FacesContext context = FacesContext.getCurrentInstance();
    	HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
 				.getExternalContext().getSession(true);
    	
    	//elegxei an exei sumblhrwsei swsta ola ta pedia
    	//mono an to message einai keno exei epituxei
    	String message="";
    	if(session.getAttribute("lat")==null) {
    		
    		message="You have to insert an address.";
			FacesMessage facesMessage = new FacesMessage(message);
	        context.addMessage(map_UI.getClientId(context), facesMessage);
		}
    	
    	if(address.equals("")) {
    		message="You have to insert an address.";
			FacesMessage facesMessage13 = new FacesMessage(message);
	        context.addMessage(map_UI.getClientId(context), facesMessage13);
    		
    	}	
    	if(name.equals("")) {
    		message="You have to insert a name for your apartment.";
			FacesMessage facesMessage17 = new FacesMessage(message);
	        context.addMessage(name_UI.getClientId(context), facesMessage17);
    		
    	}	
    	if(location.equals("")) {
    		message="You have to insert the location of your apartment.";
			FacesMessage facesMessage16 = new FacesMessage(message);
	        context.addMessage(location_UI.getClientId(context), facesMessage16);
    		
    	}	
    	if(info.equals("")) {
    		
    		message="Please give instruction about how people could reach this place.";
			FacesMessage facesMessage_2 = new FacesMessage(message);
	        context.addMessage(info_UI.getClientId(context), facesMessage_2);
	        
    	}
    	if(no_people<=0) {
    		message="Please insert number bigger than '0'.";
			FacesMessage facesMessage_3 = new FacesMessage(message);
	        context.addMessage(no_people_UI.getClientId(context), facesMessage_3);
    		
    	}
    	
    	if(m_day<=0) {
    		message="Please insert number bigger than '0'.";
			FacesMessage facesMessage_18 = new FacesMessage(message);
	        context.addMessage(m_day_UI.getClientId(context), facesMessage_18);
    		
    	}
    	
    	if(m_charge<=0) {
    		message="Please insert number bigger than '0'.";
			FacesMessage facesMessage_4 = new FacesMessage(message);
	        context.addMessage(m_charge_UI.getClientId(context), facesMessage_4);
    		
    	}
    	if(cpp<=0) {
    		 message="Please insert number bigger than '0'.";
			FacesMessage facesMessage_5 = new FacesMessage(message);
	        context.addMessage(cpp_UI.getClientId(context), facesMessage_5);
    		
    	}
    	if(type=="") {
    		message="Please choose the type of the apartment.";
			FacesMessage facesMessage_6 = new FacesMessage(message);
	        context.addMessage(type_UI.getClientId(context), facesMessage_6);
    		
    	}
    	if(description=="") {
    		message="Please give some description about the apartment the apartment.";
			FacesMessage facesMessage_7 = new FacesMessage(message);
	        context.addMessage(description_UI.getClientId(context), facesMessage_7);
    		
    	}
    	if (area<=0 || beds<=0 || baths<=0 || rooms<=0) {
    		message="Please insert number bigger than '0'.";
			FacesMessage facesMessage_8 = new FacesMessage(message);
	        context.addMessage(beds_UI.getClientId(context), facesMessage_8);
    		
    	}
    	if(main_pic.getSize()==0) {
    		message="Please insert a picture of your apartment, to use it as the default picture.";
			FacesMessage facesMessage_8 = new FacesMessage(message);
	        context.addMessage(main_pic_UI.getClientId(context), facesMessage_8);
    	}
    	else {
    		//elegxei to arxei pou dexthke
    		String[] parts = main_pic.getFileName().split("\\.");
    		if(!parts[1].equals("gif") &&
    				!parts[1].equals("jpg") &&
    				!parts[1].equals("jpe") && 
    				!parts[1].equals("png")) {
    			message="Please insert an image file (.jpg, .jpe, .gif, .png)";
    			FacesMessage facesMessage_81 = new FacesMessage(message);
    	        context.addMessage(main_pic_UI.getClientId(context), facesMessage_81);
    		}
    	}
    	if(s_month<=0 || s_month>12) {
   		 	message="Wrong input for Date.";
			FacesMessage facesMessage_9 = new FacesMessage(message);
	        context.addMessage(s_date_UI.getClientId(context), facesMessage_9);
   		
    	}
    	else if(s_day<=0 || s_day>31 ) {
    		message="Wrong input for Date.";
			FacesMessage facesMessage_9 = new FacesMessage(message);
	        context.addMessage(s_date_UI.getClientId(context), facesMessage_9);
   		
    	}
    	if(f_month<=0 || f_month>12) {
    		message="Wrong input for Date.";
			FacesMessage facesMessage_11 = new FacesMessage(message);
	        context.addMessage(f_date_UI.getClientId(context), facesMessage_11);
   		
    	}
    	else if(f_day<=0 || f_day>31) {
    		message="Wrong input for Date.";
			FacesMessage facesMessage_12 = new FacesMessage(message);
	        context.addMessage(f_date_UI.getClientId(context), facesMessage_12);
   		
    	}
    	//vazei default times an den ta edwse o xrhsths
    	if(smoking_rule=="") 
    		smoking_rule="Not Allowed";
    	if(event_rule=="") 
    		event_rule="Not Allowed";
    	if(pet_rule=="") 
    		pet_rule="Not Allowed";
    	if(living_r=="") 
    		living_r="NO";
    	if(elevator=="") 
    		elevator="NO";
    	if(heating=="") 
    		heating="NO";
    	if(kitchen=="") 
    		kitchen="NO";
    	if(refrigerator=="") 
    		refrigerator="NO";
    	if(tv=="") 
    		tv="NO";
    	if(wifi=="") 
    		wifi="NO";
    	if(parking=="") 
    		parking="NO";
    	
    	if(message=="") {
    		//success!
    		System.out.println("save: Success!!!");
    		
    		//ftiaxnei ta date  se morfh 09/01
    		String s_month_str, s_day_str, f_month_str, f_day_str ,date;
    		if(s_month < 10) 
    			s_month_str="0"+s_month.toString();
    		else
    			s_month_str=s_month.toString();
    		
    		if(f_month < 10) 
    			f_month_str="0"+f_month.toString();
    		else
    			f_month_str=f_month.toString();
    		
    		if(s_day < 10) 
    			s_day_str="0"+s_day.toString();
    		else
    			s_day_str=s_day.toString();
    		
    		if(f_day < 10) 
    			f_day_str="0"+f_day.toString();
    		else
    			f_day_str=f_day.toString();
    		
    		date=s_day_str+"/"+s_month_str+"-"+f_day_str+"/"+f_month_str;
    		System.out.println("save Date-->"+date);
    		
    		
    		File folder=new File(folder_path);
    		int folder_name_int=1;
    		
    		//ftiaxnei katallhlo fakelo gia to apartment
    		for (@SuppressWarnings("unused") final File fileEntry : folder.listFiles()) 
    			folder_name_int++;
    		folder_name_int++;
    		folder_path=folder_path+"/"+folder_name_int;
    		File new_folder =new File(folder_path);
    		new_folder.mkdir();
    		
    		
    		folder_path=folder_path+"/"; //<<---to path tou fakelou
    		System.out.println("fp:-->"+folder_path+"--"+folder_name_int);
    		//dhmiourgia arxeiou mesto fakelo
    		String extension = FilenameUtils.getExtension(main_pic.getFileName());
    		String picture_path=folder_path+"main."+extension;
    		File picture=new File(picture_path);
    		System.out.println("save- picture absolute path "+ picture.getAbsolutePath()+" " +folder_path.length());
    	
    		try {
    			Path path=Paths.get(picture.getAbsolutePath());
    			InputStream input = main_pic.getInputstream();
    			
    			Files.copy(input, path, StandardCopyOption.REPLACE_EXISTING);
    			
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    			System.out.println("Failed to save uploaded File");
    		}
    		
    		picture_path=user_id+"/"+folder_name_int+"/main."+extension;
    		
    		//apothuikeush sth vash
    		Room r=new Room();
    		System.out.println("Save username: "+username+" " +elevator+" ");
    		r.setUser(query.findUser(username));
    		r.setUsername(username);
    		r.setAddress(address);
    		r.setArea(area);
    		r.setCpp(cpp);
    		r.setDate(date);
    		r.setDescription(description);
    		r.setElevator(elevator);
    		r.setEvents(event_rule);
    		r.setHeating(heating);
    		r.setInfo(info);
    		r.setKitchen(kitchen);
    		r.setLat(Double.parseDouble(session.getAttribute("lat").toString()));
    		r.setLing(Double.parseDouble(session.getAttribute("ling").toString()));
    		r.setLivingroom(living_r);
    		r.setMCharge(m_charge);
    		r.setNoBaths(baths);
    		r.setNoBeds(beds);
    		r.setNoPeople(no_people);
    		r.setNoRooms(rooms);
    		r.setParking(parking);
    		r.setPets(pet_rule);
    		r.setPicturesFolder(folder_path);
    		r.setRefrigerator(refrigerator);
    		r.setSmoking(smoking_rule);
    		r.setTv(tv);
    		r.setType(type);
    		r.setWifi(wifi);
    		r.setLocation(location.toUpperCase());
    		r.setWifi(wifi);
    		r.setMainPic(picture_path);
    		r.setRoomName(name);
    		r.setMDays(m_day);
    		r.setRating(1);
    		r.setNoRatings(1);
    		r.setRatingSum(1);
    		
    		query.persist(r);
    		
    		
     		session.setAttribute("pictures_folder",folder_path);
    		
    		return "host_page_2.xhtml?faces-redirect=true";
    	} 		
    	
    	
    	return "";
    }
    
    
    //apothikeuei tis fwtografies
    //oi fwtografies apothikeuontai sto picture folder
    public void upload_file(FileUploadEvent event) throws IOException {
    	
    	UploadedFile uploaded_file=event.getFile();
    	FacesContext context = FacesContext.getCurrentInstance();
    	
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
 				.getExternalContext().getSession(true);
 		String pictures_folder=session.getAttribute("pictures_folder").toString();
 		
 		Path folder = Paths.get(pictures_folder);
		
		String filename = FilenameUtils.getBaseName(uploaded_file.getFileName()); 
		String extension = FilenameUtils.getExtension(uploaded_file.getFileName());
		//tou dinei rando onoma
		Path file = Files.createTempFile( folder,filename+"-" , "." + extension);
		System.out.println("folder: "+folder.toString()+"\n"+"filename: "+filename+"\n"
		 	+ "file: "+file.toString());
		
		String profile_url;
		try (InputStream input =uploaded_file.getInputstream()) {
		    Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
		   profile_url=file.toString().split("/")[file.toString().split("/").length-1];
		}

		System.out.println("Uploaded picture successfully saved in " + profile_url);
		String message="Pictures were uploaded Successfully";
		FacesMessage facesMessage_11 = new FacesMessage(message);
        context.addMessage(files_upload_UI.getClientId(context), facesMessage_11);
		
		
		
      }
    
    
    
   

	public MapModel getGeoModel() {
        return geoModel;
    }
 
    public MapModel getRevGeoModel() {
        return revGeoModel;
    }
 
    public String getCenterGeoMap() {
        return centerGeoMap;
    }
 
    public String getCenterRevGeoMap() {
        return centerRevGeoMap;
    }

	public String getInfo() {
		return info;
	}

	public Integer getM_day() {
		return m_day;
	}


	public void setM_day(Integer m_day) {
		this.m_day = m_day;
	}


	public UIComponent getM_day_UI() {
		return m_day_UI;
	}


	public void setM_day_UI(UIComponent m_date_UI) {
		this.m_day_UI = m_date_UI;
	}


	public void setInfo(String info) {
		this.info = info;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isRender_page() {
		return render_page;
	}

	public void setRender_page(boolean render_page) {
		this.render_page = render_page;
	}

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getNo_people() {
		return no_people;
	}

	public void setNo_people(Integer no_people) {
		this.no_people = no_people;
	}

	public Integer getM_charge() {
		return m_charge;
	}

	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public void setM_charge(Integer m_charge) {
		this.m_charge = m_charge;
	}

	public Integer getCpp() {
		return cpp;
	}

	public void setCpp(Integer cpp) {
		this.cpp = cpp;
	}

	public UIComponent getLocation_UI() {
		return location_UI;
	}


	public UIComponent getName_UI() {
		return name_UI;
	}


	public void setName_UI(UIComponent name_UI) {
		this.name_UI = name_UI;
	}


	public void setLocation_UI(UIComponent location_UI) {
		this.location_UI = location_UI;
	}


	public UIComponent getMap_UI() {
		return map_UI;
	}

	public void setMap_UI(UIComponent map_UI) {
		this.map_UI = map_UI;
	}

	public UIComponent getInfo_UI() {
		return info_UI;
	}

	public void setInfo_UI(UIComponent info_UI) {
		this.info_UI = info_UI;
	}

	public UIComponent getNo_people_UI() {
		return no_people_UI;
	}

	public void setNo_people_UI(UIComponent no_people_UI) {
		this.no_people_UI = no_people_UI;
	}

	public UIComponent getM_charge_UI() {
		return m_charge_UI;
	}

	public void setM_charge_UI(UIComponent m_charge_UI) {
		this.m_charge_UI = m_charge_UI;
	}

	public UIComponent getCpp_UI() {
		return cpp_UI;
	}

	public void setCpp_UI(UIComponent cpp_UI) {
		this.cpp_UI = cpp_UI;
	}

	public UIComponent getSmoking_rule_UI() {
		return smoking_rule_UI;
	}

	public void setSmoking_rule_UI(UIComponent smoking_rule_UI) {
		this.smoking_rule_UI = smoking_rule_UI;
	}

	public UIComponent getPet_rule_UI() {
		return pet_rule_UI;
	}

	public void setPet_rule_UI(UIComponent pet_rule_UI) {
		this.pet_rule_UI = pet_rule_UI;
	}

	public UIComponent getEvent_rule_UI() {
		return event_rule_UI;
	}

	public void setEvent_rule_UI(UIComponent event_rule_UI) {
		this.event_rule_UI = event_rule_UI;
	}
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public UIComponent getType_UI() {
		return type_UI;
	}

	public void setType_UI(UIComponent type_UI) {
		this.type_UI = type_UI;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UIComponent getDescription_UI() {
		return description_UI;
	}

	public void setDescription_UI(UIComponent description_UI) {
		this.description_UI = description_UI;
	}

	public Integer getBeds() {
		return beds;
	}

	public void setBeds(Integer beds) {
		this.beds = beds;
	}

	public Integer getRooms() {
		return rooms;
	}

	public void setRooms(Integer rooms) {
		this.rooms = rooms;
	}
	
	 public Integer getArea() {
			return area;
		}

	 public void setArea(Integer area) {
			this.area = area;
		}

		public Integer getBaths() {
			return baths;
		}

		public void setBaths(Integer baths) {
			this.baths = baths;
		}

		public UIComponent getAbout_ap_UI() {
			return about_ap_UI;
		}

		public void setAbout_ap_UI(UIComponent about_ap_UI) {
			this.about_ap_UI = about_ap_UI;
		}

		public UIComponent getBeds_UI() {
			return beds_UI;
		}

		public void setBeds_UI(UIComponent beds_UI) {
			this.beds_UI = beds_UI;
		}

		public UploadedFile getMain_pic() {
			return main_pic;
		}

		public void setMain_pic(UploadedFile main_pic) {
			this.main_pic = main_pic;
		}

		public String getUser_id() {
			return user_id;
		}

		public void setUser_id(String user_id) {
			this.user_id = user_id;
		}

		public UIComponent getMain_pic_UI() {
			return main_pic_UI;
		}

		public void setMain_pic_UI(UIComponent main_pic_UI) {
			this.main_pic_UI = main_pic_UI;
		}

		
		public Integer getS_year() {
			return s_year;
		}

		public void setS_year(Integer s_year) {
			this.s_year = s_year;
		}

		public Integer getS_month() {
			return s_month;
		}

		public void setS_month(Integer s_month) {
			this.s_month = s_month;
		}

		public Integer getS_day() {
			return s_day;
		}

		public void setS_day(Integer s_day) {
			this.s_day = s_day;
		}

		public Integer getF_year() {
			return f_year;
		}

		public void setF_year(Integer f_year) {
			this.f_year = f_year;
		}
		 public String getLiving_r() {
				return living_r;
			}


			public void setLiving_r(String living_r) {
				this.living_r = living_r;
			}


			public String getWifi() {
				return wifi;
			}


			public void setWifi(String wifi) {
				this.wifi = wifi;
			}


			public String getRefrigerator() {
				return refrigerator;
			}


			public void setRefrigerator(String refrigerator) {
				this.refrigerator = refrigerator;
			}


			public String getHeating() {
				return heating;
			}


			public void setHeating(String heating) {
				this.heating = heating;
			}


			public String getKitchen() {
				return kitchen;
			}


			public void setKitchen(String kitchen) {
				this.kitchen = kitchen;
			}


			public String getTv() {
				return tv;
			}


			public void setTv(String tv) {
				this.tv = tv;
			}


			public String getParking() {
				return parking;
			}


			public void setParking(String parking) {
				this.parking = parking;
			}


			public String getElevator() {
				return elevator;
			}


			public void setElevator(String elevator) {
				this.elevator = elevator;
			}

		public Integer getF_month() {
			return f_month;
		}

		public void setF_month(Integer f_month) {
			this.f_month = f_month;
		}

		public Integer getF_day() {
			return f_day;
		}

		public void setF_day(Integer f_day) {
			this.f_day = f_day;
		}

		public UIComponent getF_date_UI() {
			return f_date_UI;
		}

		public void setF_date_UI(UIComponent f_date_UI) {
			this.f_date_UI = f_date_UI;
		}

		public UIComponent getS_date_UI() {
			return s_date_UI;
		}

		public void setS_date_UI(UIComponent s_date_UI) {
			this.s_date_UI = s_date_UI;
		}


		public UIComponent getFiles_upload_UI() {
			return files_upload_UI;
		}

		public void setFiles_upload_UI(UIComponent files_upload_UI) {
			this.files_upload_UI = files_upload_UI;
		}


		public List<Room> getApartments() {
			return apartments;
		}


		public void setApartments(List<Room> apartments) {
			this.apartments = apartments;
		}


		public boolean isShow_apartments() {
			return show_apartments;
		}


		public void setShow_apartments(boolean show_apartments) {
			this.show_apartments = show_apartments;
		}


		public String getSmoking_rule() {
			return smoking_rule;
		}


		public void setSmoking_rule(String smoking_rule) {
			this.smoking_rule = smoking_rule;
		}


		public String getPet_rule() {
			return pet_rule;
		}


		public void setPet_rule(String pet_rule) {
			this.pet_rule = pet_rule;
		}


		public String getEvent_rule() {
			return event_rule;
		}


		public void setEvent_rule(String event_rule) {
			this.event_rule = event_rule;
		}



		
}
