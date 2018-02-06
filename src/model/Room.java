package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the room database table.
 * 
 */
@Entity
@Table(name="room")
@NamedQueries({
	@NamedQuery(name="Room.findAll", query="SELECT r FROM Room r"),
	@NamedQuery(name="Room.user_apartments", query="SELECT r FROM Room r WHERE r.username= :username"),
	@NamedQuery(name="Room.find_apartment", query="SELECT r FROM Room r WHERE r.roomId =:id"),
	@NamedQuery(name="Room.SimpleResults", query="SELECT r FROM Room r WHERE r.location = :location and  r.noPeople >= :no_people")
})

public class Room implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ROOM_ROOMID_GENERATOR" )
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ROOM_ROOMID_GENERATOR")
	@Column(name="room_id")
	private int roomId;

	private String address;

	private int area;

	private int cpp;

	private String date;

	private String description;

	private String elevator;

	private String events;

	private String heating;

	private String info;

	private String kitchen;

	private double lat;

	private double ling;

	private String livingroom;

	private String location;

	@Column(name="m_charge")
	private int mCharge;

	@Column(name="m_days")
	private int mDays;

	@Column(name="main_pic")
	private String mainPic;

	@Column(name="no_baths")
	private int noBaths;

	@Column(name="no_beds")
	private int noBeds;

	@Column(name="no_people")
	private int noPeople;

	@Column(name="no_ratings")
	private int noRatings;

	@Column(name="no_rooms")
	private int noRooms;

	private String parking;

	private String pets;

	@Column(name="pictures_folder")
	private String picturesFolder;

	private int rating;

	@Column(name="rating_sum")
	private int ratingSum;

	private String refrigerator;

	@Column(name="room_name")
	private String roomName;

	private String smoking;

	private String tv;

	private String type;

	private String username;

	private String wifi;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;

	//bi-directional many-to-one association to UserRoomInteraction
	@OneToMany(mappedBy="room")
	private List<UserRoomInteraction> userRoomInteractions;

	public Room() {
	}

	public int getRoomId() {
		return this.roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getArea() {
		return this.area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public int getCpp() {
		return this.cpp;
	}

	public void setCpp(int cpp) {
		this.cpp = cpp;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getElevator() {
		return this.elevator;
	}

	public void setElevator(String elevator) {
		this.elevator = elevator;
	}

	public String getEvents() {
		return this.events;
	}

	public void setEvents(String events) {
		this.events = events;
	}

	public String getHeating() {
		return this.heating;
	}

	public void setHeating(String heating) {
		this.heating = heating;
	}

	public String getInfo() {
		return this.info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getKitchen() {
		return this.kitchen;
	}

	public void setKitchen(String kitchen) {
		this.kitchen = kitchen;
	}

	public double getLat() {
		return this.lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLing() {
		return this.ling;
	}

	public void setLing(double ling) {
		this.ling = ling;
	}

	public String getLivingroom() {
		return this.livingroom;
	}

	public void setLivingroom(String livingroom) {
		this.livingroom = livingroom;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getMCharge() {
		return this.mCharge;
	}

	public void setMCharge(int mCharge) {
		this.mCharge = mCharge;
	}

	public int getMDays() {
		return this.mDays;
	}

	public void setMDays(int mDays) {
		this.mDays = mDays;
	}

	public String getMainPic() {
		return this.mainPic;
	}

	public void setMainPic(String mainPic) {
		this.mainPic = mainPic;
	}

	public int getNoBaths() {
		return this.noBaths;
	}

	public void setNoBaths(int noBaths) {
		this.noBaths = noBaths;
	}

	public int getNoBeds() {
		return this.noBeds;
	}

	public void setNoBeds(int noBeds) {
		this.noBeds = noBeds;
	}

	public int getNoPeople() {
		return this.noPeople;
	}

	public void setNoPeople(int noPeople) {
		this.noPeople = noPeople;
	}

	public int getNoRatings() {
		return this.noRatings;
	}

	public void setNoRatings(int noRatings) {
		this.noRatings = noRatings;
	}

	public int getNoRooms() {
		return this.noRooms;
	}

	public void setNoRooms(int noRooms) {
		this.noRooms = noRooms;
	}

	public String getParking() {
		return this.parking;
	}

	public void setParking(String parking) {
		this.parking = parking;
	}

	public String getPets() {
		return this.pets;
	}

	public void setPets(String pets) {
		this.pets = pets;
	}

	public String getPicturesFolder() {
		return this.picturesFolder;
	}

	public void setPicturesFolder(String picturesFolder) {
		this.picturesFolder = picturesFolder;
	}

	public int getRating() {
		return this.rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getRatingSum() {
		return this.ratingSum;
	}

	public void setRatingSum(int ratingSum) {
		this.ratingSum = ratingSum;
	}

	public String getRefrigerator() {
		return this.refrigerator;
	}

	public void setRefrigerator(String refrigerator) {
		this.refrigerator = refrigerator;
	}

	public String getRoomName() {
		return this.roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getSmoking() {
		return this.smoking;
	}

	public void setSmoking(String smoking) {
		this.smoking = smoking;
	}

	public String getTv() {
		return this.tv;
	}

	public void setTv(String tv) {
		this.tv = tv;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getWifi() {
		return this.wifi;
	}

	public void setWifi(String wifi) {
		this.wifi = wifi;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<UserRoomInteraction> getUserRoomInteractions() {
		return this.userRoomInteractions;
	}

	public void setUserRoomInteractions(List<UserRoomInteraction> userRoomInteractions) {
		this.userRoomInteractions = userRoomInteractions;
	}

	public UserRoomInteraction addUserRoomInteraction(UserRoomInteraction userRoomInteraction) {
		getUserRoomInteractions().add(userRoomInteraction);
		userRoomInteraction.setRoom(this);

		return userRoomInteraction;
	}

	public UserRoomInteraction removeUserRoomInteraction(UserRoomInteraction userRoomInteraction) {
		getUserRoomInteractions().remove(userRoomInteraction);
		userRoomInteraction.setRoom(null);

		return userRoomInteraction;
	}

}