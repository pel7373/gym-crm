package org.gym.service;

import org.gym.entity.Trainer;
import org.gym.dto.TrainerDto;
import org.gym.dao.TrainerDao;
import org.gym.exception.EntityNotFoundException;
import org.gym.exception.InvalidIdException;
import org.gym.exception.NullEntityException;
import org.gym.mapper.TrainerMapper;
import org.gym.mapper.TrainerMapperImpl;
import org.gym.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.gym.entity.TrainingType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private UserNameGeneratorService userNameGeneratorService;

    @Mock
    private PasswordGeneratorService passwordGeneratorService;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Spy
    private TrainerMapper spyTrainerMapper = new TrainerMapperImpl();

    private Trainer trainer;
    private TrainerDto trainerDto;
    private Trainer trainerForUpdate;
    private Trainer trainerUpdated;
    private TrainerDto trainerDtoUpdated;

    @BeforeEach
    void setUp() {
        trainer = Trainer.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .userName("John.Doe")
                .password("AAAAAAAAAA")
                .isActive(true)
                .specialization(YOGA)
                .build();
        trainerDto = spyTrainerMapper.convertToDto(trainer);

        trainerForUpdate = Trainer.builder()
                .id(2L)
                .firstName("Maria")
                .lastName("Ivanova")
                .userName("Maria.Ivanova")
                .password("BBBBBBBBBB")
                .isActive(true)
                .specialization(YOGA)
                .build();

        trainerUpdated = Trainer.builder()
                .id(2L)
                .firstName("John")
                .lastName("Doe")
                .userName("John.Doe0")
                .password("BBBBBBBBBB")
                .isActive(true)
                .specialization(YOGA)
                .build();
        trainerDtoUpdated = spyTrainerMapper.convertToDto(trainerUpdated);
    }

    @Test
    void getByIdIfExist() {
        when(trainerDao.findById(1L)).thenReturn(trainer);
        TrainerDto mockedTrainerDto = trainerService.getById(1L);
        assertNotNull(mockedTrainerDto);
        assertEquals("John", mockedTrainerDto.getFirstName());
        verify(trainerDao, times(1)).findById(1L);
    }

    @Test
    void getByIdNotFound() {
        when(trainerDao.findById(1L)).thenThrow(new EntityNotFoundException(String.format("Trainer with id %d wasn't found!", 1L)));
        assertThrows(EntityNotFoundException.class, () -> trainerService.getById(1L));
        verify(trainerDao, times(1)).findById(1L);
    }

    @Test
    void getByIdNullThenException() {
        assertThrows(InvalidIdException.class, () -> trainerService.getById(null));
        verify(trainerDao, never()).findById(any(Long.class));
    }

    @Test
    void getByIdNegativeIdThenException() {
        assertThrows(InvalidIdException.class, () -> trainerService.getById(-1L));
        verify(trainerDao, never()).findById(any(Long.class));
    }

    @Test
    void createTrainerSuccessfully() {
        when(userNameGeneratorService.generate(trainerDto.getFirstName(), trainerDto.getLastName())).thenReturn("John.Doe");
        when(passwordGeneratorService.generate()).thenReturn("AAAAAAAAAA");
        when(trainerDao.save(any(Trainer.class))).thenReturn(trainer);

        TrainerDto trainerDtoActual = trainerService.save(trainerDto);

        assertNotNull(trainerDtoActual);
        assertEquals(trainerDto, trainerDtoActual);
        verify(userNameGeneratorService, times(1)).generate(any(String.class), any(String.class));
        verify(trainerDao, times(1)).save(any(Trainer.class));
    }

    @Test
    void createTrainerIfNullThenNullEntityException() {
        assertThrows(NullEntityException.class, () -> trainerService.save(null));
        verify(trainerDao, never()).save(any(Trainer.class));
    }

    @Test
    void updateExistingTrainerSuccessfully() {
        when(trainerDao.findById(2L)).thenReturn(trainerForUpdate);
        when(userNameGeneratorService.generate("John", "Doe")).thenReturn("John.Doe0");
        //it's the key point - the service has prepared the correct entity for the update:
        when(trainerDao.update(2L, trainerUpdated)).thenReturn(trainerUpdated);
        TrainerDto trainerDtoActual = trainerService.update(2L, trainerDto);

        assertNotNull(trainerDtoActual);
        assertEquals(trainerDtoUpdated, trainerDtoActual);
        verify(trainerDao, times(1)).findById(2L);
        verify(trainerDao, times(1)).update(eq(2L), any(Trainer.class));
    }

    @Test
    void updateNonExistingTrainerThenException() {
        when(trainerDao.findById(3L)).thenThrow(new EntityNotFoundException(" "));
        assertThrows(EntityNotFoundException.class, () -> trainerService.update(3L, trainerDto));
        verify(trainerDao, times(1)).findById(3L);
        verify(trainerDao, never()).update(any(Long.class), any(Trainer.class));
    }

    @Test
    void updateNullTrainerThenException() {
        assertThrows(NullEntityException.class, () -> trainerService.update(3L, null));
        verify(trainerDao, never()).findById(any(Long.class));
        verify(trainerDao, never()).update(any(Long.class), any(Trainer.class));
    }

    @Test
    void updateNullIdThenException() {
        assertThrows(InvalidIdException.class, () -> trainerService.update(null, trainerDto));
        verify(trainerDao, never()).findById(any(Long.class));
        verify(trainerDao, never()).update(any(Long.class), any(Trainer.class));
    }

    @Test
    void updateNegativeIdThenException() {
        assertThrows(InvalidIdException.class, () -> trainerService.update(-1L, trainerDto));
        verify(trainerDao, never()).findById(any(Long.class));
        verify(trainerDao, never()).update(any(Long.class), any(Trainer.class));
    }
}
