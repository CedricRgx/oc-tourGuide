package com.openclassrooms.tourguide.user;

/**
 * Class representing user preferences in the TourGuide application
 */
public class UserPreferences {
	
	private int attractionProximity = Integer.MAX_VALUE;
	private int tripDuration = 1;
	private int ticketQuantity = 1;
	private int numberOfAdults = 1;
	private int numberOfChildren = 0;

	/**
	 * Default constructor for `UserPreferences`
	 */
	public UserPreferences() {
	}

	/**
	 * Sets the proximity distance for attractions
	 *
	 * @param attractionProximity the proximity distance in miles or kilometers
	 */
	public void setAttractionProximity(int attractionProximity) {
		this.attractionProximity = attractionProximity;
	}

	/**
	 * Returns the proximity distance for attractions
	 *
	 * @return the proximity distance in miles or kilometers
	 */
	public int getAttractionProximity() {
		return attractionProximity;
	}

	/**
	 * Returns the duration of the trip in days
	 *
	 * @return the trip duration as an integer
	 */
	public int getTripDuration() {
		return tripDuration;
	}

	/**
	 * Sets the duration of the trip in days
	 *
	 * @param tripDuration the trip duration to set as an integer
	 */
	public void setTripDuration(int tripDuration) {
		this.tripDuration = tripDuration;
	}

	/**
	 * Returns the quantity of tickets required
	 *
	 * @return the number of tickets as an integer
	 */
	public int getTicketQuantity() {
		return ticketQuantity;
	}

	/**
	 * Sets the quantity of tickets required
	 *
	 * @param ticketQuantity the number of tickets to set as an integer
	 */
	public void setTicketQuantity(int ticketQuantity) {
		this.ticketQuantity = ticketQuantity;
	}

	/**
	 * Returns the number of adults for the trip
	 *
	 * @return the number of adults as an integer
	 */
	public int getNumberOfAdults() {
		return numberOfAdults;
	}

	/**
	 * Sets the number of adults for the trip
	 *
	 * @param numberOfAdults the number of adults to set as an integer
	 */
	public void setNumberOfAdults(int numberOfAdults) {
		this.numberOfAdults = numberOfAdults;
	}

	/**
	 * Returns the number of children for the trip
	 *
	 * @return the number of children as an integer
	 */
	public int getNumberOfChildren() {
		return numberOfChildren;
	}

	/**
	 * Sets the number of children for the trip
	 *
	 * @param numberOfChildren the number of children to set as an integer
	 */
	public void setNumberOfChildren(int numberOfChildren) {
		this.numberOfChildren = numberOfChildren;
	}

}
