package org.gym.mapper;

import org.gym.entity.Training;
import org.gym.dto.TrainingDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.gym.entity.TrainingType.STRETCHING;
import static org.junit.jupiter.api.Assertions.*;

class TrainingMapperImplTest {

    private final TrainingMapper trainingMapper = new TrainingMapperImpl();

    @Test
    void convertToDto() {
        Training training = Training.builder()
                .id(1L)
                .traineeId(1L)
                .trainerId(2L)
                .trainingName("new program for this week")
                .trainingDate(LocalDate.of(2025, 2, 13))
                .trainingDuration(30L)
                .trainingType(STRETCHING)
                .build();

        TrainingDto trainingDto = trainingMapper.convertToDto(training);

        assertNotNull(trainingDto);
        assertEquals(training.getId(), trainingDto.getId());
        assertEquals(training.getTraineeId(), trainingDto.getTraineeId());
        assertEquals(training.getTrainerId(), trainingDto.getTrainerId());
        assertEquals(training.getTrainingName(), trainingDto.getTrainingName());
        assertEquals(training.getTrainingType(), trainingDto.getTrainingType());
        assertEquals(training.getTrainingDate(), trainingDto.getTrainingDate());
        assertEquals(training.getTrainingDuration(), trainingDto.getTrainingDuration());
    }

    @Test
    void convertToDtoWithNullTrainee() {
        TrainingDto trainingDto = trainingMapper.convertToDto(null);
        assertNull(trainingDto, "Expected result: convertToDto returns null when input is null");
    }

    @Test
    void convertToEntity() {
        TrainingDto trainingDto = TrainingDto.builder()
                .id(1L)
                .traineeId(1L)
                .trainerId(2L)
                .trainingName("new program for this week")
                .trainingDate(LocalDate.of(2025, 2, 13))
                .trainingDuration(30L)
                .trainingType(STRETCHING)
                .build();

        Training training = trainingMapper.convertToEntity(trainingDto);

        assertNotNull(training);
        assertEquals(trainingDto.getId(), training.getId());
        assertEquals(trainingDto.getTraineeId(), training.getTraineeId());
        assertEquals(trainingDto.getTrainerId(), training.getTrainerId());
        assertEquals(trainingDto.getTrainingName(), training.getTrainingName());
        assertEquals(trainingDto.getTrainingType(), training.getTrainingType());
        assertEquals(trainingDto.getTrainingDate(), training.getTrainingDate());
        assertEquals(trainingDto.getTrainingDuration(), training.getTrainingDuration());
    }

    @Test
    void convertToEntityWithNullTraineeDto() {
        Training training = trainingMapper.convertToEntity(null);
        assertNull(training, "Expected result: convertToEntity returns null when input is null");
    }
}
