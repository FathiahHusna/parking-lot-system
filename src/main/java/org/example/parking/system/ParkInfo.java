package org.example.parking.system;

import java.util.Arrays;
import java.util.Map;

public class ParkInfo {

    private String parkingId;
    private Map<Integer, Boolean[]> available;

    public Map<Integer, Boolean[]> getAvailable() {
        return available;
    }

    public void setAvailable(Map<Integer, Boolean[]> available) {
        this.available = available;
    }

    public String getParkingId() {
        return parkingId;
    }

    public void setParkingId(String parkingId) {
        this.parkingId = parkingId;
    }

    public ParkInfo() {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Boolean[]> entry : available.entrySet()) {
            Integer key = entry.getKey();
            Boolean[] value = entry.getValue();

            sb.append("Key: " ).append(key).append(" => Values: ").append(Arrays.toString(value));
        }
        return "ParkInfo{" +
                "available=" + sb.toString() +
                '}';
    }
}
