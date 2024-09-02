package com.openclassrooms.tourguide.user;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

/**
 * Class representing some user rewards in the TourGuide application
 */
public class UserReward {

	public final VisitedLocation visitedLocation;
	public final Attraction attraction;
	private int rewardPoints;

	/**
	 * Constructs a UserReward with the given visited location, attraction, and reward points
	 *
	 * @param visitedLocation the visited location associated with the reward
	 * @param attraction the attraction associated with the reward
	 * @param rewardPoints the number of reward points
	 */
	public UserReward(VisitedLocation visitedLocation, Attraction attraction, int rewardPoints) {
		this.visitedLocation = visitedLocation;
		this.attraction = attraction;
		this.rewardPoints = rewardPoints;
	}

	/**
	 * Constructs a `UserReward` object with the specified visited location and attraction
	 *
	 * @param visitedLocation the `VisitedLocation` where the user earned the reward
	 * @param attraction the `Attraction` associated with the reward
	 */
	public UserReward(VisitedLocation visitedLocation, Attraction attraction) {
		this.visitedLocation = visitedLocation;
		this.attraction = attraction;
	}

	/**
	 * Sets the number of reward points for the user
	 *
	 * @param rewardPoints the number of reward points to set as an integer
	 */
	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	/**
	 * Returns the number of reward points earned by the user
	 *
	 * @return the number of reward points as an integer
	 */
	public int getRewardPoints() {
		return rewardPoints;
	}
	
}
