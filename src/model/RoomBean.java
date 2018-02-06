package model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.map.GeocodeEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.GeocodeResult;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;



@ManagedBean(name="room")
public class RoomBean {
	private final DataQuery query=new DataQuery();

	private String room_id; 
	private String editing_field;

	private Room room;
	
	
	private String centerGeoMap;
	private String centerRevGeoMap;
	private LatLng center;
	private MapModel simpleModel;
	
	private MapModel geoModel;
    private MapModel revGeoModel;
    private double lat=0;
    private double ling=0;

	List<String> images;
	
	//den periexei thn main
	List<String> images_2;
	
	List<UserRoomInteraction> uri_list;
	List<UserRoomInteraction> comment_list;
	
	private DataTable datatable;

	 private String username;
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
	private String comment;
	
	private Integer rating;
	
	//h hdh uparxousa bathmologia
	private Integer rooms_rating;
	   
	UploadedFile main_pic;
	    
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
	private UIComponent location_UI;
	private UIComponent name_UI;
	private UIComponent m_day_UI;
	private UIComponent rmv_btn_UI;
	
	
	private Date from;
	private Date till;
	
	private int from_month;
	private int till_month;
	
	private int from_day;
	private int till_day;
	
	private int no_people_2;
	private long cost;
	
	
	
	@PostConstruct
    public void init() {  
		//vriskei to dwmatio pou epilexthke kai to apeikonizei

		java.util.Map<String, String> params =FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
 				.getExternalContext().getSession(true);
		
		if(params.get("id")!=null) {
			room_id= params.get("id");
			session.setAttribute("room_id", room_id);
		}
		else room_id=session.getAttribute("room_id").toString()	;
		
		room=query.find_apartment(room_id);
		
		//ama hrthe apo to results
		if(params.get("roomToBook")!=null) {
			from=(Date)session.getAttribute("from");
			till=(Date)session.getAttribute("till");
			no_people_2=(int) session.getAttribute("no_people");
			
			long days=till.getTime()- from.getTime();
			days=TimeUnit.DAYS.convert(days, TimeUnit.MILLISECONDS);
			
			Calendar cal = Calendar.getInstance();
			
			cal.setTime(from);
			from_month = cal.get(Calendar.MONTH) +1;
			from_day = cal.get(Calendar.DAY_OF_MONTH);
			
			cal.setTime(till);
			till_month = cal.get(Calendar.MONTH) +1;
			till_day = cal.get(Calendar.DAY_OF_MONTH);
			
			cost=days*room.getCpp()*no_people_2;
			
			System.out.println("Cost: "+cost+"--"+till_day+"/"+till_month );
			
		}
		//gia ama ginei edit
		if(session.getAttribute("editing_field")!=null) {
			setEditing_field(session.getAttribute("editing_field").toString());
		}
		
		//arxikopoihseis
		centerGeoMap=room.getLat()+","+room.getLing();
		setCenterRevGeoMap(room.getLat()+","+room.getLing());
		
		center=new LatLng(room.getLat(),room.getLing());
		
		simpleModel = new DefaultMapModel();
		simpleModel.addOverlay(new Marker(center));
		
		String picture_folder=room.getPicturesFolder();
		String[] parts = picture_folder.split("resources/");

		String part2 = parts[1];
		
		File folder=new File(picture_folder);
		images = new ArrayList<String>();
		images_2 = new ArrayList<String>();
		for ( final File fileEntry : folder.listFiles()) {
			images.add(part2 + fileEntry.getName());
			if(fileEntry.getName().charAt(0)!='m') {
				images_2.add(part2 + fileEntry.getName());
				
			}
			
		}
		
		geoModel = new DefaultMapModel();
        setRevGeoModel(new DefaultMapModel());
		
        //arxikopoiei ta coment
        uri_list=room.getUserRoomInteractions();
        comment_list=new ArrayList<UserRoomInteraction>();
        for(UserRoomInteraction uri : uri_list) {
        	if(!uri.getCommentDate().equals("")) {
        		comment_list.add(uri);
        	}
        	
        }
       

	}
	//gia ta geocode
	public void onGeocode(GeocodeEvent event) {
        List<GeocodeResult> results = event.getResults();
         
        if (results != null && !results.isEmpty()) {
            LatLng center = results.get(0).getLatLng();
            centerGeoMap = center.getLat() + "," + center.getLng();
            lat=center.getLat();
            ling=center.getLng();
            System.out.println("-->"+lat+" "+ling+" ");
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
     				.getExternalContext().getSession(true);
     		session.setAttribute("lat", lat);
     		session.setAttribute("ling", ling);
     	
            for (int i = 0; i < results.size(); i++) {
                GeocodeResult result = results.get(i);
                geoModel.addOverlay(new Marker(result.getLatLng(), result.getAddress()));
            }
        }
    }
	
	//ama o xrhsths einai o host emfanizei ta koumpia gia to edit
	public boolean render_edit() {
    	HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
 				.getExternalContext().getSession(true);

    	if(room.getUsername().equals(session.getAttribute("username")))
    			return true;
    	return false;
	}
	
	//edit_X vazei to field pou tha ginei edit sto editting field
	public String edit_main_pic() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
 				.getExternalContext().getSession(true);

    	session.setAttribute("editing_field","Main picture of the apartment's profile");
    	return "room_edit_page.xhtml?faces-redirect=true";
	}
	
	public String edit_pictures() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
 				.getExternalContext().getSession(true);

    	session.setAttribute("editing_field","Pictures of the apartment");
    	return "room_edit_page.xhtml?faces-redirect=true";
	}
	
	public String edit_info() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
 				.getExternalContext().getSession(true);

    	session.setAttribute("editing_field","Information about the apartment/room");
    	return "room_edit_page.xhtml?faces-redirect=true";
	}
	
	public String edit_description() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
 				.getExternalContext().getSession(true);

    	session.setAttribute("editing_field","Descreption");
    	return "room_edit_page.xhtml?faces-redirect=true";
	}
	
	public String edit_rules() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
 				.getExternalContext().getSession(true);

    	session.setAttribute("editing_field","Rules of the apartment/room");
    	return "room_edit_page.xhtml?faces-redirect=true";
	}
	
	public String edit_location() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
 				.getExternalContext().getSession(true);

    	session.setAttribute("editing_field","Location");
    	return "room_edit_page.xhtml?faces-redirect=true";
	}
	
	//render_X apeikonizei to katallhlo panel
	public boolean render_location() {
		if(editing_field.equals("Location"))
			return true;
		return false;
	}
	
	public boolean render_rules() {
		if(editing_field.equals("Rules of the apartment/room"))
			return true;
		return false;
	}
	
	public boolean render_Descreption() {
		if(editing_field.equals("Descreption"))
			return true;
		return false;
	}
	
	public boolean render_info() {
		if(editing_field.equals("Information about the apartment/room")) 
			return true;
		return false;
	}
	
	public boolean render_mainPic() {
		if(editing_field.equals("Main picture of the apartment's profile"))
			return true;
		return false;
	}
	
	public boolean render_pictures() {
		if(editing_field.equals("Pictures of the apartment"))
			return true;
		return false;
	}
	
	//kanei edit tis times pou allaxthkan..alla tis checkarei prwta
	public String edit() {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
 				.getExternalContext().getSession(true);
		
		boolean flag=false;
		
		if(editing_field.equals("Information about the apartment/room")) {
			if(username!="") {
				room.setRoomName(username);
				
			}
			if(m_charge!=0) {
				if(m_charge<0) {
					String message="Please insert number bigger than '0'.";
					FacesMessage facesMessage = new FacesMessage(message);
			        context.addMessage(m_charge_UI.getClientId(context), facesMessage);
			        flag=true;
				}
				else
					room.setMCharge(m_charge);
				
			}
			if(cpp!=0) {
				if(cpp<0) {
					String message="Please insert number bigger than '0'.";
					FacesMessage facesMessage_2 = new FacesMessage(message);
			        context.addMessage(cpp_UI.getClientId(context), facesMessage_2);
			        flag=true;
				}
				else 
					room.setCpp(cpp);
				
				
			}
			
			if(f_month!=0 || f_day!=0) {
				if(f_month<=0 || f_day<=0 || f_month>12 || f_day>31) {
					String message="Wrong input for date.";
					FacesMessage facesMessage_3 = new FacesMessage(message);
					context.addMessage(f_date_UI.getClientId(context), facesMessage_3);
					flag=true;
				}
				else {
					String date, f_day_str, f_month_str = null, new_date="";
				
					if(f_day<10)
						f_day_str="0"+f_day.toString();
					else
						f_day_str=f_day.toString();
				
					if(f_month<10)
						f_month_str="0"+f_month.toString();
					else
						f_month_str=f_month.toString();
					
					date=room.getDate();
					String edited_date=f_day_str+"/"+f_month_str;
					
					String[] dates=date.split("-");
					
					for(int i=0;i<dates.length-1;i++) {
						new_date=new_date+dates[i]+"-";
					}
					new_date=new_date+edited_date;
					
					//	room.setDate(date);
					System.out.println("new Date-->"+new_date);
				}
			}
			if(s_month!=0 || s_day!=0) {
				if(s_month<=0 || s_day<=0 || s_month>12 || s_day>31) {
					String message="Wrong input for date '0'.";
					FacesMessage facesMessage_4 = new FacesMessage(message);
					context.addMessage(s_date_UI.getClientId(context), facesMessage_4);
					flag=true;
				
				}
				else {
					String date, s_day_str, s_month_str = null, new_date="";		
					if(s_day<10)
						s_day_str="0"+s_day.toString();
					else
						s_day_str=s_day.toString();
				
					if(s_month<10)
						s_month_str="0"+s_month.toString();
					else
						s_month_str=s_month.toString();
					
					String edited_date=s_day_str+"/"+s_month_str;
		
					date=room.getDate();
					String[] dates=date.split("-");
					new_date=new_date+edited_date;
					for(int i=1;i<dates.length;i++) {
						new_date=new_date+"-"+dates[i];
					}
				
					//room.setDate(date);
					System.out.println("new Date-->"+new_date);
				}
			}
			if(type!="") {
				room.setType(type);
				
			}
			if(beds!=0){
				if(beds<0) {
					String message="Please insert number bigger than '0'.";
					FacesMessage facesMessage_6 = new FacesMessage(message);
			        context.addMessage(cpp_UI.getClientId(context), facesMessage_6);
			        flag=true;
				}
				else
					room.setNoBeds(beds);
				
			}
			if(baths!=0) {
				if(baths<0) {
					String message="Please insert number bigger than '0'.";
					FacesMessage facesMessage_7 = new FacesMessage(message);
			        context.addMessage(cpp_UI.getClientId(context), facesMessage_7);
			        flag=true;
				}
				else
					room.setNoBaths(baths);
				
				
			}
			if(area!=0) {
				if(area<0) {
					String message="Please insert number bigger than '0'.";
					FacesMessage facesMessage_8 = new FacesMessage(message);
			        context.addMessage(cpp_UI.getClientId(context), facesMessage_8);
			        flag=true;
				}
				else
					room.setArea(area);
				
			}
			if(living_r!="") {
				room.setLivingroom(living_r);
				
			}
			if(wifi!="") {
				room.setWifi(wifi);
				
			}
			if(tv!="") {
				room.setTv(tv);
		
			}
			if(kitchen!="") {
				room.setKitchen(kitchen);
				
			}
			if(elevator!="") {
				room.setElevator(elevator);
				
			}
			if(refrigerator!="") {
				room.setRefrigerator(refrigerator);
				
			}
			if(heating!="") {
				room.setHeating(heating);
				
			}
			if(parking!="") {
				room.setParking(parking);				
			}

		}
		else if(editing_field.equals("Descreption")) {
			if(description!="") {
				room.setDescription(description);
				
			}
		}
		else if(editing_field.equals("Location")) {
			
			if(!location.equals("")) {
				room.setLocation(location.toUpperCase());
			}
			if(!info.equals("")) {
				room.setInfo(info);
			}
			if(!address.equals("")) {
				room.setLat(Double.parseDouble(session.getAttribute("lat").toString()));
	    		room.setLing(Double.parseDouble(session.getAttribute("ling").toString()));
	    		room.setAddress(address);
			}
		}
		else if(editing_field.equals("Rules of the apartment/room")) {
			if(smoking_rule!="") {
				room.setSmoking(smoking_rule);
			}
			if(event_rule!="") {
				room.setEvents(event_rule);
			}
			if(pet_rule!="") {
				room.setPets(pet_rule);
			}
			if(no_people!=0) {
				if(no_people<0) {
					String message="Please insert number bigger than '0'.";
					FacesMessage facesMessage = new FacesMessage(message);
			        context.addMessage(no_people_UI.getClientId(context), facesMessage);
			        flag=true;
				}
				else
					room.setNoPeople(no_people);
				
			}
			if(m_day!=0) {
				if(m_day<0) {
					String message="Please insert number bigger than '0'.";
					FacesMessage facesMessage_2 = new FacesMessage(message);
			        context.addMessage(m_day_UI.getClientId(context), facesMessage_2);
			        flag=true;
				}
				else	
					room.setMDays(m_day);
			}
			
		}
		else if(editing_field.equals("Main picture of the apartment's profile")) {
			if(main_pic!=null) {
				if(main_pic.getSize()>0) {
					
					String[] parts = main_pic.getFileName().split("\\.");
					System.out.println("--"+parts[1]);
					if(!parts[1].equals("gif") &&
							!parts[1].equals("jpg") &&
							!parts[1].equals("jpe") && 
							!parts[1].equals("png")) {
	
						String message="Please insert an image file (.jpg, .jpe, .gif, .png)";
						FacesMessage facesMessage_81 = new FacesMessage(message);
						context.addMessage(main_pic_UI.getClientId(context), facesMessage_81);
						flag=true;
					}
					else {
						System.out.println(room.getPicturesFolder());
						System.out.println(main_pic.getSize());
			
						File folder=new File(room.getPicturesFolder());
						//ftiaxnei katallhlo arxeio gia to apartment
						for ( final File fileEntry : folder.listFiles()) {
				
							if(fileEntry.getName().charAt(0)=='m') {
								
								File file = new File(room.getPicturesFolder()+fileEntry.getName());
								
								if(file.delete()){
					    			System.out.println(file.getName() + " is deleted!");
					    		}else{
					    			System.out.println("Delete operation is failed.");
					    		}
								
								
								String extension = FilenameUtils.getExtension(main_pic.getFileName());
					    		String picture_path=room.getPicturesFolder()+"main."+extension;
					    		File picture=new File(picture_path);
					    		System.out.println("save- picture absolute path "+ picture.getAbsolutePath());
					    	
					    		try {
					    			Path path=Paths.get(picture.getAbsolutePath());
					    			InputStream input = main_pic.getInputstream();
					    			
					    			Files.copy(input, path, StandardCopyOption.REPLACE_EXISTING);
					    			
					    		} catch (IOException e1) {
					    			// TODO Auto-generated catch block
					    			e1.printStackTrace();
					    			System.out.println("Failed to save uploaded File");
					    		}

					    		String[] pic_path=room.getMainPic().split("/");
					    		picture_path=pic_path[pic_path.length-3]+"/"+pic_path[pic_path.length-2]+"/main."+extension;
					    		System.out.println("---->>>>"+picture_path+"--"+room.getMainPic());
					    		room.setMainPic(picture_path);
					    		
							}
						}
					}
				}
			}
		}
		
		query.commit();
		
		if(!flag)
			return "room_page.xhtml?faces-redirect=true";
		else 
			return "";
	}
	
	//diagrafei thn eikona pou epilxthike na diagraftei
	public String remove_picture(){
		System.out.println("Delete file");
		String pic=(String) datatable.getRowData();
		
		String pic_name=pic.split("/")[pic.split("/").length-1];
		System.out.println("-->"+room.getPicturesFolder()+pic_name);
		
		File file = new File(room.getPicturesFolder()+pic_name);
		if(file.delete()){
			System.out.println(file.getName() + " is deleted!");
			
			FacesContext context = FacesContext.getCurrentInstance();
			String message="The picture was deleted Successfully.";
			FacesMessage facesMessage = new FacesMessage(message);
			//context.addMessage(null, facesMessage);
	     
			context.addMessage(null,facesMessage);
			context.getExternalContext().getFlash().setKeepMessages(true);
			
		}else{
			System.out.println("Delete operation is failed.");
		}
		return "room_edit_page.xhtml?faces-redirect=true";
	}
	
	//kanei upload fwtografia
	 public String upload_file(FileUploadEvent event) throws IOException {
	    	
	    	UploadedFile uploaded_file=event.getFile();
	    	String pictures_folder=room.getPicturesFolder();
	 		
	 		System.out.println("upload-  "+ pictures_folder);
	 		
	 		Path folder = Paths.get(pictures_folder);
		
			String filename = FilenameUtils.getBaseName(uploaded_file.getFileName()); 
			String extension = FilenameUtils.getExtension(uploaded_file.getFileName());
			Path file = Files.createTempFile( folder,filename+"-" , "." + extension);
			System.out.println("folder: "+folder.toString()+"\n"+"filename: "+filename+"\n"
			 	+ "file: "+file.toString());
			
		
			try (InputStream input =uploaded_file.getInputstream()) {
			    Files.copy(input, file, StandardCopyOption.REPLACE_EXISTING);
			}
			
			FacesContext context = FacesContext.getCurrentInstance();
			String message="Pictures were uploaded Successfully.";
			FacesMessage facesMessage = new FacesMessage(message);
	        context.addMessage(null, facesMessage);
	        
	        return "room_edit_page.xhtml?faces-redirect=true";
			
		}
	 		
	 		

			//kanei comment...mono an exei klisei to dwmatio borei na kanei comment
	    public String Comment() {   	
	    	HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
	 				.getExternalContext().getSession(true);
	    	
	    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	    	Date date = new Date();
	    	System.out.println(dateFormat.format(date)); //2016/11/16 

	    	for(UserRoomInteraction uri : uri_list) {
				   if(uri.getUser().getUsername().equals(session.getAttribute("username").toString())) {
					   uri.setComment(comment);
					   uri.setCommentDate(dateFormat.format(date));
					   
					   query.commit();   
					   break;
				   }
			   }
	    	return "room_page.xhtml?faces-redirect=true";
	    }
	    
	    
	    //diagrafei comment...mono o host borei
	   public String deleteComment(String str_index) {
		  
		   System.out.println(comment_list.get(Integer.parseInt(str_index)).getComment());
		   UserRoomInteraction uri=query.findUserRoomInteraction(comment_list.get(Integer.parseInt(str_index)).getInteractionId());
		   uri.setComment("You haven't leave a Comment");
		   uri.setCommentDate("");
		   query.commit();  
	   
		   return "room_page.xhtml?faces-redirect=true";
		   
	   }
	   
	   
	   
	   public String rate() {
		   HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
	 				.getExternalContext().getSession(true);
	    	
		   for(UserRoomInteraction uri : uri_list) {
			   if(uri.getUser().getUsername().equals(session.getAttribute("username").toString())) {
				   uri.setRating(rating);
				   System.out.println("change existing rating"); 			   
				   break;
			   }
		   }
		   
		   //upologizei thn bathmologia tou
		   int count=1 , sum=rating;
		   for( UserRoomInteraction uri : uri_list) {
			  if(uri.getRating()>0) {
				  count++;
				  sum=sum+uri.getRating();
				  System.out.println("sum "+ sum + "-- "+ count);
			  }
			 }
			 if(count>0) { 
			   room.setNoRatings(count);
			   room.setRating(sum/count);
					
			 }
			 else {
			   room.setRating(1);
			   rooms_rating=1;
			 }
			 System.out.println("Create raiting"+sum+"--"+count+"-->"+sum/count);
			 query.commit();
		   
			 return "room_page.xhtml?faces-redirect=true";
	    }

	
	   public boolean render_rating() {
		   HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
	 				.getExternalContext().getSession(true);
		   if(session.getAttribute("username")!=null) {
			   for(UserRoomInteraction uri : uri_list) {
				   if(uri.getUser().getUsername().equals(session.getAttribute("username").toString())) 
					   return true;
			   }
		   }
		   return false;
	   }
	   
	   
	   
	   //an exei erthei apta results tote tou dinei th dunatothta na to klisei
	   public boolean render_book() {
		   
		   java.util.Map<String, String> params =FacesContext.getCurrentInstance().
	                getExternalContext().getRequestParameterMap();
	   
		   HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
	 				.getExternalContext().getSession(true);
		   
		   if(session.getAttribute("from")!=null 
				   && session.getAttribute("till")!=null 
				   && session.getAttribute("no_people")!=null
				   && params.get("roomToBook")!=null )
			   return true;
		   else
			   return false;   
	   }
	   
	
	   //klinei to dwmatio
	public String book() {
		 HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
	 				.getExternalContext().getSession(true);
		 
		boolean find=false;
		String edited_date="";
		String new_date="";
		
		from=(Date)session.getAttribute("from");
		till=(Date)session.getAttribute("till");
		String date=room.getDate();
		
		System.out.println("Date--->"+ date);
		
		/*oi hmeromhnies einai apothikeumenes me thn morfh px "9/1-20/1:24/1-29/1"
			pou shmainei pws to dwmatio einai diatheshmo apo tis 9 mexri tis 20 kai apo
			tis 24 mexri tis 29...opote vriskei se poio diasthma theloume na kleisei kai
			epanaupologizei thn hmeromhnia ..px thelei na klisei aptis 12/1-18/1 tha ginei
			9/1-12/1:18/1-20/1:24/1-29/1
		*/
		if(date.contains(":")) {
			String[] dates=date.split(":");
			int i=0;
			for(i=0;i<dates.length ;i++) {
				
				String[] date_str=dates[i].split("-");
				
				Date s_date= new GregorianCalendar(2017, Integer.parseInt(date_str[0].split("/")[1])-1, Integer.parseInt((date_str[0].split("/"))[0])).getTime() ;
				Date f_date= new GregorianCalendar(2017, Integer.parseInt(date_str[1].split("/")[1])-1, Integer.parseInt((date_str[1].split("/"))[0])).getTime() ;
				
				
				if((from.after(s_date) || from.equals(s_date)) && (till.before(f_date) || till.equals(f_date))) {
					
					find=true;
					if(s_date.equals(from)) {
						if(f_date.equals(till)) {
							
							//lathos timh..shmainei oti den thanei pote diatheshmo pleon..den tha to vgazei to search
							edited_date="5/9-4/9";
						}
						else {
							Calendar cal_till = Calendar.getInstance();
							Calendar cal_fdate = Calendar.getInstance();
							cal_fdate.setTime(f_date);	
							cal_till.setTime(till);
							edited_date= Integer.toString(cal_till.get(Calendar.DAY_OF_MONTH))+"/"+Integer.toString(cal_till.get(Calendar.MONTH) +1)+
									"-"+Integer.toString(cal_fdate.get(Calendar.DAY_OF_MONTH))+"/"+Integer.toString(cal_fdate.get(Calendar.MONTH) +1);
						}
					}
					else {
						if(f_date.equals(till)) {
							Calendar cal_from = Calendar.getInstance();
							Calendar cal_sdate = Calendar.getInstance();
							cal_sdate.setTime(s_date);	
							cal_from.setTime(from);
							edited_date= Integer.toString(cal_sdate.get(Calendar.DAY_OF_MONTH))+"/"+Integer.toString(cal_sdate.get(Calendar.MONTH) +1)+
									"-"+Integer.toString(cal_from.get(Calendar.DAY_OF_MONTH))+"/"+Integer.toString(cal_from.get(Calendar.MONTH) +1);
							
							//dates[i]= s_date-from;
						}
						else {
							Calendar cal_till = Calendar.getInstance();
							Calendar cal_fdate = Calendar.getInstance();
							cal_fdate.setTime(f_date);	
							cal_till.setTime(till);
						
							Calendar cal_from = Calendar.getInstance();
							Calendar cal_sdate = Calendar.getInstance();
							cal_sdate.setTime(s_date);	
							cal_from.setTime(from);
						
							edited_date= Integer.toString(cal_sdate.get(Calendar.DAY_OF_MONTH))+"/"+Integer.toString(cal_sdate.get(Calendar.MONTH) +1)+
								"-"+Integer.toString(cal_from.get(Calendar.DAY_OF_MONTH))+"/"+Integer.toString(cal_from.get(Calendar.MONTH) +1)+":"
								+Integer.toString(cal_till.get(Calendar.DAY_OF_MONTH))+"/"+Integer.toString(cal_till.get(Calendar.MONTH) +1)+
								"-"+Integer.toString(cal_fdate.get(Calendar.DAY_OF_MONTH))+"/"+Integer.toString(cal_fdate.get(Calendar.MONTH) +1);
						
							//dates[i]= s_date-from:till-f_date;
						}
					}
				}
				if(find)
					break;
			}
			//reconstruct to string
			if(find) {
				
				for(int j=0;j<dates.length;j++) {
					if(i!=j){
						
						new_date=new_date+dates[j];
					}
					else{
						new_date=new_date+edited_date;
						
					}
					if(j!=dates.length-1)
						new_date=new_date+":";
				}
			}
			
		}
		else {

			String[] date_str=date.split("-");
			Date s_date= new GregorianCalendar(2017, Integer.parseInt(date_str[0].split("/")[1])-1, Integer.parseInt((date_str[0].split("/"))[0])).getTime() ;
			Date f_date= new GregorianCalendar(2017, Integer.parseInt(date_str[1].split("/")[1])-1, Integer.parseInt((date_str[1].split("/"))[0])).getTime() ;
			
			System.out.println((from.after(s_date) || from.equals(s_date)) +"--"+ (till.before(f_date) || till.equals(f_date)));
			
			if((from.after(s_date) || from.equals(s_date)) && (till.before(f_date) || till.equals(f_date))) {
				find=true;
				if(s_date.equals(from)) {
					if(f_date.equals(till)) {
						//lathos timh..shmainei oti den thanei pote diatheshmo pleon..den tha to vgazei to search
						edited_date="5/9-4/9";
					}
					else {
						Calendar cal_till = Calendar.getInstance();
						Calendar cal_fdate = Calendar.getInstance();
						cal_fdate.setTime(f_date);	
						cal_till.setTime(till);
						edited_date= Integer.toString(cal_till.get(Calendar.DAY_OF_MONTH))+"/"+Integer.toString(cal_till.get(Calendar.MONTH) +1)+
								"-"+Integer.toString(cal_fdate.get(Calendar.DAY_OF_MONTH))+"/"+Integer.toString(cal_fdate.get(Calendar.MONTH) +1);
					}
				}
				else {
					if(f_date.equals(till)) {
						Calendar cal_from = Calendar.getInstance();
						Calendar cal_sdate = Calendar.getInstance();
						cal_sdate.setTime(s_date);	
						cal_from.setTime(from);
						edited_date= Integer.toString(cal_sdate.get(Calendar.DAY_OF_MONTH))+"/"+Integer.toString(cal_sdate.get(Calendar.MONTH) +1)+
								"-"+Integer.toString(cal_from.get(Calendar.DAY_OF_MONTH))+"/"+Integer.toString(cal_from.get(Calendar.MONTH) +1);
						
						//dates[i]= s_date-from;
					}
					else {
						Calendar cal_till = Calendar.getInstance();
						Calendar cal_fdate = Calendar.getInstance();
						cal_fdate.setTime(f_date);	
						cal_till.setTime(till);
					
						Calendar cal_from = Calendar.getInstance();
						Calendar cal_sdate = Calendar.getInstance();
						cal_sdate.setTime(s_date);	
						cal_from.setTime(from);
					
						edited_date= Integer.toString(cal_sdate.get(Calendar.DAY_OF_MONTH))+"/"+Integer.toString(cal_sdate.get(Calendar.MONTH) +1)+
							"-"+Integer.toString(cal_from.get(Calendar.DAY_OF_MONTH))+"/"+Integer.toString(cal_from.get(Calendar.MONTH) +1)+":"
							+Integer.toString(cal_till.get(Calendar.DAY_OF_MONTH))+"/"+Integer.toString(cal_till.get(Calendar.MONTH) +1)+
							"-"+Integer.toString(cal_fdate.get(Calendar.DAY_OF_MONTH))+"/"+Integer.toString(cal_fdate.get(Calendar.MONTH) +1);
					
						//dates[i]= s_date-from:till-f_date;
					
					}
				}
			}
			//reconstruct to string
			if(find) 
				new_date=edited_date;

		}
		
		System.out.println("New Date--->"+ new_date);
		
		
    	UserRoomInteraction uri=new UserRoomInteraction();
		uri.setComment("You haven't leave a Comment");
		uri.setCommentDate("");
		uri.setRoom(room);
		
		
		if(session.getAttribute("username")!=null) {
			User u=query.findUser(session.getAttribute("username").toString());
			uri.setUser(u);
			u.addUserRoomInteraction(uri);
		}
		else {
			//ama den einai sundedemenos o user apla xrisimopoiei ton user tou idiokthth tou room.
			//den paizei kapoio sugkekrimeno rolo
			uri.setUser(room.getUser());
			
		}
		
		uri.setRating(0);
		
		uri.setBookedDate(from_day+"/"+from_month+"-"+till_day+"/"+till_month);
		room.addUserRoomInteraction(uri);
		room.setDate(new_date);
		
		query.persist(uri);
		
		String message="You successfully booked "+room.getRoomName()+" from: "+from.toString()+" till: "+ till.toString();
		FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, message, null);
		FacesContext context=FacesContext.getCurrentInstance();
		context.addMessage(null,facesMessage);
		context.getExternalContext().getFlash().setKeepMessages(true);
		
		
		return "welcome.xhtml?faces-redirect=true";
	}
	
	
	//an einai sundedemenos o xrhsths borei mono na milhsei..
	public boolean render_chatBtn() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
 				.getExternalContext().getSession(true);
		if(session.getAttribute("username")!=null)
			return true;
		return false;
		
	}
	
	
	
	//dhmiourgei chat me ton host..sth sunexeia mono o host tha exei thn dunatotha
	//diagrafeis mhnumatwn
	public String message_host() {
		 HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
	 				.getExternalContext().getSession(true);
		 
		List<ChatRoom> chats=room.getUser().getChatRooms1();
		chats.addAll(room.getUser().getChatRooms2());
		
		User u=query.findUser(session.getAttribute("username").toString());
		
		boolean find=false;
		
		for(ChatRoom chat : chats) {
			if(chat.getUser2().getUsername().equals(session.getAttribute("username")) 
					|| chat.getUser1().getUsername().equals(session.getAttribute("username"))) {
				find=true;	
			}
		}
		if(!find) {
			ChatRoom chat=new ChatRoom();
			
			chat.setUser1(room.getUser());
			chat.setUser2(u);
			
			u.addChatRooms2(chat);
			room.getUser().addChatRooms1(chat);
			
			query.persist(chat);
			
			System.out.println("chat was created");
			
			
		}
		
		session.setAttribute("reciever", room.getUser());
		session.setAttribute("sender", u);
		
		System.out.println(find);
		return "chat_room.xhtml?faces-redirect=true";
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public LatLng getCenter() {
		return center;
	}

	public void setCenter(LatLng center) {
		this.center = center;
	}
	
	public String getRoom_id() {
		return room_id;
	}

	public void setRoom_id(String room_id) {
		this.room_id = room_id;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}




	public MapModel getSimpleModel() {
		return simpleModel;
	}




	public void setSimpleModel(MapModel simpleModel) {
		this.simpleModel = simpleModel;
	}




	public String getCenter_str() {
		return centerGeoMap;
	}




	public void setCenter_str(String center_str) {
		this.centerGeoMap = center_str;
	}




	public List<String> getImages() {
		return images;
	}




	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getEditing_field() {
		return editing_field;
	}

	public List<UserRoomInteraction> getComment_list() {
		return comment_list;
	}

	public void setComment_list(List<UserRoomInteraction> comment_list) {
		this.comment_list = comment_list;
	}

	public void setEditing_field(String editing_field) {
		this.editing_field = editing_field;
	}

	public String getCenterRevGeoMap() {
		return centerRevGeoMap;
	}

	public void setCenterRevGeoMap(String centerRevGeoMap) {
		this.centerRevGeoMap = centerRevGeoMap;
	}

	public String getCenterGeoMap() {
		return centerGeoMap;
	}

	public void setCenterGeoMap(String centerGeoMap) {
		this.centerGeoMap = centerGeoMap;
	}

	public MapModel getGeoModel() {
		return geoModel;
	}

	public MapModel getRevGeoModel() {
		return revGeoModel;
	}

	public void setRevGeoModel(MapModel revGeoModel) {
		this.revGeoModel = revGeoModel;
	}

	public void setGeoModel(MapModel geoModel) {
		this.geoModel = geoModel;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public int getFrom_month() {
		return from_month;
	}

	public void setFrom_month(int from_month) {
		this.from_month = from_month;
	}

	public int getTill_month() {
		return till_month;
	}

	public void setTill_month(int till_month) {
		this.till_month = till_month;
	}

	public int getFrom_day() {
		return from_day;
	}

	public void setFrom_day(int from_day) {
		this.from_day = from_day;
	}

	public int getTill_day() {
		return till_day;
	}

	public void setTill_day(int till_day) {
		this.till_day = till_day;
	}

	public double getLing() {
		return ling;
	}

	public void setLing(double ling) {
		this.ling = ling;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getAddress() {
		return address;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer Rating) {
		rating = Rating;
	}

	public Integer getRooms_rating() {
		return rooms_rating;
	}

	public void setRooms_rating(Integer rooms_rating) {
		this.rooms_rating = rooms_rating;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public UploadedFile getMain_pic() {
		return main_pic;
	}

	public void setMain_pic(UploadedFile main_pic) {
		this.main_pic = main_pic;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTill() {
		return till;
	}

	public void setTill(Date till) {
		this.till = till;
	}

	public int getNo_people_2() {
		return no_people_2;
	}

	public void setNo_people_2(int no_people_2) {
		this.no_people_2 = no_people_2;
	}

	public long getCost() {
		return cost;
	}

	public void setCost(long cost) {
		this.cost = cost;
	}

	public List<UserRoomInteraction> getUri_list() {
		return uri_list;
	}

	public void setUri_list(List<UserRoomInteraction> uri_list) {
		this.uri_list = uri_list;
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

	public List<String> getImages_2() {
		return images_2;
	}

	public void setImages_2(List<String> images_2) {
		this.images_2 = images_2;
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

	public Integer getM_day() {
		return m_day;
	}

	public void setM_day(Integer m_day) {
		this.m_day = m_day;
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

	public String getLiving_r() {
		return living_r;
	}

	public void setLiving_r(String living_r) {
		this.living_r = living_r;
	}

	public String getWifi() {
		return wifi;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public DataTable getDatatable() {
		return datatable;
	}

	public void setDatatable(DataTable datatable) {
		this.datatable = datatable;
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

	public UIComponent getType_UI() {
		return type_UI;
	}

	public void setType_UI(UIComponent type_UI) {
		this.type_UI = type_UI;
	}

	public UIComponent getDescription_UI() {
		return description_UI;
	}

	public void setDescription_UI(UIComponent description_UI) {
		this.description_UI = description_UI;
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

	public UIComponent getMain_pic_UI() {
		return main_pic_UI;
	}

	public void setMain_pic_UI(UIComponent main_pic_UI) {
		this.main_pic_UI = main_pic_UI;
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

	public UIComponent getLocation_UI() {
		return location_UI;
	}

	public void setLocation_UI(UIComponent location_UI) {
		this.location_UI = location_UI;
	}

	public UIComponent getName_UI() {
		return name_UI;
	}

	public void setName_UI(UIComponent name_UI) {
		this.name_UI = name_UI;
	}

	public UIComponent getM_day_UI() {
		return m_day_UI;
	}

	public void setM_day_UI(UIComponent m_day_UI) {
		this.m_day_UI = m_day_UI;
	}

	public UIComponent getRmv_btn_UI() {
		return rmv_btn_UI;
	}

	public void setRmv_btn_UI(UIComponent rmv_btn_UI) {
		this.rmv_btn_UI = rmv_btn_UI;
	}






}
