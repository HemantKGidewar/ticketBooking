package ticket.booking.entities;

import java.sql.Time;
import java.util.List;
import java.util.Map;

public class Train {
    private String trainId;
    private String trainNo;
    private List<List<Integer>> seats;
    private Map<String, Time> arrivalTimes;
    private List<String> stations;

    public String getTrainId(){
        return trainId;
    }

    public String getTrainNo(){
        return trainNo;
    }

    public List<List<Integer>> getSeats(){
        return seats;
    }

    public String getTrainInfo(){
        return String.format("Train ID: %s, Train No : %s", trainId, trainNo);
    }

    public List<String> getStations () {
        return stations;
    }
}
