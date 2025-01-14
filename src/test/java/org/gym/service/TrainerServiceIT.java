package org.gym.service;

import org.gym.Main;
import org.gym.config.TestConfig;
import org.gym.entity.Trainer;
import org.gym.dto.TrainerDto;
import org.gym.exception.EntityNotFoundException;
import org.gym.service.impl.TrainerServiceImpl;
import org.gym.storage.TrainerStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.gym.entity.TrainingType.YOGA;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = {Main.class, TestConfig.class})
class TrainerServiceIT {
    @Autowired
    private TrainerServiceImpl trainerService;

    @Autowired
    private TrainerStorage trainerStorage;

    private TrainerDto trainerDto;
    long idForTrainer;

    @BeforeEach
    void setup() {
        trainerDto = TrainerDto.builder()
                .id(1L)
                .firstName("Maria")
                .lastName("Petrenko")
                .userName("Maria.Petrenko")
                .isActive(true)
                .specialization(YOGA)
                .build();
    }

    @AfterEach
    void clear() {
        trainerStorage.deleteById(idForTrainer);
    }

    @Test
    void saveTrainerToStorageSuccessfully() {
        TrainerDto createdTrainerDto = trainerService.save(trainerDto);
        idForTrainer = createdTrainerDto.getId();

        assertNotNull(createdTrainerDto);
        assertEquals(trainerDto.getFirstName(), createdTrainerDto.getFirstName());
        assertEquals(trainerDto.getLastName(), createdTrainerDto.getLastName());
        assertEquals(trainerDto.getUserName(), createdTrainerDto.getUserName());

        Trainer savedTrainer = trainerStorage.findById(idForTrainer);
        assertNotNull(savedTrainer);
        assertEquals(trainerDto.getFirstName(), savedTrainer.getFirstName());
        assertEquals(trainerDto.getLastName(), savedTrainer.getLastName());
        assertEquals(trainerDto.getUserName(), savedTrainer.getUserName());
    }


    @Test
    void getTrainerSuccessfully() {
        TrainerDto createdTrainerDto = trainerService.save(trainerDto);
        idForTrainer = createdTrainerDto.getId();

        assertNotNull(createdTrainerDto);
        assertEquals(trainerDto.getFirstName(), createdTrainerDto.getFirstName());
        assertEquals(trainerDto.getLastName(), createdTrainerDto.getLastName());

        Trainer receivedAgainTrainer = trainerStorage.findById(idForTrainer);
        assertNotNull(receivedAgainTrainer);
        assertEquals(trainerDto.getFirstName(), receivedAgainTrainer.getFirstName());
        assertEquals(trainerDto.getLastName(), receivedAgainTrainer.getLastName());
    }

    @Test
    void getTrainerNotFound() {
        long idNotFound = 4L;
        assertThrows(EntityNotFoundException.class, () -> trainerService.getById(idNotFound),
                String.format("Trainer with id %d wasn't found!", idNotFound));
    }

    @Test
    void updateTrainerSuccessfully() {
        TrainerDto createdTrainerDto = trainerService.save(trainerDto);
        idForTrainer = createdTrainerDto.getId();

        String firstName = "Petro";
        String lastName = "Petrenko";

        TrainerDto trainerDtoForUpdate = new TrainerDto();
        trainerDtoForUpdate.setFirstName(firstName);
        trainerDtoForUpdate.setLastName(lastName);
        trainerDtoForUpdate.setId(idForTrainer);

        TrainerDto updatedTrainerDto = trainerService.update(idForTrainer, trainerDtoForUpdate);

        assertNotNull(updatedTrainerDto);
        assertEquals(firstName, updatedTrainerDto.getFirstName());
        assertEquals(lastName, updatedTrainerDto.getLastName());

        Trainer updatedTrainer = trainerStorage.findById(idForTrainer);
        assertNotNull(updatedTrainer);
        assertEquals(firstName, updatedTrainer.getFirstName());
        assertEquals(lastName, updatedTrainer.getLastName());
    }

    @Test
    void updateTrainerNotFound() {
        String firstName = "Petro";
        String lastName = "Petrenko";
        long idNotExist = 20L;

        TrainerDto trainerDtoForUpdate = new TrainerDto();
        trainerDtoForUpdate.setFirstName(firstName);
        trainerDtoForUpdate.setLastName(lastName);

        assertThrows(EntityNotFoundException.class, () -> trainerService.update(idNotExist, trainerDtoForUpdate),
                String.format("Trainer with id %d wasn't found!", idNotExist));
    }
}
