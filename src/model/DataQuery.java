package model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;


/* Auto einai ena data access object 
 * pou mesw autou lmbanoumetimes apth 
 * mnhmh dedomenwn
 * */

public class DataQuery {
	EntityManagerFactory emf;
	EntityManager em;
	
	public DataQuery() {
		emf=Persistence.createEntityManagerFactory("Airbnb");
		em=emf.createEntityManager();
		em.getTransaction().begin();
		
	}
	 
	
	//-----------------------------------------------------------------------------------------------
	//checkarei an uparxei user
	public boolean Username_exist(String username) {
		
		try {
			User u= em.createNamedQuery("User.Username_exist", User.class).setParameter("username",username).getSingleResult();
			
			if (u!=null)
				return true;
			else 
				return false;
		}
		catch(NoResultException e) {
			return false;
		}
	}
	
	//kanei persisst
	public void persist(User u) {
		em.persist(u);
		em.getTransaction().commit();
	}
	
	public void persist(Room r) {
		em.persist(r);
		em.getTransaction().commit();
	}
	
	public void persist(UserRoomInteraction uri) {
		em.persist(uri);
		em.getTransaction().commit();
	}
	
	public void persist(ChatRoom chat) {
		em.persist(chat);
		em.getTransaction().commit();
	}
	
	public void persist(ChatMessage msg) {
		em.persist(msg);
		em.getTransaction().commit();
	}
	//omoiws me persist gia periptwseis pou kanoume
	//add se lista se diaforetiko table
	public void merge(ChatRoom chat) {
		em.merge(chat);
		em.getTransaction().commit();
		
	}
	
	public void commit() {
		em.getTransaction().commit();
	}
	
	public void remove(UserRoomInteraction uri) {
		 em.remove(uri);
		 em.getTransaction().commit();
	}
	
	//--------------------------------------------------------------------------------------------------
	//checkarei gia to login
	@SuppressWarnings("unused")
	public boolean login_validation(String username ,String password) {
		try {
			User u= em.createNamedQuery("User.login", User.class).setParameter("username",username)
					.setParameter("password", password).getSingleResult();
			System.out.println("query result-->"+ u.getUsername() );
			if (u!=null)
				return true;
		}
		catch(NoResultException e) {
			return false;
		}
		return false;
		
	}
	
	
	//-----------------------------------------------------------------------------------------------------------------------------------
	public User findUser(String username) {
		try {
			User u= em.createNamedQuery("User.Username_exist", User.class).setParameter("username",username).getSingleResult();
			if (u!=null)
				return u;
			else 
				return null;
		}
		catch(NoResultException e) {
			return null;
		}
	}
	
	public List<User> find_all() {
		TypedQuery<User> query =em.createNamedQuery("User.findAll", User.class);
		List<User> results = query.getResultList();
		return results;
	}
	
	public List<Room> find_all_rooms() {
		TypedQuery<Room> query =em.createNamedQuery("Room.findAll", Room.class);
		List<Room> results = query.getResultList();
		return results;
	}
	
	public List<UserRoomInteraction> find_allURI() {
		TypedQuery<UserRoomInteraction> query =em.createNamedQuery("UserRoomInteraction.findAll", UserRoomInteraction.class);
		List<UserRoomInteraction> results = query.getResultList();
		return results;
	}
	
	
	public  List<Room> find_user_apartments(String username){
		TypedQuery<Room> query =em.createNamedQuery("Room.user_apartments", Room.class).setParameter("username",username);
		if(query!=null) {
			List<Room> results = query.getResultList();
			return results;
		}
		return null;
		
	}
	
	public Room find_apartment(String id) {
		try {
			int room_id=Integer.parseInt(id);
			Room r= em.createNamedQuery("Room.find_apartment", Room.class).setParameter("id",room_id).getSingleResult();
			return r;
		}
		catch(NoResultException e) {
			return null;
		}
	}
	
	public UserRoomInteraction findUserRoomInteraction(int uri_id) {
		try {
			UserRoomInteraction uri= em.createNamedQuery("UserRoomInteraction.findURI", UserRoomInteraction.class).setParameter("uri_id",uri_id).getSingleResult();
			if (uri!=null)
				return uri;
			else 
				return null;
		}
		catch(NoResultException e) {
			return null;
		}
	}
	

	public List<Room> findSimpleResults(String location,int  no_people){
		TypedQuery<Room> query =em.createNamedQuery("Room.SimpleResults", Room.class).setParameter("location",location).setParameter("no_people",no_people);
		if(query!=null) {
			List<Room> results = query.getResultList();
			return results;
		}
		return null;
	}
	
	
	public int findRating(User u, Room r) {
		try {
			
		UserRoomInteraction uri =em.createNamedQuery("UserRoomInteraction.findRating", UserRoomInteraction.class)
				.setParameter("user",u).setParameter("room",r).getSingleResult();
		if(uri!=null)
			return uri.getRating();
		return 0;
		}
		
		catch(NoResultException e) {
			return 0;
		}
		catch(NonUniqueResultException e) {
			TypedQuery<UserRoomInteraction> query =em.createNamedQuery("UserRoomInteraction.findRating", UserRoomInteraction.class)
					.setParameter("user",u).setParameter("room",r);
			List<UserRoomInteraction> results = query.getResultList();
			int max=0;
			for(int i=0;i<results.size();i++)
				if(max<results.get(i).getRating())
					max=results.get(i).getRating();
			return max;
		}
		
		
	}
	
}
