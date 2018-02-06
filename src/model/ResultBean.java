package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;



@ManagedBean(name="result")
public class ResultBean {
	private String location;
	private Date from;
	private Date till;
	private Integer no_people;
	
	
	private String type;
	private String wifi;
    private String refrigerator;
    private String heating;
    private String kitchen;
    private String tv;
    private String parking;
    private String elevator;
    private Integer max_cost;
	
	private final DataQuery query=new DataQuery();
	List<Room> results;
	List<Room> page_results;
	
	
	
	private UIComponent from_UI;
	private UIComponent till_UI;
	private UIComponent no_people_UI;
	private UIComponent location_UI;
	private UIComponent max_cost_UI;
	
	private boolean render_recommendetion=false;
	private List<Room> recommended_rooms;
	
	
	/*
	 *an h selida einai h results.xhtml tote psaxnei gia results..arxika pairnei ola ta
	 *dwmatia pou einai sto sugkekrimeno location kai dexwntai tosous anthrwpous..
	 *sth sunexeia checkarei tis hmeromhnies na dei an einai diatheshmo kai sth sunexeia
	 *elegxei gia ta alla filtra an exoun dwthei
	 * */
	


	@PostConstruct
	public void init() {
	
		
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);

		//kanei to recommendation
		if(session.getAttribute("username")!=null) {		
			Recommend recommend=new Recommend();
			recommend.CreateVectors();
			recommended_rooms=recommend.recommendation();
			if(recommended_rooms!=null) {
				System.out.print("---RESULTS--\n[");
				for(int j=0; j<recommended_rooms.size(); j++) 
					System.out.print(" . "+recommended_rooms.get(j).getRoomName()+" . ");
				System.out.print("]\n");
				render_recommendetion=true;
			}
		}
		
		FacesContext facesContext=FacesContext.getCurrentInstance();
		String viewId = facesContext.getViewRoot().getViewId();
		
		if(viewId.equals("/results.xhtml")) {
			if(session.getAttribute("location")!=null){
				location=session.getAttribute("location").toString();
				no_people=Integer.parseInt(session.getAttribute("no_people").toString());
			
				results=query.findSimpleResults(location, no_people);
			
				from=(Date)session.getAttribute("from");
				till=(Date)session.getAttribute("till");
				
			
				long days=till.getTime()- from.getTime();
				days=TimeUnit.DAYS.convert(days, TimeUnit.MILLISECONDS);
							
				
				for (Iterator<Room> iterator = results.iterator(); iterator.hasNext();) { 
					Room r=iterator.next();
					
					String date=r.getDate();
					/*
					 * h hmeromhnia borei na einai ths morfhs "9/3-15/3:17/3-20/3:26/3-1/4
					 * pou shmainei pws to diamerixma einai diatheshmo sta diasthmata X/X-Y/Y 
					 * opote spame to string kai elegxoume an yparxei diasthma pou mas kanei*/
					if(date.contains(":")) {
						boolean find=false;
						String[] dates_str=date.split(":");
						for(int i=0; i<dates_str.length; i++) {
							String[] date_str=dates_str[i].split("-");
														
							Date s_date= new GregorianCalendar(2017, Integer.parseInt(date_str[0].split("/")[1])-1, Integer.parseInt((date_str[0].split("/"))[0])).getTime() ;
							Date f_date= new GregorianCalendar(2017, Integer.parseInt(date_str[1].split("/")[1])-1, Integer.parseInt((date_str[1].split("/"))[0])).getTime() ;
					
							if((from.after(s_date) || from.equals(s_date)) && (till.before(f_date) || till.equals(f_date))) {
								find=true;
								break;
							}
						}
						if(!find) {
							iterator.remove();
							continue;
						}
						
					}
					else {
					
						String[] date_str=date.split("-");
						Date s_date= new GregorianCalendar(2017, Integer.parseInt(date_str[0].split("/")[1])-1, Integer.parseInt((date_str[0].split("/"))[0])).getTime() ;
						Date f_date= new GregorianCalendar(2017, Integer.parseInt(date_str[1].split("/")[1])-1, Integer.parseInt((date_str[1].split("/"))[0])).getTime() ;
				
						if(from.before(s_date) || till.after(f_date)) {
							iterator.remove();
							continue;
						}
					}
				
					if(no_people*r.getCpp()*days < r.getMCharge()) {
						iterator.remove();
						continue;
					}	
					if(days < r.getMDays()) {
						iterator.remove();
						continue;
					}
				
				}	
			
				if(session.getAttribute("type")!=null || session.getAttribute("wifi")!=null 
					|| session.getAttribute("tv")!=null || session.getAttribute("heating")!=null
					|| session.getAttribute("elevator")!=null || session.getAttribute("kitchen")!=null 
					|| session.getAttribute("parking")!=null ||  session.getAttribute("refrigerator")!=null	
					|| session.getAttribute("max_cost")!=null) {
				
					for (Iterator<Room> iterator = results.iterator(); iterator.hasNext();) { 
						Room r=iterator.next();
						if(session.getAttribute("type")!=null) {
							if(!r.getType().equals(session.getAttribute("type").toString())) {
								iterator.remove();
								continue;
							}
						}
						if(session.getAttribute("wifi")!=null) {
							if(!r.getWifi().equals(session.getAttribute("wifi").toString())) {
								iterator.remove();
								continue;
							}
						}
						if(session.getAttribute("tv")!=null) {
							if(!r.getTv().equals(session.getAttribute("tv").toString())) {
								iterator.remove();
								continue;
							}
						}
						if(session.getAttribute("heating")!=null) {
							if(!r.getHeating().equals(session.getAttribute("heating").toString())) {
								iterator.remove();
								continue;
							}
						}
						if(session.getAttribute("elevator")!=null) {
							if(!r.getElevator().equals(session.getAttribute("elevator").toString())) {
								iterator.remove();
								continue;
							}
						}
						if(session.getAttribute("kitchen")!=null) {
							if(!r.getKitchen().equals(session.getAttribute("kitchen").toString())) {
								iterator.remove();
								continue;
							}
						}
						if(session.getAttribute("parking")!=null) {
							if(!r.getParking().equals(session.getAttribute("parking").toString())) {
								iterator.remove();
								continue;
							}
						}
						if(session.getAttribute("refrigerator")!=null) {
							if(!r.getRefrigerator().equals(session.getAttribute("refrigerator").toString())) {
								iterator.remove();
								continue;
							}
						}
						if(session.getAttribute("max_cost")!=null) {
							System.out.println("max_cost"+(int)session.getAttribute("max_cost")+"--cost: "+no_people*r.getCpp()*days);
							if((int)session.getAttribute("max_cost") < no_people*r.getCpp()*days ) {
								iterator.remove();
								continue;
								
							}
						}
					}
				}
				
				//ta kanoume sort me vash to costos ana atomo
				Collections.sort( results, new Comparator<Room>(){
					@Override
					public int compare(Room r1, Room r2) {
						return r1.getCpp() > r2.getCpp() ? 1 : (r1.getCpp() < r2.getCpp() ? -1 : 0);
               
					}});
			
				if(session.getAttribute("page")!=null) {
					int page=Integer.parseInt(session.getAttribute("page").toString());
					int start=page*10; //<-----10
					int end=(page+1)*10-1;
	
					page_results=new ArrayList<Room>();
					System.out.println("page:"+page+"--start:"+start+"-end:"+end);
					if(end<results.size())
						for(int i=start;i<=end;i++) 	{
							page_results.add(results.get(i));
						}
					else
						for(int i=start;i<results.size();i++) {
							page_results.add(results.get(i));
						}
				}	
			}
		}
	}
	
	
	//apla ta vazei sto session kai ta results upologizwntai apo thn init
	//vazei mono ta filtra pou dwthikan...an dwsei lathos basika stoixeia
	//emfanizei error mhnuma
	public String find_results() {
		String message="";
		FacesContext   context = FacesContext.getCurrentInstance();
		
		if(location==null || location=="") {
			message="Location is required!";
			FacesMessage facesMessage2= new FacesMessage(message);
	        context.addMessage(location_UI.getClientId(context), facesMessage2);
			
		}
		else if(from==null || till==null) {
			message="Wrong input for date!";
			FacesMessage facesMessage2 = new FacesMessage(message);
	        context.addMessage(no_people_UI.getClientId(context), facesMessage2);
		}
		else if(from.after(till)) {
			message="Wrong input for date!";
			FacesMessage facesMessage2 = new FacesMessage(message);
	        context.addMessage(no_people_UI.getClientId(context), facesMessage2);
		}
		
		else if(no_people<=0) {
			message="Wrong input for number of people";
			FacesMessage facesMessage2 = new FacesMessage(message);
	        context.addMessage(no_people_UI.getClientId(context), facesMessage2);
			
		}
		else if(max_cost<0) {
			message="Wrong value for number of Maximum cost";
			FacesMessage facesMessage2 = new FacesMessage(message);
	        context.addMessage(no_people_UI.getClientId(context), facesMessage2);
			
		}
		else {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
					.getExternalContext().getSession(true);
			session.setAttribute("location",location.toUpperCase());
			session.setAttribute("no_people",no_people);
			session.setAttribute("page",0);
			session.setAttribute("from",from);
			session.setAttribute("till",till);
			
			if(type=="") {
				if(session.getAttribute("type")!=null) 
					session.removeAttribute("type");
			}
			else 
				session.setAttribute("type",type);
			
			if(wifi=="") {
				if(session.getAttribute("wifi")!=null) 
					session.removeAttribute("wifi");
			}
			else 
				session.setAttribute("wifi",wifi);
			
			if(tv=="") {
				if(session.getAttribute("tv")!=null) 
					session.removeAttribute("tv");
			}
			else 
				session.setAttribute("tv",tv);
			
			if(kitchen=="") {
				if(session.getAttribute("kitchen")!=null) 
					session.removeAttribute("kitchen");
			}
			else 
				session.setAttribute("kitchen",kitchen);
			
			if(heating=="") {
				if(session.getAttribute("heating")!=null) 
					session.removeAttribute("heatin");
			}
			else 
				session.setAttribute("heating",heating);
			
			if(elevator=="") {
				if(session.getAttribute("elevator")!=null) 
					session.removeAttribute("elevator");
			}
			else 
				session.setAttribute("elevator",elevator);
			
			if(refrigerator=="") {
				if(session.getAttribute("refrigerator")!=null) 
					session.removeAttribute("refrigerator");
			}
			else 
				session.setAttribute("refrigerator",refrigerator);
			

			if(max_cost==0) {
				if(session.getAttribute("max_cost")!=null) 
					session.removeAttribute("max_cost");
			}
			else 
				session.setAttribute("max_cost",max_cost);
			
			if(parking=="") {
				if(session.getAttribute("parking")!=null) 
					session.removeAttribute("parking");
			}
			else 
				session.setAttribute("parking",parking);
			
			return "results.xhtml?faces-redirect=true";
		}
		return "";
	}

	//vazei sto session se  poia selida pame ama paththei to <
	public String previous_page() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		
		if(session.getAttribute("page")!=null) {
			int page=Integer.parseInt(session.getAttribute("page").toString());
			if(page!=0)
				session.setAttribute("page",page-1);
		}
		return "results.xhtml?faces-redirect=true";
	}
	
	//vazei sto session se  poia selida pame ama paththei to <
	public String next_page() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		
		if(session.getAttribute("page")!=null) {
			int page=Integer.parseInt(session.getAttribute("page").toString());
			session.setAttribute("page",page+1);
		}
		return "results.xhtml?faces-redirect=true";
	}
	
	//upologizei pote einai energo to < ..einai anenergo otan eimaste sthn selida 0
	public boolean disable_previous_btn() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		
		if(session.getAttribute("page")!=null) {
			int page=Integer.parseInt(session.getAttribute("page").toString());
			if(page==0) 
				return true;
			else 
				return false;
		}
		return true;
	}
	
	//upologizei pote einai energo to > ...einai anenergo otan sthn selida pou deixthke emfanizetai to teleutaio apotelesma 
	public boolean disable_next_btn() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		
		if(session.getAttribute("page")!=null) {
			int page=Integer.parseInt(session.getAttribute("page").toString());
			int end=(page+1)*10-1; //<-----10
			if(end < page_results.size()) 
				return false;
			else 
				return true;
		}
		return true;
	}
	
	
	
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public Integer getNo_people() {
		return no_people;
	}

	public void setNo_people(Integer no_people) {
		this.no_people = no_people;
	}




	public UIComponent getFrom_UI() {
		return from_UI;
	}




	public void setFrom_UI(UIComponent from_UI) {
		this.from_UI = from_UI;
	}




	public UIComponent getTill_UI() {
		return till_UI;
	}




	public List<Room> getRecommended_rooms() {
		return recommended_rooms;
	}


	public void setRecommended_rooms(List<Room> recommended_rooms) {
		this.recommended_rooms = recommended_rooms;
	}


	public UIComponent getLocation_UI() {
		return location_UI;
	}




	public void setLocation_UI(UIComponent location_UI) {
		this.location_UI = location_UI;
	}




	public void setTill_UI(UIComponent till_UI) {
		this.till_UI = till_UI;
	}




	public UIComponent getNo_people_UI() {
		return no_people_UI;
	}




	public List<Room> getResults() {
		return results;
	}




	public void setResults(List<Room> results) {
		this.results = results;
	}




	public List<Room> getPage_results() {
		return page_results;
	}



	public void setPage_results(List<Room> page_results) {
		this.page_results = page_results;
	}



	public void setNo_people_UI(UIComponent no_people_UI) {
		this.no_people_UI = no_people_UI;
	}
	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
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



	public boolean isRender_recommendetion() {
		return render_recommendetion;
	}


	public void setRender_recommendetion(boolean render_recommendetion) {
		this.render_recommendetion = render_recommendetion;
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



	public Integer getMax_cost() {
		return max_cost;
	}



	public void setMax_cost(Integer max_cost) {
		this.max_cost = max_cost;
	}



	public UIComponent getMax_cost_UI() {
		return max_cost_UI;
	}



	public void setMax_cost_UI(UIComponent max_cost_UI) {
		this.max_cost_UI = max_cost_UI;
	}



}
