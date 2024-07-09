package com.openclassrooms.tourguide.DTO;

import gpsUtil.location.Location;

public class NearAttractionsDTO {

    private String attractionName;
    private Location attractionLocation;
    private Location userLocation;
    double distanceBetweenLocationAttractionAndUserLocation;
    int rewardPoints;

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
