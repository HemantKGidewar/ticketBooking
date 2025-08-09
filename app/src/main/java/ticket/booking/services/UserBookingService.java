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

    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
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

    public void fetchBooking() {
        user.printTickets();
    }

    public Boolean cancelBooking(String ticketId) {
        List<Ticket> currentTicketList = user.getTicketsBooked();
        List<Ticket> newTicketList = currentTicketList.stream().filter(ticket -> !Objects.equals(ticket.getTicketId(), ticketId)).toList();
        // !Objects.equals() wont throw NullPointException error, thus is safer from simple .equals() comparison
        user.setTicketsBooked(newTicketList);

        return Boolean.TRUE;
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
                    train.get()
            );

            user.addTicket(ticket);
            saveUserListToFile();
        } catch (IOException ignored) {
            System.out.println("Something went wrong ! \n Ticket couldn't be booked");
        }
    }
}
