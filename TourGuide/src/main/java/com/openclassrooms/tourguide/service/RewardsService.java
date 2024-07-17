package com.openclassrooms.tourguide.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import com.openclassrooms.tourguide.user.User;
import com.openclassrooms.tourguide.user.UserReward;

/**
 * Service for managing rewards in the TourGuide application
 */
@Service
public class RewardsService {
	private Logger logger = LoggerFactory.getLogger(RewardsService.class);
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

	// proximity in miles
    private int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;
	private int attractionProximityRange = 200;
	private final GpsUtil gpsUtil;
	private final RewardCentral rewardsCentral;

	/**
	 * Constructs a RewardsService with the given dependencies
	 *
	 * @param gpsUtil the GPS utility service
	 * @param rewardCentral the rewards central service
	 */
	public RewardsService(GpsUtil gpsUtil, RewardCentral rewardCentral) {
		this.gpsUtil = gpsUtil;
		this.rewardsCentral = rewardCentral;
	}

	/**
	 * Sets the proximity buffer for calculating rewards
	 *
	 * @param proximityBuffer the proximity buffer in miles
	 */
	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}

	/**
	 * Sets the proximity buffer to its default value
	 */
	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	/**
	 * Calculates rewards for a given user based on their visited locations and nearby attractions
	 *
	 * @param user the user for whom rewards are calculated
	 */
	public void calculateRewards(User user) {
		CopyOnWriteArrayList<VisitedLocation> userLocationsCopy = new CopyOnWriteArrayList<>(user.getVisitedLocations()); // create a copy of list of locations (getVisitedLocations)
		List<Attraction> attractions = gpsUtil.getAttractions();

		for(VisitedLocation visitedLocation : userLocationsCopy) {
			for(Attraction attraction : attractions) {
				if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attraction.attractionName)).count() == 0) {
					if(nearAttraction(visitedLocation, attraction)) {
						user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
					}
				}
			}
		}
	}

	/**
	 * Checks if a location is within proximity of an attraction
	 *
	 * @param attraction the attraction to check
	 * @param location the location to check
	 * @return true if the location is within proximity of the attraction, false otherwise
	 */
	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		return getDistance(attraction, location) > attractionProximityRange ? false : true;
	}

	/**
	 * Checks if a visited location is near an attraction
	 *
	 * @param visitedLocation the visited location to check
	 * @param attraction the attraction to check
	 * @return true if the visited location is near the attraction, false otherwise
	 */
	private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}

	/**
	 * Gets the reward points for a user at a given attraction
	 *
	 * @param attraction the attraction to check
	 * @param user the user to check
	 * @return the reward points for the user at the attraction
	 */
	private int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}

	/**
	 * Calculates the distance between two locations in statute miles
	 *
	 * @param loc1 the first location
	 * @param loc2 the second location
	 * @return the distance between the two locations in statute miles
	 */
	public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
	}

	/**
	 * Gets the reward points for a user at a given attraction
	 *
	 * @param attraction the attraction to check
	 * @param user the user to check
	 * @return the reward points for the user at the attraction
	 */
	public int getPoints(Attraction attraction, User user) {
		return getRewardPoints(attraction, user);
	}

	/**
	 * Checks if a visited location is near an attraction
	 *
	 * @param visitedLocation the visited location to check
	 * @param attraction the attraction to check
	 * @return true if the visited location is near the attraction, false otherwise
	 */
	public boolean isNearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
	}

}
