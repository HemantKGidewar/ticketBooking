package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TrainService {
    private List<Train> trainList;
    private static final String TRAIN_PATH = "../localDb/trains.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public TrainService() throws IOException {
        trainList = loadTrains();
    }

    public List<Train> loadTrains () throws IOException {
        File trains = new File(TRAIN_PATH);
        return objectMapper.readValue(trains, new TypeReference<List<Train>>() {});
    }

    public List<Train> getTrainList(String source, String destination){
        return trainList.stream().filter(train -> {
            List<String> stationOrder = train.getStations();

            int sourceIndex = stationOrder.indexOf(source.toLowerCase());
            int destinationIndex = stationOrder.indexOf(destination.toLowerCase());

            return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
        }).toList();
    }

    public void printAvailableTrains(){

    }


}
