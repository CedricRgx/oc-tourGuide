package com.openclassrooms.tourguide;

import java.util.List;

import com.jsoniter.output.JsonStream;
import com.openclassrooms.tourguide.DTO.NearAttractionsDTO;
import com.openclassrooms.tourguide.service.NearAttractionsDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import gpsUtil.location.VisitedLocation;

import com.openclassrooms.tourguide.service.TourGuideService;
import com.openclassrooms.tourguide.user.User;
import com.openclassrooms.tourguide.user.UserReward;

import tripPricer.Provider;

/**
 * TourGuideController for handling TourGuide application requests
 */
@RestController
public class TourGuideController {

    @Autowired
    TourGuideService tourGuideService;

    @Autowired
    NearAttractionsDTOService nearAttractionsDTOService;

    /**
     * Endpoint to return a greeting message
     *
     * @return A greeting string.
     */
    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    /**
     * Endpoint to get the location of a user
     *
     * @param userName the username of the user
     * @return the visited location of the user
     */
    @RequestMapping("/getLocation") 
    public VisitedLocation getLocation(@RequestParam String userName) {
    	return tourGuideService.getUserLocation(getUser(userName));
    }

    /**
     * Endpoint to get nearby attractions for a user
     *
     * @param userName the username of the user
     * @return JSON serialized list of nearby attractions
     */
    @RequestMapping("/getNearbyAttractions")
    public String getNearbyAttractions(@RequestParam String userName) {
        List<NearAttractionsDTO> nearbyAttractions = nearAttractionsDTOService.getNearByAttractions(userName);
        return JsonStream.serialize(nearbyAttractions);
    }

    /**
     * Endpoint to get rewards for a user
     *
     * @param userName the username of the user
     * @return list of user rewards
     */
    @RequestMapping("/getRewards") 
    public List<UserReward> getRewards(@RequestParam String userName) {
    	return tourGuideService.getUserRewards(getUser(userName));
    }

    /**
     * Endpoint to get trip deals for a user
     *
     * @param userName the username of the user
     * @return list of trip deals
     */
    @RequestMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
    	return tourGuideService.getTripDeals(getUser(userName));
    }

    /**
     * Method to get a user by username
     *
     * @param userName the username of the user
     * @return the user object
     */
    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }
   

}