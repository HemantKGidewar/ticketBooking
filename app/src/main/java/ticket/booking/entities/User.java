package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class User {
    private String name;
    private String userId;
    private String password;
    private String hashedPassword;
    private List<Ticket> ticketsBooked;

    public User(String name, String userId, String hashedPassword, String password, List<Ticket> ticketsBooked) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.hashedPassword = hashedPassword;
        this.ticketsBooked = ticketsBooked;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void printTickets() {
        for(int i=0; i<ticketsBooked.size(); i++){
            System.out.println(i + ". " +  ticketsBooked.get(i).getTicketInfo());
        }
    }

    public List<Ticket> getTicketsBooked() {
        return this.ticketsBooked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked1) {
        this.ticketsBooked = ticketsBooked1;
    }

    public void addTicket(Ticket ticket) {
        ticketsBooked.add(ticket);
    }

    public void removeTicketFromIndex(int index){
        if(index < 0 || index >= ticketsBooked.size()) {
            System.out.println("Index out of bounds; Select a correct one");
        }
        ticketsBooked.remove(index);
    }

}
