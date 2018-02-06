package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the chat_message database table.
 * 
 */
@Entity
@Table(name="chat_message")
@NamedQuery(name="ChatMessage.findAll", query="SELECT c FROM ChatMessage c")
public class ChatMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="CHAT_MESSAGE_MESSAGEID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CHAT_MESSAGE_MESSAGEID_GENERATOR")
	@Column(name="message_id")
	private int messageId;

	private String message;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="senter_id")
	private User user;

	//bi-directional many-to-one association to ChatRoom
	@ManyToOne
	@JoinColumn(name="chat_id")
	private ChatRoom chatRoom;

	public ChatMessage() {
	}

	public int getMessageId() {
		return this.messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ChatRoom getChatRoom() {
		return this.chatRoom;
	}

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}

}