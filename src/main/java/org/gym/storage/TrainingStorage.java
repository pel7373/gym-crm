package org.gym.storage;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.gym.entity.Training;
import org.gym.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class TrainingStorage implements CrudStorage<Training, Long> {
    private final HashMap<Long, Training> trainingMap = new HashMap<>();
    private long storageNextId;
    private final String trainingsFileIn;
    private final String trainingsFileOut;
    private final RestoreBackupDataFromToFile<Training> restoreBackupDataFromToFile;

    @Autowired
    public TrainingStorage(@Value("${file.trainings.input}") String trainingsFileIn,
                          @Value("${file.trainings.output}") String trainingsFileOut,
                           RestoreBackupDataFromToFile<Training> restoreBackupDataFromToFile) {
        this.trainingsFileIn = trainingsFileIn;
        this.trainingsFileOut = trainingsFileOut;
        this.restoreBackupDataFromToFile = restoreBackupDataFromToFile;
    }

    @Override
    public List<Training> findAll() {
        return new ArrayList<>(trainingMap.values());
    }

    @Override
    public Training findById(Long id) throws EntityNotFoundException {
        return Optional.ofNullable(trainingMap.get(id))
                .orElseThrow(() -> {
                    LOGGER.warn("The training with id {} wasn't found!", id);
                    return new EntityNotFoundException(String.format("The training with id %d wasn't found!", id));
                });
    }

    @Override
    public Training save(Training training) {
        training.setId(storageNextId);
        trainingMap.put(storageNextId++, training);
        return training;
    }

    @Override
    public Training update(Long id, Training training) {
        trainingMap.put(id, training);
        return training;
    }

    @Override
    public void deleteById(Long id) {
        trainingMap.remove(id);
    }

    @PostConstruct
    private void restoreDataFromFileToStorage() {
        List<Training> trainingList;
        try {
            trainingList = restoreBackupDataFromToFile.restoreTrainings(trainingsFileIn);
        } catch (IOException e) {
            LOGGER.warn("restoreDataFromFileToDb couldn't read or convert data from file {}", trainingsFileIn);
            return;
        }

        if(trainingList != null && !trainingList.isEmpty()) {
            trainingList.forEach(t -> trainingMap.put(t.getId(), t));
            storageNextId = restoreBackupDataFromToFile.calculateNextId(trainingList);
            LOGGER.info("restoreDataFromFileToDb restored storage with data from file {}", trainingsFileIn);
        } else {
            LOGGER.warn("restoreDataFromFileToDb has read the file {}, but can't get data from it", trainingsFileIn);
        }
    }

    @PreDestroy
    public void backupDataFromStorageToFile() {
        restoreBackupDataFromToFile.backupToFile(trainingsFileOut, new ArrayList<>(trainingMap.values()));
    }
}
