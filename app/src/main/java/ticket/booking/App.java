package ticket.booking;

import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.services.UserBookingService;
import ticket.booking.util.UserServiceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import static ticket.booking.util.UserServiceUtil.readInt;

public class App {

    public static void main(String[] args) {
        System.out.println("Running Train Booking service");
        Scanner scan = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService;
        try {
            userBookingService = new UserBookingService();
        } catch (IOException ex) {
            System.out.println("Something went wrong");
            System.out.println(ex.getMessage());
            return;
        }

        List<Train> availableTrains = new ArrayList<>();
        String source = "";
        String destination = "";
        while (option != 7) {
            System.out.println("Choose Option");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Trains");
            System.out.println("5. Book a seat");
            System.out.println("6. Cancel my booking");
            System.out.println("7. Exit the App");
            option = readInt(scan, "Choose Option (enter a number):");

            switch (option) {
                case 1:
                    System.out.println("Enter the username to signup");
                    String nameToSignUp = scan.next();
                    System.out.println("Enter the password to signup");
                    String passwordToSignUp = scan.next();
                    User userToSignUp = new User(
                            nameToSignUp,
                            UUID.randomUUID().toString(),
                            UserServiceUtil.hashPassword(passwordToSignUp),
                            passwordToSignUp,
                            new ArrayList<>()
                    );
                    userBookingService.signUp(userToSignUp);
                    break;

                case 2:
                    System.out.println("Enter the username to login");
                    String nameToLogin = scan.next();
                    System.out.println("Enter the password to login");
                    String passwordToLogin = scan.next();
                    User userToLogin = new User(
                            nameToLogin,
                            UUID.randomUUID().toString(),
                            UserServiceUtil.hashPassword(passwordToLogin),
                            passwordToLogin,
                            new ArrayList<>()
                    );

                    try {
                        userBookingService = new UserBookingService(userToLogin);
                        if (userBookingService.loginUser(nameToLogin, passwordToLogin)) {
                            System.out.println("User logged in");
                        } else {
                            System.out.println("User could not log in");
                        }
                    } catch (IOException ex) {
                        return;
                    }
                    break;

                case 3:
                    System.out.println("Fetching Bookings");
                    userBookingService.printBookings();
                    break;

                case 4:
                    System.out.println("Searching Trains");
                    System.out.println("Where would you start your journey");
                    source = scan.next();
                    System.out.println("Where would you end your journey");
                    destination = scan.next();

                    availableTrains = userBookingService.searchTrains(source, destination);

                    if (availableTrains.isEmpty()) {
                        System.out.println("No trains found");
                        break;
                    }
                    System.out.println("Here are the available trains");
                    for (Train train : availableTrains) {
                        System.out.println(train.getTrainInfo());
                    }
                    break;


                case 5:
                    if (availableTrains.isEmpty()) {
                        System.out.println("No trains found for the given route, please search for trains again");
                        break;
                    }
                    System.out.println("Tell us which train would you be choosing");
                    for (int i = 0; i < availableTrains.size(); i++) {
                        System.out.println(i + ". " + availableTrains.get(i).getTrainInfo());
                    }

                    int chosenTrainNumber = readInt(scan, "Please enter the train number");
                    // proceed booking the train
                    Train chosenTrain = availableTrains.get(chosenTrainNumber);

                    System.out.println("Here is the seating arrangement in the train you selected");
                    chosenTrain.printSeatList();

                    int chosenSeatRow = readInt(scan, "Please enter the seat row");
                    int chosenSeatCol = readInt(scan, "Please enter the seat column");

                    userBookingService.bookTicket(
                            chosenTrain.getTrainNo(),
                            chosenTrain.getTrainId(),
                            chosenSeatRow,
                            chosenSeatCol,
                            source,
                            destination
                    );
                    break;

                case 6:
                    if (!userBookingService.isBookingPresent()) {
                        System.out.println("No bookings to cancel");
                        break;
                    }
                    System.out.println("Which booking do you want to cancel");
                    userBookingService.printBookings();

                    int ticketToBeCancelled = readInt(scan, "Enter the booking number to cancel");

                    userBookingService.cancelBooking(ticketToBeCancelled);

                    break;

                default:
                    option = 7;
                    break;
            }
        }
    }
}
