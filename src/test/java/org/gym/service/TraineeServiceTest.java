package org.gym.service;

import org.gym.dao.TraineeDao;
import org.gym.entity.Trainee;
import org.gym.dto.TraineeDto;
import org.gym.exception.EntityNotFoundException;
import org.gym.exception.InvalidIdException;
import org.gym.exception.NullEntityException;
import org.gym.mapper.TraineeMapper;
import org.gym.mapper.TraineeMapperImpl;
import org.gym.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private UserNameGeneratorService userNameGeneratorService;

    @Mock
    private PasswordGeneratorService passwordGeneratorService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Spy
    private TraineeMapper spyTraineeMapper = new TraineeMapperImpl();

    private Trainee trainee;
    private TraineeDto traineeDto;
    private Trainee traineeForUpdate;
    private Trainee traineeUpdated;
    private TraineeDto traineeDtoUpdated;

    @BeforeEach
    void setUp() {
        trainee = Trainee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .userName("John.Doe")
                .password("AAAAAAAAAA")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();
        traineeDto = spyTraineeMapper.convertToDto(trainee);

        traineeForUpdate = Trainee.builder()
                .id(2L)
                .firstName("Maria")
                .lastName("Ivanova")
                .userName("Maria.Ivanova")
                .password("BBBBBBBBBB")
                .isActive(true)
                .dateOfBirth(LocalDate.of(2000, 2, 13))
                .address("Kyiv, Soborna str. 35, ap. 26")
                .build();

        traineeUpdated = Trainee.builder()
                .id(2L)
                .firstName("John")
                .lastName("Doe")
                .userName("John.Doe0")
                .password("BBBBBBBBBB")
                .isActive(true)
                .dateOfBirth(LocalDate.of(1995, 1, 23))
                .address("Vinnitsya, Soborna str. 35, ap. 26")
                .build();
        traineeDtoUpdated = spyTraineeMapper.convertToDto(traineeUpdated);
    }

    @Test
    void getByIdIfExist() {
        when(traineeDao.findById(1L)).thenReturn(trainee);
        TraineeDto mockedTraineeDto = traineeService.getById(1L);
        assertNotNull(mockedTraineeDto);
        assertEquals("John", mockedTraineeDto.getFirstName());
        verify(traineeDao, times(1)).findById(1L);
    }

    @Test
    void getByIdNotFound() {
        when(traineeDao.findById(1L)).thenThrow(new EntityNotFoundException(String.format("Trainee with id %d wasn't found!", 1L)));
        assertThrows(EntityNotFoundException.class, () -> traineeService.getById(1L));
        verify(traineeDao, times(1)).findById(1L);
    }

    @Test
    void getByIdNullThenException() {
        assertThrows(InvalidIdException.class, () -> traineeService.getById(null));
        verify(traineeDao, never()).findById(any(Long.class));
    }

    @Test
    void getByIdNegativeIdThenException() {
        assertThrows(InvalidIdException.class, () -> traineeService.getById(-1L));
        verify(traineeDao, never()).findById(any(Long.class));
    }

    @Test
    void createTraineeSuccessfully() {
        when(userNameGeneratorService.generate(traineeDto.getFirstName(), traineeDto.getLastName())).thenReturn("John.Doe");
        when(passwordGeneratorService.generate()).thenReturn("AAAAAAAAAA");
        when(traineeDao.save(any(Trainee.class))).thenReturn(trainee);

        TraineeDto traineeDtoActual = traineeService.save(traineeDto);

        assertNotNull(traineeDtoActual);
        assertEquals(traineeDto, traineeDtoActual);
        verify(userNameGeneratorService, times(1)).generate(any(String.class), any(String.class));
        verify(traineeDao, times(1)).save(any(Trainee.class));
    }

    @Test
    void createTraineeIfNullThenNullEntityException() {
        assertThrows(NullEntityException.class, () -> traineeService.save(null));
        verify(traineeDao, never()).save(any(Trainee.class));
    }

    @Test
    void updateExistingTraineeSuccessfully() {
        when(traineeDao.findById(2L)).thenReturn(traineeForUpdate);
        when(userNameGeneratorService.generate("John", "Doe")).thenReturn("John.Doe0");
        //it's the key point - the service has prepared the correct entity for the update:
        when(traineeDao.update(2L, traineeUpdated)).thenReturn(traineeUpdated);
        TraineeDto traineeDtoActual = traineeService.update(2L, traineeDto);

        assertNotNull(traineeDtoActual);
        assertEquals(traineeDtoUpdated, traineeDtoActual);
        verify(traineeDao, times(1)).findById(2L);
        verify(traineeDao, times(1)).update(eq(2L), any(Trainee.class));
    }

    @Test
    void updateNonExistingTraineeThenException() {
        when(traineeDao.findById(3L)).thenThrow(new EntityNotFoundException(" "));
        assertThrows(EntityNotFoundException.class, () -> traineeService.update(3L, traineeDto));
        verify(traineeDao, times(1)).findById(3L);
        verify(traineeDao, never()).update(any(Long.class), any(Trainee.class));
    }

    @Test
    void updateNullTraineeThenException() {
        assertThrows(NullEntityException.class, () -> traineeService.update(3L, null));
        verify(traineeDao, never()).findById(any(Long.class));
        verify(traineeDao, never()).update(any(Long.class), any(Trainee.class));
    }

    @Test
    void updateNullIdThenException() {
        assertThrows(InvalidIdException.class, () -> traineeService.update(null, traineeDto));
        verify(traineeDao, never()).findById(any(Long.class));
        verify(traineeDao, never()).update(any(Long.class), any(Trainee.class));
    }

    @Test
    void updateNegativeIdThenException() {
        assertThrows(InvalidIdException.class, () -> traineeService.update(-1L, traineeDto));
        verify(traineeDao, never()).findById(any(Long.class));
        verify(traineeDao, never()).update(any(Long.class), any(Trainee.class));
    }

    @Test
    void deleteByIdTraineeSuccessfully() {
        traineeService.deleteById(2L);
        verify(traineeDao, times(1)).deleteById(2L);
    }

    @Test
    void deleteByIdNullIdThenException() {
        assertThrows(InvalidIdException.class, () -> traineeService.deleteById(null));
        verify(traineeDao, never()).deleteById(any(Long.class));
    }

    @Test
    void deleteByIdNegativeIdThenException() {
        assertThrows(InvalidIdException.class, () -> traineeService.deleteById(-1L));
        verify(traineeDao, never()).deleteById(any(Long.class));
    }
}
