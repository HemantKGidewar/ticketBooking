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
            option = scan.nextInt();

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
                        if (userBookingService.loginUser()) {
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
                    userBookingService.fetchBooking();
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

                    System.out.println("Please enter the train number");
                    int chosenTrainNumber = scan.nextInt();
                    // proceed booking the train
                    Train chosenTrain = availableTrains.get(chosenTrainNumber);

                    System.out.println("Here is the seating arrangement in the train you selected");
                    chosenTrain.printSeatList();

                    System.out.println("Please enter the seat row");
                    int chosenSeatRow = scan.nextInt();
                    System.out.println("Please enter the seat column");
                    int chosenSeatCol = scan.nextInt();

                    userBookingService.bookTicket(
                            chosenTrain.getTrainNo(),
                            chosenTrain.getTrainId(),
                            chosenSeatRow,
                            chosenSeatCol,
                            source,
                            destination
                    );
                    break;

                default:
                    option = 7;
                    break;
            }
        }
    }
}
