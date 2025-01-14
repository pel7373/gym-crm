package org.gym.service;

import org.gym.Main;
import org.gym.config.TestConfig;
import org.gym.entity.Trainee;
import org.gym.dto.TraineeDto;
import org.gym.exception.EntityNotFoundException;
import org.gym.service.impl.TraineeServiceImpl;
import org.gym.storage.TraineeStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = {Main.class, TestConfig.class})
class TraineeServiceIT {

    @Autowired
    private TraineeServiceImpl traineeService;

    @Autowired
    private TraineeStorage traineeStorage;

    private TraineeDto traineeDto;
    long idForTrainee;

    @BeforeEach
    void setup() {
        traineeDto = TraineeDto.builder()
                .id(1L)
                .firstName("Maria")
                .lastName("Petrenko")
                .userName("Maria.Petrenko")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();
    }

    @AfterEach
    void clear() {
        traineeStorage.deleteById(idForTrainee);
    }

    @Test
    void saveTraineeToStorageSuccessfully() {
        TraineeDto createdTraineeDto = traineeService.save(traineeDto);
        idForTrainee = createdTraineeDto.getId();

        assertNotNull(createdTraineeDto);
        assertEquals(traineeDto.getFirstName(), createdTraineeDto.getFirstName());
        assertEquals(traineeDto.getLastName(), createdTraineeDto.getLastName());
        assertEquals(traineeDto.getUserName(), createdTraineeDto.getUserName());

        Trainee savedTrainee = traineeStorage.findById(idForTrainee);
        assertNotNull(savedTrainee);
        assertEquals(traineeDto.getFirstName(), savedTrainee.getFirstName());
        assertEquals(traineeDto.getLastName(), savedTrainee.getLastName());
        assertEquals(traineeDto.getUserName(), savedTrainee.getUserName());
    }


    @Test
    void getTraineeSuccessfully() {
        TraineeDto createdTraineeDto = traineeService.save(traineeDto);
        idForTrainee = createdTraineeDto.getId();

        assertNotNull(createdTraineeDto);
        assertEquals(traineeDto.getFirstName(), createdTraineeDto.getFirstName());
        assertEquals(traineeDto.getLastName(), createdTraineeDto.getLastName());

        Trainee receivedAgainTrainee = traineeStorage.findById(idForTrainee);
        assertNotNull(receivedAgainTrainee);
        assertEquals(traineeDto.getFirstName(), receivedAgainTrainee.getFirstName());
        assertEquals(traineeDto.getLastName(), receivedAgainTrainee.getLastName());
    }

    @Test
    void getTraineeNotFound() {
        long idNotFound = 4L;
        assertThrows(EntityNotFoundException.class, () -> traineeService.getById(idNotFound),
                String.format("Trainee with id %d wasn't found!", idNotFound));
    }

    @Test
    void updateTraineeSuccessfully() {
        TraineeDto createdTraineeDto = traineeService.save(traineeDto);
        idForTrainee = createdTraineeDto.getId();

        String firstName = "Petro";
        String lastName = "Petrenko";

        TraineeDto traineeDtoForUpdate = new TraineeDto();
        traineeDtoForUpdate.setFirstName(firstName);
        traineeDtoForUpdate.setLastName(lastName);
        traineeDtoForUpdate.setId(idForTrainee);

        TraineeDto updatedTraineeDto = traineeService.update(idForTrainee, traineeDtoForUpdate);

        assertNotNull(updatedTraineeDto);
        assertEquals(firstName, updatedTraineeDto.getFirstName());
        assertEquals(lastName, updatedTraineeDto.getLastName());

        Trainee updatedTrainee = traineeStorage.findById(idForTrainee);
        assertNotNull(updatedTrainee);
        assertEquals(firstName, updatedTrainee.getFirstName());
        assertEquals(lastName, updatedTrainee.getLastName());
    }

    @Test
    void updateTraineeNotFound() {
        String firstName = "Petro";
        String lastName = "Petrenko";
        long idNotExist = 20L;

        TraineeDto traineeDtoForUpdate = new TraineeDto();
        traineeDtoForUpdate.setFirstName(firstName);
        traineeDtoForUpdate.setLastName(lastName);

        assertThrows(EntityNotFoundException.class, () -> traineeService.update(idNotExist, traineeDtoForUpdate),
                String.format("Trainee with id %d wasn't found!", idNotExist));
    }

    @Test
    void deleteTraineeSuccessfully() {
        TraineeDto createdTraineeDto = traineeService.save(traineeDto);
        idForTrainee = createdTraineeDto.getId();
        traineeService.deleteById(idForTrainee);

        assertThrows(EntityNotFoundException.class, () -> traineeService.getById(idForTrainee),
                String.format("Trainee with id %d wasn't found!", idForTrainee));
    }

    @Test
    void deleteTrainee_notFound() {
        long idNotExist = 20L;
        traineeService.deleteById(idNotExist);
        assertThrows(EntityNotFoundException.class, () -> traineeService.getById(idNotExist),
                String.format("Trainee with id %d wasn't found!", idNotExist));
    }
}
