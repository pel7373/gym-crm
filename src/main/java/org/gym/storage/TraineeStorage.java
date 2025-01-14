package org.gym.storage;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.gym.entity.Trainee;
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
public class TraineeStorage implements CrudStorage<Trainee, Long>, IsUserNameExists {
    private final HashMap<Long, Trainee> traineeMap = new HashMap<>();
    private long storageNextId;
    private final String traineesFileIn;
    private final String traineesFileOut;
    private final RestoreBackupDataFromToFile<Trainee> restoreBackupDataFromToFile;

    @Autowired
    public TraineeStorage(@Value("${file.trainees.input}") String traineesFileIn,
                          @Value("${file.trainees.output}") String traineesFileOut,
                          RestoreBackupDataFromToFile<Trainee> restoreBackupDataFromToFile) {
        this.traineesFileIn = traineesFileIn;
        this.traineesFileOut = traineesFileOut;
        this.restoreBackupDataFromToFile = restoreBackupDataFromToFile;
    }

    @Override
    public List<Trainee> findAll() {
        return new ArrayList<>(traineeMap.values());
    }

    @Override
    public Trainee findById(Long id) throws EntityNotFoundException {
        return Optional.ofNullable(traineeMap.get(id))
                .orElseThrow(() -> {
                    LOGGER.warn("Trainee with id {} wasn't found!", id);
                    return new EntityNotFoundException(String.format("Trainee with id %d wasn't found!", id));
                });
    }

    @Override
    public Trainee save(Trainee trainee) {
        trainee.setId(storageNextId);
        traineeMap.put(storageNextId++, trainee);
        return trainee;
    }

    @Override
    public Trainee update(Long id, Trainee trainee) {
        traineeMap.put(id, trainee);
        return trainee;
    }

    @Override
    public void deleteById(Long id) {
        traineeMap.remove(id);
    }

    @Override
    public boolean isUserNameExist(String userName) {
        return traineeMap.values().stream()
                .anyMatch(t -> userName.equals(t.getUserName()));
    }

    @PostConstruct
    private void restoreDataFromFileToStorage() {
        List<Trainee> traineeList;
        try {
            traineeList = restoreBackupDataFromToFile.restoreTrainees(traineesFileIn);
        } catch (IOException e) {
            LOGGER.warn("restoreDataFromFileToDb couldn't read or convert data from file {}", traineesFileIn);
            return;
        }

        if(traineeList != null && !traineeList.isEmpty()) {
            traineeList.forEach(t -> traineeMap.put(t.getId(), t));
            storageNextId = restoreBackupDataFromToFile.calculateNextId(traineeList);
            LOGGER.info("restoreDataFromFileToDb restored storage with data from file {}", traineesFileIn);
        } else {
            LOGGER.warn("restoreDataFromFileToDb has read the file {}, but can't get data from it", traineesFileIn);
        }
    }

    @PreDestroy
    public void backupDataFromStorageToFile() {
        restoreBackupDataFromToFile.backupToFile(traineesFileOut, new ArrayList<>(traineeMap.values()));
    }
}
