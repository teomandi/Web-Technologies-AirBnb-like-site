package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;


@ManagedBean(name="chat")
public class ChatBean {
	private final DataQuery query=new DataQuery();
	

	
	
	private User sender;
	private User reciever;

	private ChatRoom chat;
	List<ChatRoom> chats;
	private String message;
	
	
	/*arxikopoiei to chat..pairnei to sender hte apo session hte apoparam
	 * meta vriskei olous autous pou exei epikoinwnhsei (exei steilei kai tou exoun steilei)
	 * (afairountai apo th lista oi koines) 
	 * an uparxei reciever pairnei ta mhnumata apto chat tous
	 * */
	
	@PostConstruct
	public void init() {  

		java.util.Map<String, String> params =FacesContext.getCurrentInstance().
                getExternalContext().getRequestParameterMap();
		
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
 				.getExternalContext().getSession(true);
		
		
		sender=null;
		reciever=null;
		
		if(params.get("sender")!=null) {
			sender=query.findUser(params.get("sender"));
			session.setAttribute("sender",sender);
		}
		else if(session.getAttribute("sender")!=null ) 
			sender=(User)session.getAttribute("sender");
		
		chats=new ArrayList<ChatRoom>();
		chats.addAll(sender.getChatRooms1());
		chats.addAll(sender.getChatRooms2());
		
		for (Iterator<ChatRoom> iterator = chats.iterator(); iterator.hasNext();) { 
			ChatRoom temp_chat=iterator.next();
			if(temp_chat.getUser1().getUsername().equals(sender.getUsername()) && temp_chat.getUser2().getUsername().equals(sender.getUsername()) )
				iterator.remove();

		}
		
		if(params.get("reciever")!=null) {
			reciever=query.findUser(params.get("reciever"));
			session.setAttribute("reciever",reciever);
		}
		else if(session.getAttribute("reciever")!=null) 
			reciever=(User)session.getAttribute("reciever");
		
		if(sender!=null && reciever!=null) {
			
			for(ChatRoom temp_chat : chats) {
				
				System.out.println(temp_chat.getUser1().getUsername()+"--"+temp_chat.getUser2().getUsername()+"--"+temp_chat.getChatId());
				//to chat tous einai auto pou hte o reciever htan o user1 hte o sender
				if((temp_chat.getUser1().getUsername().equals(reciever.getUsername()) && temp_chat.getUser2().getUsername().equals(sender.getUsername()))
						||( temp_chat.getUser2().getUsername().equals(reciever.getUsername()) && temp_chat.getUser1().getUsername().equals(sender.getUsername()))) {
					chat=temp_chat;
					for(ChatMessage msg2 :chat.getChatMessages()) 
						System.out.println("0--->"+msg2.getMessage());	
					break;
				}
			}
		
			
			}
		else 
			System.out.println("Error: no sender && reciever");	
	}
	
	
	//stelnei to mhnuma...ousiasth to dhmiourgei kai to vazei sto entity
	public String sent_message() {
		if(message!="") {
			System.out.println("chat_id:"+chat.getChatId());
			for(ChatMessage msg2 :chat.getChatMessages()) {
				System.out.println("0--->"+msg2.getMessage());	
			}
		
			ChatMessage msg=new ChatMessage();
			msg.setChatRoom(chat);
			msg.setMessage(message);
			msg.setUser(sender);
			chat.addChatMessage(msg);
			
			query.merge(chat);
		}
		return "";
	}
	
	//mono an uparxei kai reciever ginetai renrer
	public boolean render_messages() {
		if(reciever!=null && sender!=null)
			return true;
		return false;
		
	}
	
	//mono autos ston opoion steilane tha borei na diagrapsei ta mhnumata
	public boolean render_deleteMsg() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
 				.getExternalContext().getSession(true);
		if(session.getAttribute("username").toString().equals(chat.getUser1().getUsername()))
				return true;
		else return false;
	}
	
	//ta bgazei apth lista
	public String deleteMsg(String str_index) {
		System.out.println("Delete-->"+(chat.getChatMessages().get(Integer.parseInt(str_index)).getMessage()));
		chat.removeChatMessage(chat.getChatMessages().get(Integer.parseInt(str_index)));
		query.merge(chat);
		return "";
	}
	
	
	//epistrefei to onoma ths epafhs..to onoma pou den einai tou online user
	 public String contact_name(String str_index) { 
		 ChatRoom chat=chats.get(Integer.parseInt(str_index));
		 if(chat.getUser1().getUsername().equals(sender.getUsername()))
			 return chat.getUser2().getUsername();
		 else 
			 return chat.getUser1().getUsername();
	 }



	public ChatRoom getChat() {
		return chat;
	}


	public List<ChatRoom> getChats() {
		return chats;
	}



	public void setChats(List<ChatRoom> chats) {
		this.chats = chats;
	}



	public void setChat(ChatRoom chat) {
		this.chat = chat;
	}


	

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReciever() {
		return reciever;
	}

	public void setReciever(User reciever) {
		this.reciever = reciever;
	}



}
