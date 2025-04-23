package org.example.parking.system;

import java.util.*;

public class ParkingLot {

    public static void main(String[] args) {
        /*
         * vehicle: find available slot, book, create ticket, park -> return ticket
         */
        Scanner scanner = new Scanner(System.in);

        ParkInfo parkInfo = new ParkInfo();
        List<CarInfo> carInfoList = new ArrayList<>();


        while(true){
            String input = scanner.nextLine();

            if("exit".equalsIgnoreCase(input)){
                break;
            }

            String[] parts = input.split(" ");
            String command = parts[0];

            switch(command){
                case  "create_parking_lot": {
                    String parkLotId = parts[1];
                    int numFloor = Integer.parseInt(parts[2]);
                    int numSlot = Integer.parseInt(parts[3]);
                    parkInfo.setParkingId(parkLotId);
                    String result = createParkingLot(numFloor, numSlot, parkInfo);
                    System.out.println(result);
                    break;
                }
                //#2 park vehicle park_vehicle CAR KA-01-DB-1234 black
                case "park_vehicle": {
                    String carType = parts[1];
                    String regNo = parts[2];
                    String colour = parts[3];
                    CarInfo carInfo = new CarInfo();
                    carInfo.setType(carType);
                    carInfo.setRegNo(regNo);
                    carInfo.setColour(colour);

                    String result = parkVehicle(parkInfo, carInfoList, carInfo);
                    System.out.println(result);
                    break;
                }
                case "unpark_vehicle":{
                    String ticketId = parts[1];

                    String result = unparkVehicle(ticketId, carInfoList, parkInfo);
                    System.out.println(result);
                    break;
                }
                case "display_free_count": {
                    String vehicleType = parts[1];
                    displayFreeCount(vehicleType, parkInfo);
                    break;
                }
                case "display_free_slots": {
                    String vehicleType = parts[1];
                    displayFreeSlot(vehicleType, parkInfo);
                    break;
                }
                case "display_occupied_slots": {
                    String vehicleType = parts[1];
                    displayOccupiedSlot(vehicleType, parkInfo);
                    break;
                }


                default:
                    break;
            }
        }

        scanner.close();
    }

    //#1 create parking lot
    public static String createParkingLot(int numFloor, int numSlot, ParkInfo parkInfo){
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
    public static String parkVehicle(ParkInfo parkInfo, List<CarInfo> carInfoList, CarInfo carInfo){
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
    public static String unparkVehicle(String ticketId, List<CarInfo> carInfoList, ParkInfo parkInfo){
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

    //#4 display free count
    public static void displayFreeCount(String vehichleType, ParkInfo parkInfo){
        Map<Integer, Boolean[]> availableMap = parkInfo.getAvailable();

        if(availableMap!=null && !availableMap.isEmpty()) {
            for (Map.Entry<Integer, Boolean[]> entry : availableMap.entrySet()) {
                boolean unpark = true;
                Boolean[] arrSlots = entry.getValue();
                int floor = entry.getKey();
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
                System.out.println("No. of free slots for " + vehichleType + " on Floor " + floor + " : " + countSlot);
            }
        }
    }

    //#5 display free slots
    public static void displayFreeSlot(String vehichleType, ParkInfo parkInfo){
        Map<Integer, Boolean[]> availableMap = parkInfo.getAvailable();

        if(availableMap!=null && !availableMap.isEmpty()) {
            for (Map.Entry<Integer, Boolean[]> entry : availableMap.entrySet()) {
                boolean unpark = true;
                Boolean[] arrSlots = entry.getValue();
                int floor = entry.getKey();
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
        }
    }

    //#6 display occupied slots
    public static void displayOccupiedSlot(String vehichleType, ParkInfo parkInfo){
        Map<Integer, Boolean[]> availableMap = parkInfo.getAvailable();

        if(availableMap!=null && !availableMap.isEmpty()) {
            for (Map.Entry<Integer, Boolean[]> entry : availableMap.entrySet()) {
                boolean unpark = true;
                Boolean[] arrSlots = entry.getValue();
                int floor = entry.getKey();
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
    }

}
