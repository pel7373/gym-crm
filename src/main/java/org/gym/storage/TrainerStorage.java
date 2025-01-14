package org.gym.storage;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.gym.entity.Trainer;
import org.gym.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
public class TrainerStorage implements CrudStorage<Trainer, Long>, IsUserNameExists {
    private final HashMap<Long, Trainer> trainerMap = new HashMap<>();
    private long storageNextId;
    private final String trainersFileIn;
    private final String trainersFileOut;
    private final RestoreBackupDataFromToFile<Trainer> restoreBackupDataFromToFile;

    @Autowired
    public TrainerStorage(@Value("${file.trainers.input}") String trainersFileIn,
                          @Value("${file.trainers.output}") String trainersFileOut,
                          RestoreBackupDataFromToFile<Trainer> restoreBackupDataFromToFile) {
        this.trainersFileIn = trainersFileIn;
        this.trainersFileOut = trainersFileOut;
        this.restoreBackupDataFromToFile = restoreBackupDataFromToFile;
    }

    @Override
    public List<Trainer> findAll() {
        return new ArrayList<>(trainerMap.values());
    }

    @Override
    public Trainer findById(Long id) throws EntityNotFoundException {
        return Optional.ofNullable(trainerMap.get(id))
                .orElseThrow(() -> {
                    LOGGER.warn("Trainer with id {} wasn't found!", id);
                    return new EntityNotFoundException(String.format("Trainer with id %d wasn't found!", id));
                });
    }

    @Override
    public Trainer save(Trainer trainer) {
        trainer.setId(storageNextId);
        trainerMap.put(storageNextId++, trainer);
        return trainer;
    }

    @Override
    public Trainer update(Long id, Trainer trainer) {
        trainerMap.put(id, trainer);
        return trainer;
    }

    @Override
    public void deleteById(Long id) {
        trainerMap.remove(id);
    }

    @Override
    public boolean isUserNameExist(String userName) {
        return trainerMap.values().stream()
                .anyMatch(t -> userName.equals(t.getUserName()));
    }

    @PostConstruct
    private void restoreDataFromFileToStorage() {
        List<Trainer> trainerList;
        try {
            trainerList = restoreBackupDataFromToFile.restoreTrainers(trainersFileIn);
        } catch (IOException e) {
            LOGGER.warn("restoreDataFromFileToDb couldn't read or convert data from file {}", trainersFileIn);
            return;
        }

        if(trainerList != null && !trainerList.isEmpty()) {
            trainerList.forEach(t -> trainerMap.put(t.getId(), t));
            storageNextId = restoreBackupDataFromToFile.calculateNextId(trainerList);
            LOGGER.info("restoreDataFromFileToDb restored storage with data from file {}", trainersFileIn);
        } else {
            LOGGER.warn("restoreDataFromFileToDb has read the file {}, but can't get data from it", trainersFileIn);
        }
    }

    @PreDestroy
    public void backupDataFromStorageToFile() {
        restoreBackupDataFromToFile.backupToFile(trainersFileOut, new ArrayList<>(trainerMap.values()));
    }
}
