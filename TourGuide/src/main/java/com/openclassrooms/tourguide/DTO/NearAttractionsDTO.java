package com.openclassrooms.tourguide.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gpsUtil.location.Location;

/**
 * The NearAttractionsDTO DTO is for communicating information to the /getNearbyAttractions endpoint
 */
@JsonPropertyOrder({"attractionName", "attractionLocation", "userLocation", "distanceBetweenLocationAttractionAndUserLocation", "rewardPoints"})
public class NearAttractionsDTO {

    @JsonProperty("attractionName")
    private String attractionName;

    @JsonProperty("attractionLocation")
    private Location attractionLocation;

    @JsonProperty("userLocation")
    private Location userLocation;

    @JsonProperty("distanceBetweenLocationAttractionAndUserLocation")
    private double distanceBetweenLocationAttractionAndUserLocation;

    @JsonProperty("rewardPoints")
    private int rewardPoints;

    public String getAttractionName() {
        return attractionName;
    }

    public void setAttractionName(String attractionName) {
        this.attractionName = attractionName;
    }

    public Location getAttractionLocation() {
        return attractionLocation;
    }

    public void setAttractionLocation(Location attractionLocation) {
        this.attractionLocation = attractionLocation;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    public double getDistanceBetweenLocationAttractionAndUserLocation() {
        return distanceBetweenLocationAttractionAndUserLocation;
    }

    public void setDistanceBetweenLocationAttractionAndUserLocation(double distanceBetweenLocationAttractionAndUserLocation) {
        this.distanceBetweenLocationAttractionAndUserLocation = distanceBetweenLocationAttractionAndUserLocation;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
}
