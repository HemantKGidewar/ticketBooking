package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Ticket;
import ticket.booking.entities.Train;
import ticket.booking.entities.User;
import ticket.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        return  objectMapper.readValue(users, new TypeReference<List<User>>(){});
    }

    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream().filter(user1 -> {
            return user1.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(), user1.getHashedPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public void signUp(User user1){
        try {
            userList.add((user1));
            saveUserListToFile();
        } catch (IOException ex){
            System.out.println("User could not sign up");
        }
    }

    public void saveUserListToFile() throws IOException{
        File userFile = new File(USERS_PATH);
        objectMapper.writeValue(userFile, userList);
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

}
