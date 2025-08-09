package ticket.booking.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.sql.Time;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class Train {
    private String trainId;
    private String trainNo;
    private List<List<Integer>> seats;
    private Map<String, Time> arrivalTimes;
    private List<String> stations;

    public String getTrainId() {
        return trainId;
    }

    public String getTrainNo() {
        return trainNo;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public String getTrainInfo() {
        return String.format("Train ID: %s, Train No : %s", trainId, trainNo);
    }

    public List<String> getStations() {
        return stations;
    }

    public Map<String, Time> getArrivalTimes() {
        return arrivalTimes;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    public void setArrivalTimes(Map<String, Time> arrivalTimes) {
        this.arrivalTimes = arrivalTimes;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }

    public Boolean bookSeat(Integer seatRow, Integer seatCol) {
        if (seats.get(seatRow).get(seatCol) == 1) {
            return Boolean.FALSE;
        }
        seats.get(seatRow).set(seatCol, 1);
        return Boolean.TRUE;
    }

    public void printSeatList() {
        for (int row = 0; row < seats.size(); row++) {
            for (int col = 0; col < seats.getFirst().size(); col++) {
                if (seats.get(row).get(col) == 0) {
                    System.out.print(String.format("%02d", row) + "_" + String.format("%02d", col) + " ");
                } else {
                    System.out.print("_OCC_");
                }
            }
            System.out.println();
        }
    }

    public Boolean checkSeatsAvailable(Integer seatRow, Integer seatCol) {
        if (seatRow > seats.size()) {
            System.out.println("The row you selected is not available");
            return Boolean.FALSE;
        }

        if (seatCol > seats.getFirst().size()) {
            System.out.println("The column you selected is not available");
            return Boolean.FALSE;
        }

        if (seats.get(seatRow).get(seatCol) != 0) {
            System.out.println("The chosen seat is not available / is already booked");
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }
}
