package com.openclassrooms.tourguide.service;

import com.openclassrooms.tourguide.DTO.NearAttractionsDTO;
import com.openclassrooms.tourguide.user.User;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for handling operations related to nearby attractions
 */
@Service
public class NearAttractionsDTOService {

    private final GpsUtil gpsUtil;
    private final RewardsService rewardsService;
    private final TourGuideService tourGuideService;

    /**
     * Create a NearAttractionsDTOService with the given dependencies
     *
     * @param gpsUtil the GPS utility service
     * @param rewardsService the rewards service
     * @param tourGuideService the tour guide service
     */
    public NearAttractionsDTOService(GpsUtil gpsUtil, RewardsService rewardsService, TourGuideService tourGuideService) {
        this.gpsUtil = gpsUtil;
        this.rewardsService = rewardsService;
        this.tourGuideService = tourGuideService;
    }

    /**
     * Retrieves a list of nearby attractions for a given user
     *
     * @param userName the username of the user
     * @return a list of nearby attractions DTOs
     */
    public List<NearAttractionsDTO> getNearByAttractions(String userName){
        // Get user by username
        User user = tourGuideService.getUser(userName);

        // Get the current location of the user
        VisitedLocation visitedLocation = tourGuideService.getUserLocation(user);
        Location userLocation = new Location(visitedLocation.location.latitude, visitedLocation.location.longitude);

        // List to store the DTOs for nearby attractions
        List<NearAttractionsDTO> listDTO = new ArrayList<>();

        // Get all available attractions from the GPS utility
        List<Attraction> allAttractions = gpsUtil.getAttractions();

        // Find the nearest 5 attractions sorted by distance to the user's current location
        List<Attraction> nearByAttractions = allAttractions.stream().sorted((location1, location2) -> Double.compare(
                rewardsService.getDistance(visitedLocation.location, new Location(location1.latitude, location1.longitude)),
                rewardsService.getDistance(visitedLocation.location, new Location(location2.latitude, location2.longitude))
        )).limit(5).collect(Collectors.toList());

        // Create DTOs for each of the nearest attractions and add them to the list
        for(Attraction attraction : nearByAttractions){
            NearAttractionsDTO nearAttractionsDTO = new NearAttractionsDTO();
            Location locationAttraction = new Location(attraction.latitude, attraction.longitude);
            nearAttractionsDTO.setAttractionName(attraction.attractionName);
            nearAttractionsDTO.setAttractionLocation(locationAttraction);
            nearAttractionsDTO.setUserLocation(userLocation);
            nearAttractionsDTO.setDistanceBetweenLocationAttractionAndUserLocation(rewardsService.getDistance(locationAttraction, userLocation));
            nearAttractionsDTO.setRewardPoints(rewardsService.getPoints(attraction, user));

            listDTO.add(nearAttractionsDTO);
        }

        // Return the list of nearby attractions DTOs
        return listDTO;
    }

}
