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

	public UUID getUserId() {
		return userId;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setLatestLocationTimestamp(Date latestLocationTimestamp) {
		this.latestLocationTimestamp = latestLocationTimestamp;
	}
	
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

	public List<UserReward> getUserRewards() {
		return userRewards;
	}

	public UserPreferences getUserPreferences() {
		return userPreferences;
	}
	
	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}

	public VisitedLocation getLastVisitedLocation() {
		return visitedLocations.get(visitedLocations.size() - 1);
	}
	
	public void setTripDeals(List<Provider> tripDeals) {
		this.tripDeals = tripDeals;
	}
	
	public List<Provider> getTripDeals() {
		return tripDeals;
	}

}
