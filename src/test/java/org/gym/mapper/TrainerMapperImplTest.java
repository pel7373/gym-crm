package org.gym.mapper;

import org.gym.entity.Trainer;
import org.gym.dto.TrainerDto;
import org.junit.jupiter.api.Test;

import static org.gym.entity.TrainingType.YOGA;
import static org.junit.jupiter.api.Assertions.*;

class TrainerMapperImplTest {

    private final TrainerMapper trainerMapper = new TrainerMapperImpl();

    @Test
    void convertToDto() {
        Trainer trainer = Trainer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .userName("John.Doe")
                .password("AAAAAAAAAA")
                .isActive(true)
                .specialization(YOGA)
                .build();

        TrainerDto trainerDto = trainerMapper.convertToDto(trainer);

        assertNotNull(trainerDto);
        assertEquals(trainer.getId(), trainerDto.getId());
        assertEquals(trainer.getFirstName(), trainerDto.getFirstName());
        assertEquals(trainer.getLastName(), trainerDto.getLastName());
        assertEquals(trainer.getUserName(), trainerDto.getUserName());
        assertEquals(trainer.getIsActive(), trainerDto.getIsActive());
        assertEquals(trainer.getSpecialization(), trainerDto.getSpecialization());
    }

    @Test
    void convertToDtoWithNullTrainer() {
        TrainerDto trainerDto = trainerMapper.convertToDto(null);
        assertNull(trainerDto, "Expected result: convertToDto returns null when input is null");
    }

    @Test
    void convertToEntity() {
        TrainerDto trainerDto = TrainerDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .userName("John.Doe")
                .isActive(true)
                .specialization(YOGA)
                .build();

        Trainer trainer = trainerMapper.convertToEntity(trainerDto);

        assertNotNull(trainer);
        assertEquals(trainerDto.getId(), trainer.getId());
        assertEquals(trainerDto.getFirstName(), trainer.getFirstName());
        assertEquals(trainerDto.getLastName(), trainer.getLastName());
        assertEquals(trainerDto.getUserName(), trainer.getUserName());
        assertEquals(trainerDto.getIsActive(), trainer.getIsActive());
        assertEquals(trainerDto.getSpecialization(), trainer.getSpecialization());
    }

    @Test
    void convertToEntityWithNullTraineeDto() {
        Trainer trainer = trainerMapper.convertToEntity(null);
        assertNull(trainer, "Expected result: convertToEntity returns null when input is null");
    }
}
