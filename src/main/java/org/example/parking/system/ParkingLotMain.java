package org.example.parking.system;

import java.util.*;

public class ParkingLotMain {

    public static void main(String[] args) {
        /*
         * vehicle: find available slot, book, create ticket, park -> return ticket
         */
        Scanner scanner = new Scanner(System.in);

        ParkInfo parkInfo = new ParkInfo();
        List<CarInfo> carInfoList = new ArrayList<>();
        ParkingLotService parkingLotService = new ParkingLotService();

        System.out.println("=============Welcome to Parking Lot System=============");

        while(true){
            try{
                printListCmd();

                String input = scanner.nextLine();

                if("exit".equalsIgnoreCase(input)){
                    System.out.println("=============Thank You for Using Parking Lot System=============");
                    break;
                }

                String[] parts = input.split(" ");
                String command = parts[0];

                switch(command){
                    //#1 create_parking_lot <parking_lot_id> <no_of_floors> <no_of_slots_per_floor>
                    case  "create_parking_lot": {
                        String parkLotId = parts[1];
                        int numFloor = Integer.parseInt(parts[2]);
                        int numSlot = Integer.parseInt(parts[3]);
                        parkInfo.setParkingId(parkLotId);
                        String result = parkingLotService.createParkingLot(numFloor, numSlot, parkInfo);
                        System.out.println(result);
                        break;
                    }
                    //#2 park_vehicle <vehicle_type> <reg_no> <color>
                    case "park_vehicle": {
                        String carType = parts[1];
                        String regNo = parts[2];
                        String colour = parts[3];
                        CarInfo carInfo = new CarInfo();
                        carInfo.setType(carType);
                        carInfo.setRegNo(regNo);
                        carInfo.setColour(colour);

                        String result = parkingLotService.parkVehicle(parkInfo, carInfoList, carInfo);
                        System.out.println(result);
                        break;
                    }
                    //#3 unpark_vehicle <ticket_id>
                    case "unpark_vehicle":{
                        String ticketId = parts[1];

                        String result = parkingLotService.unparkVehicle(ticketId, carInfoList, parkInfo);
                        System.out.println(result);
                        break;
                    }
                    case "display": {
                        String displayType = parts[1];
                        String vehicleType = parts[2];
                        parkingLotService.display(vehicleType, parkInfo, displayType);
                        break;
                    }
                    default:
                        break;
                }
            }catch (Exception ex){
                System.out.println("Wrong command. Please re-enter");
                printListCmd();
            }

        }
        scanner.close();
    }

    public static void printListCmd(){
        System.out.println("List of Commands:");
        System.out.println("""
                        > create_parking_lot <parking_lot_id> <no_of_floors> <no_of_slots_per_floor>
                        > park_vehicle <vehicle_type> <reg_no> <color>
                        - Possible values of vehicle type: truck, bike, car
                        > unpark_vehicle <ticket_id>
                        > display <display_type> <vehicle_type>
                        - Possible values of display_type: free_count, free_slots, occupied_slots
                        > exit
                        """);
    }
}
