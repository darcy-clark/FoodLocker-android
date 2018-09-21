package org.foodlocker.structs;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {

    private String box;
    private String lockerNumber;
    private String lockerCombo;
    private String user;
    private String volunteer;
    private String uuid;
    private String deliveryTime;
    private OrderStatus status;
    private long timestamp;
    private List<String> dietRestrictions;

    public Order() {

    }

    public Order(String box, String lockerNumber, String lockerCombo, String user, long timestamp,
                 List<String> dietRestrictions) {
        this.box = box;
        this.lockerNumber = lockerNumber;
        this.lockerCombo = lockerCombo;
        this.user = user;
        this.timestamp = timestamp;
        this.dietRestrictions = dietRestrictions;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public String getLockerNumber() {
        return lockerNumber;
    }

    public void setLockerNumber(String lockerNumber) {
        this.lockerNumber = lockerNumber;
    }

    public String getLockerCombo() {
        return lockerCombo;
    }

    public void setLockerCombo(String lockerCombo) {
        this.lockerCombo = lockerCombo;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public String getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(String volunteer) {
        this.volunteer = volunteer;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getDietRestrictions() {
        return dietRestrictions;
    }

    public void setDietRestrictions(List<String> dietRestrictions) {
        this.dietRestrictions = dietRestrictions;
    }
}
