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
