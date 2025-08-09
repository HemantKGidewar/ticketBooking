package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UserBookingService {

    private User user;

    private final List<User> userList;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String USERS_PATH = "app/src/main/java/ticket/booking/localDb/users.json";

    public UserBookingService(User newUser) throws IOException {
        this.user = newUser;
        userList = loadUsers();
    }

    public UserBookingService() throws IOException {
        userList = loadUsers();
    }

    public List<User> loadUsers() throws IOException {
        File users = new File(USERS_PATH);
        // we use TypeReference get the type of List<User> at runtime
        return objectMapper.readValue(users, new TypeReference<List<User>>() {
        });
    }

    public boolean loginUser(String username, String password) {
        Optional<User> foundUser = userList.stream()
                .filter(u -> u.getName().equals(username) &&
                        UserServiceUtil.checkPassword(password, u.getHashedPassword()))
                .findFirst();

        if (foundUser.isPresent()) {
            user = foundUser.get(); // reference from userList
            return true;
        }
        return false;
    }

    public void signUp(User user1) {
        try {
            userList.add((user1));
            saveUserListToFile();
        } catch (IOException ex) {
            System.out.println("User could not sign up");
        }
    }

    public void saveUserListToFile() throws IOException {
        File userFile = new File(USERS_PATH);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(userFile, userList);
    }

    public void printBookings() {
        user.printTickets();
    }

    public void cancelBooking(int ticketIndex) {
        try {
            if (ticketIndex < 0 || ticketIndex >= user.getTicketsBooked().size()) {
                System.out.println("Invalid index of ticket");
                return;
            }
            Ticket userTicket = user.getTicketsBooked().get(ticketIndex);
            TrainService trainService = new TrainService();
            trainService.removeBookedSeat(
                    userTicket.getTrain(),
                    userTicket.getSeatRow(),
                    userTicket.getSeatCol());

            user.removeTicketFromIndex(ticketIndex);
            saveUserListToFile();
            System.out.println("The booking was successfully cancelled");
        } catch (IOException ex) {
            System.out.println("The booking could not be cancelled");
        }
    }

    public List<Train> searchTrains(String source, String destination) {
        try {
            TrainService trainService = new TrainService();
            return trainService.getTrainList(source, destination);
        } catch (IOException ex) {
            return new ArrayList<>();
        }
    }

    public void bookTicket(String trainNo,
                           String trainId,
                           Integer seatRow,
                           Integer seatCol,
                           String source,
                           String destination) {
        // This function books ticket for user from source to destination for a given train
        // For now the function books the seats for the whole journey for the user
        if (user == null) {
            System.out.println("No user logged in!");
            return;
        }

        try {
            TrainService trainService = new TrainService();
            Optional<Train> train = trainService.bookTrainSeats(trainNo, trainId, seatRow, seatCol);
            if (train.isEmpty()) {

                return;
            }
            Ticket ticket = new Ticket(
                    UUID.randomUUID().toString(),
                    user.getUserId(),
                    source,
                    destination,
                    new Date(),
                    train.get(),
                    seatRow,
                    seatCol
            );

            user.addTicket(ticket);
            saveUserListToFile();
        } catch (IOException ignored) {
            System.out.println("Something went wrong ! \n Ticket couldn't be booked");
        }
    }

    public Boolean isBookingPresent() {
        return !user.getTicketsBooked().isEmpty();
    }

}
