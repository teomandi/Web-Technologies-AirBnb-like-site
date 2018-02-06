package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the user_room_interaction database table.
 * 
 */
@Entity
@Table(name="user_room_interaction")
@NamedQueries({
	@NamedQuery(name="UserRoomInteraction.findAll", query="SELECT u FROM UserRoomInteraction u"),
	@NamedQuery(name="UserRoomInteraction.findURI", query="SELECT uri FROM UserRoomInteraction uri WHERE uri.interactionId= :uri_id"),
	@NamedQuery(name="UserRoomInteraction.findRating", query="SELECT uri FROM UserRoomInteraction uri  WHERE uri.room= :room and uri.user= :user ")
})
public class UserRoomInteraction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="USER_ROOM_INTERACTION_INTERACTIONID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_ROOM_INTERACTION_INTERACTIONID_GENERATOR")
	@Column(name="interaction_id")
	private int interactionId;

	@Column(name="booked_date")
	private String bookedDate;

	private String comment;

	@Column(name="comment_date")
	private String commentDate;

	private int rating;

	//bi-directional many-to-one association to Room
	@ManyToOne
	@JoinColumn(name="room_id")
	private Room room;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	public UserRoomInteraction() {
	}

	public int getInteractionId() {
		return this.interactionId;
	}

	public void setInteractionId(int interactionId) {
		this.interactionId = interactionId;
	}

	public String getBookedDate() {
		return this.bookedDate;
	}

	public void setBookedDate(String bookedDate) {
		this.bookedDate = bookedDate;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCommentDate() {
		return this.commentDate;
	}

	public void setCommentDate(String commentDate) {
		this.commentDate = commentDate;
	}

	public int getRating() {
		return this.rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Room getRoom() {
		return this.room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}