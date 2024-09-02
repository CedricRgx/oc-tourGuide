package com.openclassrooms.tourguide.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import gpsUtil.location.VisitedLocation;
import tripPricer.Provider;

/**
 * Class representing a user in the TourGuide application
 */
public class User {
	private final UUID userId;
	private final String userName;
	private String phoneNumber;
	private String emailAddress;
	private Date latestLocationTimestamp;
	private List<VisitedLocation> visitedLocations = new ArrayList<>();
	private List<UserReward> userRewards = new ArrayList<>();
	private UserPreferences userPreferences = new UserPreferences();
	private List<Provider> tripDeals = new ArrayList<>();

	/**
	 * Constructs a User with the given details
	 *
	 * @param userId the unique identifier for the user
	 * @param userName the username of the user
	 * @param phoneNumber the phone number of the user
	 * @param emailAddress the email address of the user
	 */
	public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
		this.userId = userId;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}

	/**
	 * Returns the unique identifier (UUID) of the user
	 *
	 * @return the `UUID` representing the user's unique identifier
	 */
	public UUID getUserId() {
		return userId;
	}

	/**
	 * Returns the username of the user
	 *
	 * @return the `String` representing the user's username
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the phone number for the user
	 *
	 * @param phoneNumber the `String` representing the user's phone number
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Returns the phone number of the user
	 *
	 * @return the `String` representing the user's phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Sets the email address for the user
	 *
	 * @param emailAddress the `String` representing the user's email address
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * Returns the email address of the user
	 *
	 * @return the `String` representing the user's email address
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * Sets the timestamp for the user's latest location
	 *
	 * @param latestLocationTimestamp the `Date` representing the timestamp of the user's most recent location
	 */
	public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
		this.latestLocationTimestamp = latestLocationTimestamp;
	}

	/**
	 * Returns the timestamp of the user's latest location
	 *
	 * @return the `Date` representing the timestamp of the user's most recent location
	 */
	public Date getLatestLocationTimestamp() {
		return latestLocationTimestamp;
	}

	/**
	 * Adds a visited location to the user's list of visited locations
	 *
	 * @param visitedLocation the visited location to add
	 */
	public void addToVisitedLocations(VisitedLocation visitedLocation) {
		visitedLocations.add(visitedLocation);
	}

	/**
	 * Gets the list of visited locations of the user
	 *
	 * @return the list of visited locations
	 */
	public List<VisitedLocation> getVisitedLocations() {
		return visitedLocations;
	}

	/**
	 * Clears the list of visited locations
	 */
	public void clearVisitedLocations() {
		visitedLocations.clear();
	}

	/**
	 * Adds a reward to the user's list of rewards
	 *
	 * @param userReward the reward to add
	 */
	public void addUserReward(UserReward userReward) {
		if(userRewards.stream().filter(r -> !r.attraction.attractionName.equals(userReward.attraction)).count() == 0) {
			userRewards.add(userReward);
		}
	}

	/**
	 * Returns the list of rewards earned by the user
	 *
	 * @return a `List<UserReward>` containing the user's rewards
	 */
	public List<UserReward> getUserRewards() {
		return userRewards;
	}

	/**
	 * Returns the user's preferences
	 *
	 * @return a `UserPreferences` object representing the user's preferences
	 */
	public UserPreferences getUserPreferences() {
		return userPreferences;
	}

	/**
	 * Sets the user's preferences
	 *
	 * @param userPreferences the `UserPreferences` object representing the user's preferences
	 */
	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	/**
	 * Returns the most recent location visited by the user
	 *
	 * @return the `VisitedLocation` object representing the user's last visited location
	 * @throws IndexOutOfBoundsException if the user has no visited locations
	 */
	public VisitedLocation getLastVisitedLocation() {
		return visitedLocations.get(visitedLocations.size() - 1);
	}

	/**
	 * Sets the list of trip deals available to the user
	 *
	 * @param tripDeals the `List<Provider>` representing the trip deals to be set for the user
	 */
	public void setTripDeals(List<Provider> tripDeals) {
		this.tripDeals = tripDeals;
	}

	/**
	 * Returns the list of trip deals available to the user
	 *
	 * @return a `List<Provider>` representing the user's trip deals
	 */
	public List<Provider> getTripDeals() {
		return tripDeals;
	}

}
