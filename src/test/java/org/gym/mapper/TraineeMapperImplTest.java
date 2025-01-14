package org.gym.mapper;

import org.gym.entity.Trainee;
import org.gym.dto.TraineeDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TraineeMapperImplTest {

    private final TraineeMapper traineeMapper = new TraineeMapperImpl();

    @Test
    void convertToDto() {
        Trainee trainee = Trainee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .userName("John.Doe")
                .password("AAAAAAAAAA")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnytsia, Soborna str. 35, ap. 26")
                .build();

        TraineeDto traineeDto = traineeMapper.convertToDto(trainee);

        assertNotNull(traineeDto);
        assertEquals(trainee.getId(), traineeDto.getId());
        assertEquals(trainee.getFirstName(), traineeDto.getFirstName());
        assertEquals(trainee.getLastName(), traineeDto.getLastName());
        assertEquals(trainee.getUserName(), traineeDto.getUserName());
        assertEquals(trainee.getIsActive(), traineeDto.getIsActive());
        assertEquals(trainee.getDateOfBirth(), traineeDto.getDateOfBirth());
        assertEquals(trainee.getAddress(), traineeDto.getAddress());
    }

    @Test
    void convertToDtoWithNullTrainee() {
        TraineeDto traineeDto = traineeMapper.convertToDto(null);
        assertNull(traineeDto, "Expected result: convertToDto returns null when input is null");
    }

    @Test
    void convertToEntity() {
        TraineeDto traineeDto = TraineeDto.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .userName("John.Doe")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnytsia, Soborna str. 35, ap. 26")
                .build();

        Trainee trainee = traineeMapper.convertToEntity(traineeDto);

        assertNotNull(trainee);
        assertEquals(traineeDto.getId(), trainee.getId());
        assertEquals(traineeDto.getFirstName(), trainee.getFirstName());
        assertEquals(traineeDto.getLastName(), trainee.getLastName());
        assertEquals(traineeDto.getUserName(), trainee.getUserName());
        assertEquals(traineeDto.getIsActive(), trainee.getIsActive());
        assertEquals(traineeDto.getDateOfBirth(), trainee.getDateOfBirth());
        assertEquals(traineeDto.getAddress(), trainee.getAddress());
    }

    @Test
    void convertToEntityWithNullTraineeDto() {
        Trainee trainee = traineeMapper.convertToEntity(null);
        assertNull(trainee, "Expected result: convertToEntity returns null when input is null");
    }
}
