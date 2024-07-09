package com.openclassrooms.tourguide.service;

import com.openclassrooms.tourguide.DTO.NearAttractionsDTO;
import com.openclassrooms.tourguide.user.User;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;
import rewardCentral.RewardCentral;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NearAttractionsDTOService {

    private final GpsUtil gpsUtil;
    private final RewardsService rewardsService;

    public NearAttractionsDTOService(GpsUtil gpsUtil, RewardsService rewardsService) {
        this.gpsUtil = gpsUtil;
        this.rewardsService = rewardsService;
    }

    public List<NearAttractionsDTO> getNearByAttractions(User user, VisitedLocation visitedLocation, int limit){
        Location userLocation = new Location(visitedLocation.location.latitude, visitedLocation.location.longitude);

        List<NearAttractionsDTO> listDTO = new ArrayList<>();
        List<Attraction> allAttractions = gpsUtil.getAttractions();
        List<Attraction> nearByAttractions = allAttractions.stream().sorted((location1, location2) -> Double.compare(
                rewardsService.getDistance(visitedLocation.location, new Location(location1.latitude, location1.longitude)),
                rewardsService.getDistance(visitedLocation.location, new Location(location2.latitude, location2.longitude))
        )).limit(limit).collect(Collectors.toList());

        for(Attraction attraction : nearByAttractions){
            NearAttractionsDTO nearAttractionsDTO = new NearAttractionsDTO();
            Location locationAttraction = new Location(attraction.latitude, attraction.longitude);
            nearAttractionsDTO.setAttractionName(attraction.attractionName);
            nearAttractionsDTO.setAttractionLocation(new Location(attraction.latitude, attraction.longitude));
            nearAttractionsDTO.setUserLocation(userLocation);
            nearAttractionsDTO.setDistanceBetweenLocationAttractionAndUserLocation(rewardsService.getDistance(locationAttraction, userLocation));
            nearAttractionsDTO.setRewardPoints(rewardsService.getPoints(attraction, user));

            listDTO.add(nearAttractionsDTO);
        }


        return listDTO;
    }

}
