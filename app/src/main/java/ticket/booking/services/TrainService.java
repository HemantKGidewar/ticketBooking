package ticket.booking.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TrainService {
    private final List<Train> trainList;
    private static final String TRAIN_PATH = "app/src/main/java/ticket/booking/localDb/trains.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public TrainService() throws IOException {
        trainList = loadTrains();
    }

    public List<Train> loadTrains() throws IOException {
        System.out.println("Loaded Trains");
        File trains = new File(TRAIN_PATH);
        List<Train> list = objectMapper.readValue(trains, new TypeReference<List<Train>>() {
        });
        if (list.isEmpty()) {
            System.out.println("No trains found");
        }
        System.out.println("Number of total trains : " + list.size());
        return list;
    }

    public void saveTrainListToFile() throws IOException {
        File trainFile = new File(TRAIN_PATH);
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(trainFile, trainList);
    }

    public List<Train> getTrainList(String source, String destination) {
        return trainList.stream().filter(train -> {
            List<String> stationOrder = train.getStations();

            int sourceIndex = stationOrder.indexOf(source.toLowerCase());
            int destinationIndex = stationOrder.indexOf(destination.toLowerCase());

            return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
        }).toList();
    }

    public Train getTrain(String trainNo, String trainId) {
        return trainList.stream().filter(train -> {
            return train.getTrainId().equals(trainId) && train.getTrainNo().equals(trainNo);
        }).findAny().orElse(null);
    }

    public Optional<Train> bookTrainSeats(String trainNo, String trainId, Integer seatRow, Integer seatCol) throws IOException {
        Train selectedTrain = getTrain(trainNo, trainId);
        if (selectedTrain == null) {
            System.out.println("No train found!");
            return Optional.empty();
        }
        if (!selectedTrain.checkSeatsAvailable(seatRow, seatCol)) {
            return Optional.empty();
        }

        selectedTrain.bookSeat(seatRow, seatCol);
        saveTrainListToFile();
        System.out.println("Seat booked successfully");
        return Optional.of(selectedTrain);
    }
}
