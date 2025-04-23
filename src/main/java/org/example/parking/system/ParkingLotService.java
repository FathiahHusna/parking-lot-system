package org.example.parking.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLotService {

    public ParkingLotService() {
    }

    //#1 create parking lot
    public String createParkingLot(int numFloor, int numSlot, ParkInfo parkInfo){
        Map<Integer, Boolean[]> availableMap = new HashMap<>();
        for(int i=1; i<=numFloor; i++){
            Boolean[] arrSlot = new Boolean[numSlot];
            availableMap.put(i, arrSlot);
        }
        parkInfo.setAvailable(availableMap);
        System.out.println(parkInfo);

        return "Created parking lot with " + numFloor + " floors and " + numSlot + " slots per floor";
    }

    //#2 park vehicle park_vehicle CAR KA-01-DB-1234 black
    public String parkVehicle(ParkInfo parkInfo, List<CarInfo> carInfoList, CarInfo carInfo){
        Map<Integer, Boolean[]> availableMap = parkInfo.getAvailable();
        if(availableMap!=null && !availableMap.isEmpty()){
            for (Map.Entry<Integer, Boolean[]> entry : availableMap.entrySet()) {
                boolean park = false;
                Boolean[] arrSlot = entry.getValue();
                for(int i=0; i< arrSlot.length; i++){
                    if(arrSlot[i]==null || !arrSlot[i]){
                        if("truck".equalsIgnoreCase(carInfo.getType()) && (i==0)){
                            park = true;
                        }
                        if("bike".equalsIgnoreCase(carInfo.getType()) && (i==1 || i==2)){
                            park = true;
                        }
                        if("car".equalsIgnoreCase(carInfo.getType()) && i>2){
                            park = true;
                        }

                        if(park){
                            arrSlot[i] = Boolean.TRUE;
                            carInfo.setFloor(entry.getKey());
                            carInfo.setSlot(i+1);

                            StringBuilder sb = new StringBuilder();
                            sb.append(parkInfo.getParkingId()).append("_").append(carInfo.getFloor()).append("_").append(carInfo.getSlot());
                            String ticketId = sb.toString();
                            carInfo.setTicketId(ticketId);
                            carInfoList.add(carInfo);


                            return "Parked vehicle. Ticket ID: " + ticketId;
                        }
                    }
                }
            }

        }
        return "Parking Lot Full";
    }

    //#3 unpark vehicle
    public String unparkVehicle(String ticketId, List<CarInfo> carInfoList, ParkInfo parkInfo){
        String[] arr = ticketId.split("_");
        if(arr!=null){
            int floor = Integer.parseInt(arr[1]);
            int level = Integer.parseInt(arr[2]);

            for(CarInfo carInfo:carInfoList){
                if(ticketId.equalsIgnoreCase(carInfo.getTicketId())){
                    carInfo.setFloor(0);
                    carInfo.setSlot(0);
                    carInfo.setTicketId(null);

                    Map<Integer, Boolean[]> availableMap = parkInfo.getAvailable();
                    Boolean[] arrSlots = availableMap.get(floor);
                    arrSlots[level] = Boolean.FALSE;
                    availableMap.put(floor, arrSlots);

                    return "Unparked vehicle with Registration Number: " + carInfo.getRegNo() + " and Color: " + carInfo.getColour();
                }
            }
        }
        return "Invalid Ticket";
    }

    public void display(String vehichleType, ParkInfo parkInfo, String displayType){
        Map<Integer, Boolean[]> availableMap = parkInfo.getAvailable();

        if(availableMap!=null && !availableMap.isEmpty()) {
            for (Map.Entry<Integer, Boolean[]> entry : availableMap.entrySet()) {
                Boolean[] arrSlots = entry.getValue();
                int floor = entry.getKey();

                if("free_count".equals(displayType)){
                    int countSlot = freeCount(arrSlots, vehichleType);
                    System.out.println("No. of free slots for " + vehichleType + " on Floor " + floor + " : " + countSlot);

                }
                if("free_slots".equals(displayType)){
                    freeSlot(arrSlots, vehichleType, floor);
                }
                if("occupied_slots".equals(displayType)){
                    occupiedSlot(arrSlots, vehichleType, floor);
                }
            }
        }
    }

    //#4 display free count
    private int freeCount(Boolean[] arrSlots, String vehichleType){
        int countSlot = 0;
        for(int i=0; i< arrSlots.length; i++){
            if(arrSlots[i]==null || !arrSlots[i]){
                if("truck".equalsIgnoreCase(vehichleType) && (i==0)){
                    countSlot++;
                }
                if("bike".equalsIgnoreCase(vehichleType) && (i==1 || i==2)){
                    countSlot++;
                }
                if("car".equalsIgnoreCase(vehichleType) && i>2){
                    countSlot++;
                }
            }
        }
        return countSlot;
    }

    //#5 display free slots
    private void freeSlot(Boolean[] arrSlots, String vehichleType, int floor){
        for(int i=0; i< arrSlots.length; i++){
            boolean free = false;
            if(arrSlots[i]==null || !arrSlots[i]){
                if("truck".equalsIgnoreCase(vehichleType) && (i==0)){
                    free = true;
                }
                if("bike".equalsIgnoreCase(vehichleType) && (i==1 || i==2)){
                    free = true;
                }
                if("car".equalsIgnoreCase(vehichleType) && i>2){
                    free = true;
                }
            }
            int slot = i+1;
            if(free){
                System.out.println("Free slots for " + vehichleType + " on Floor " + floor + " : " + slot);
            }
        }
    }

    //#6 display occupied slots
    private void occupiedSlot(Boolean[] arrSlots, String vehichleType, int floor){
        for(int i=0; i< arrSlots.length; i++){
            boolean occupy = false;
            if(arrSlots[i]!=null && arrSlots[i]){
                if("truck".equalsIgnoreCase(vehichleType) && (i==0)){
                    occupy = true;
                }
                if("bike".equalsIgnoreCase(vehichleType) && (i==1 || i==2)){
                    occupy = true;
                }
                if("car".equalsIgnoreCase(vehichleType) && i>2){
                    occupy = true;
                }
            }
            int slot = i+1;
            if(occupy){
                System.out.println("Free slots for " + vehichleType + " on Floor " + floor + " : " + slot);
            }
        }
    }
}
