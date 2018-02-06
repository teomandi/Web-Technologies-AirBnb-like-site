package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the chat_room database table.
 * 
 */
@Entity
@Table(name="chat_room")
@NamedQuery(name="ChatRoom.findAll", query="SELECT c FROM ChatRoom c")
public class ChatRoom implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CHAT_ROOM_CHATID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CHAT_ROOM_CHATID_GENERATOR")
	@Column(name="chat_id")
	private int chatId;

	//bi-directional many-to-one association to ChatMessage
	@OneToMany(mappedBy="chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChatMessage> chatMessages;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user1;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="host_id")
	private User user2;

	public ChatRoom() {
	}

	public int getChatId() {
		return this.chatId;
	}

	public void setChatId(int chatId) {
		this.chatId = chatId;
	}

	public List<ChatMessage> getChatMessages() {
		return this.chatMessages;
	}

	public void setChatMessages(List<ChatMessage> chatMessages) {
		this.chatMessages = chatMessages;
	}

	public ChatMessage addChatMessage(ChatMessage chatMessage) {
		getChatMessages().add(chatMessage);
		chatMessage.setChatRoom(this);

		return chatMessage;
	}

	public ChatMessage removeChatMessage(ChatMessage chatMessage) {
		getChatMessages().remove(chatMessage);
		chatMessage.setChatRoom(null);

		return chatMessage;
	}

	public User getUser1() {
		return this.user1;
	}

	public void setUser1(User user1) {
		this.user1 = user1;
	}

	public User getUser2() {
		return this.user2;
	}

	public void setUser2(User user2) {
		this.user2 = user2;
	}

}