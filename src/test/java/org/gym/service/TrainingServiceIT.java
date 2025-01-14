package org.gym.service;

import org.gym.Main;
import org.gym.config.TestConfig;
import org.gym.entity.Training;
import org.gym.dto.TrainingDto;
import org.gym.exception.EntityNotFoundException;
import org.gym.service.impl.TrainingServiceImpl;
import org.gym.storage.TrainingStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.gym.entity.TrainingType.STRETCHING;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = {Main.class, TestConfig.class})
class TrainingServiceIT {
    @Autowired
    private TrainingServiceImpl trainingService;

    @Autowired
    private TrainingStorage trainingStorage;

    private TrainingDto trainingDto;
    long idForTraining;

    @BeforeEach
    void setup() {
        trainingDto = TrainingDto.builder()
                .id(1L)
                .traineeId(1L)
                .trainerId(2L)
                .trainingName("new program for this week")
                .trainingDate(LocalDate.of(2025, 2, 13))
                .trainingDuration(30L)
                .trainingType(STRETCHING)
                .build();
    }

    @AfterEach
    void clear() {
        trainingStorage.deleteById(idForTraining);
    }

    @Test
    void saveTrainingToStorageSuccessfully() {
        TrainingDto createdTrainingDto = trainingService.save(trainingDto);
        idForTraining = createdTrainingDto.getId();

        assertNotNull(createdTrainingDto);
        assertEquals(trainingDto.getTraineeId(), createdTrainingDto.getTraineeId());
        assertEquals(trainingDto.getTrainerId(), createdTrainingDto.getTrainerId());
        assertEquals(trainingDto.getTrainingName(), createdTrainingDto.getTrainingName());

        Training savedTraining = trainingStorage.findById(idForTraining);
        assertNotNull(savedTraining);
        assertEquals(trainingDto.getTraineeId(), savedTraining.getTraineeId());
        assertEquals(trainingDto.getTrainerId(), savedTraining.getTrainerId());
        assertEquals(trainingDto.getTrainingName(), savedTraining.getTrainingName());
    }

    @Test
    void getTrainingSuccessfully() {
        TrainingDto createdTrainingDto = trainingService.save(trainingDto);
        idForTraining = createdTrainingDto.getId();

        assertNotNull(createdTrainingDto);
        assertEquals(trainingDto.getTraineeId(), createdTrainingDto.getTraineeId());
        assertEquals(trainingDto.getTrainerId(), createdTrainingDto.getTrainerId());
        assertEquals(trainingDto.getTrainingName(), createdTrainingDto.getTrainingName());

        Training receivedAgainTraining = trainingStorage.findById(idForTraining);
        assertNotNull(receivedAgainTraining);
        assertEquals(trainingDto.getTraineeId(), receivedAgainTraining.getTraineeId());
        assertEquals(trainingDto.getTrainerId(), receivedAgainTraining.getTrainerId());
    }

    @Test
    void getTrainingNotFound() {
        long idNotFound = 4L;
        assertThrows(EntityNotFoundException.class, () -> trainingService.getById(idNotFound),
                String.format("Training with id %d wasn't found!", idNotFound));
    }
}
