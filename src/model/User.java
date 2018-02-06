package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="user")
@NamedQueries({
	@NamedQuery(name="User.findAll", query="SELECT u FROM User u"),
	@NamedQuery(name="User.Username_exist", query="SELECT u FROM User u WHERE u.username= :username"),
	@NamedQuery(name="User.login",query="SELECT u FROM User u WHERE u.username=:username and u.password=:password")
})
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USER_USERID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_USERID_GENERATOR")
	@Column(name="user_id")
	private int userId;

	@Column(name="a_approve")
	private String aApprove;

	private String email;

	private String name;

	private String password;

	private String phone;

	@Column(name="profile_pic")
	private String profilePic;

	private String role;

	private String surname;

	private String username;

	//bi-directional many-to-one association to ChatMessage
	@OneToMany(mappedBy="user")
	private List<ChatMessage> chatMessages;

	//bi-directional many-to-one association to ChatRoom
	@OneToMany(mappedBy="user1")
	private List<ChatRoom> chatRooms1;

	//bi-directional many-to-one association to ChatRoom
	@OneToMany(mappedBy="user2")
	private List<ChatRoom> chatRooms2;

	//bi-directional many-to-one association to Room
	@OneToMany(mappedBy="user")
	private List<Room> rooms;

	//bi-directional many-to-one association to UserRoomInteraction
	@OneToMany(mappedBy="user")
	private List<UserRoomInteraction> userRoomInteractions;

	public User() {
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAApprove() {
		return this.aApprove;
	}

	public void setAApprove(String aApprove) {
		this.aApprove = aApprove;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getProfilePic() {
		return this.profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<ChatMessage> getChatMessages() {
		return this.chatMessages;
	}

	public void setChatMessages(List<ChatMessage> chatMessages) {
		this.chatMessages = chatMessages;
	}

	public ChatMessage addChatMessage(ChatMessage chatMessage) {
		getChatMessages().add(chatMessage);
		chatMessage.setUser(this);

		return chatMessage;
	}

	public ChatMessage removeChatMessage(ChatMessage chatMessage) {
		getChatMessages().remove(chatMessage);
		chatMessage.setUser(null);

		return chatMessage;
	}

	public List<ChatRoom> getChatRooms1() {
		return this.chatRooms1;
	}

	public void setChatRooms1(List<ChatRoom> chatRooms1) {
		this.chatRooms1 = chatRooms1;
	}

	public ChatRoom addChatRooms1(ChatRoom chatRooms1) {
		getChatRooms1().add(chatRooms1);
		chatRooms1.setUser1(this);

		return chatRooms1;
	}

	public ChatRoom removeChatRooms1(ChatRoom chatRooms1) {
		getChatRooms1().remove(chatRooms1);
		chatRooms1.setUser1(null);

		return chatRooms1;
	}

	public List<ChatRoom> getChatRooms2() {
		return this.chatRooms2;
	}

	public void setChatRooms2(List<ChatRoom> chatRooms2) {
		this.chatRooms2 = chatRooms2;
	}

	public ChatRoom addChatRooms2(ChatRoom chatRooms2) {
		getChatRooms2().add(chatRooms2);
		chatRooms2.setUser2(this);

		return chatRooms2;
	}

	public ChatRoom removeChatRooms2(ChatRoom chatRooms2) {
		getChatRooms2().remove(chatRooms2);
		chatRooms2.setUser2(null);

		return chatRooms2;
	}

	public List<Room> getRooms() {
		return this.rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public Room addRoom(Room room) {
		getRooms().add(room);
		room.setUser(this);

		return room;
	}

	public Room removeRoom(Room room) {
		getRooms().remove(room);
		room.setUser(null);

		return room;
	}

	public List<UserRoomInteraction> getUserRoomInteractions() {
		return this.userRoomInteractions;
	}

	public void setUserRoomInteractions(List<UserRoomInteraction> userRoomInteractions) {
		this.userRoomInteractions = userRoomInteractions;
	}

	public UserRoomInteraction addUserRoomInteraction(UserRoomInteraction userRoomInteraction) {
		getUserRoomInteractions().add(userRoomInteraction);
		userRoomInteraction.setUser(this);

		return userRoomInteraction;
	}

	public UserRoomInteraction removeUserRoomInteraction(UserRoomInteraction userRoomInteraction) {
		getUserRoomInteractions().remove(userRoomInteraction);
		userRoomInteraction.setUser(null);

		return userRoomInteraction;
	}

}