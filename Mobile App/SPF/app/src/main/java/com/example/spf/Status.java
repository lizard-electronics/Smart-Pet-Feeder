package com.example.spf;

public class Status {
    int deviceId;
    int foodRefill;
    int waterRefill;
    int timesEat;

    public Status(int deviceId, int foodRefill, int waterRefill, int timesEat) {
        this.deviceId = deviceId;
        this.foodRefill = foodRefill;
        this.waterRefill = waterRefill;
        this.timesEat = timesEat;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public int getFoodRefill() {
        return foodRefill;
    }
    public String getFoodRefillInString() {

        return String.valueOf(foodRefill);
    }

    public String getWaterRefillInString() {

        return String.valueOf(waterRefill);
    }

    public String getTimeEatInString() {

        return String.valueOf(timesEat);
    }

    public int getWaterRefill() {
        return waterRefill;
    }

    public int getTimesEat() {
        return timesEat;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public void setFoodRefill(int foodRefill) {
        this.foodRefill = foodRefill;
    }

    public void setWaterRefill(int waterRefill) {
        this.waterRefill = waterRefill;
    }

    public void setTimesEat(int timesEat) {
        this.timesEat = timesEat;
    }
}
