package ticket.booking.entities;

import java.util.Date;

public class Ticket {
    private String ticketId;
    private String userId;
    private String source;
    private String destination;
    private Date dateOfTravel;
    private Train train;

    public String getTicketInfo(){
        return "ticketId : " + ticketId + ", from : " + source + " to : " + destination;
    }

    public String getTicketId() {
        return this.ticketId;
    }
}
