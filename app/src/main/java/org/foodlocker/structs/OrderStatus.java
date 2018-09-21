package org.foodlocker.structs;

public enum OrderStatus {

    OPEN("open"),
    ACCEPTED("accepted"),
    COMPLETED("completed");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
