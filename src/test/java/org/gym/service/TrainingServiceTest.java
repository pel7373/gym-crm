package org.gym.service;

import org.gym.entity.Training;
import org.gym.dto.TrainingDto;
import org.gym.dao.TrainingDao;
import org.gym.exception.EntityNotFoundException;
import org.gym.exception.InvalidIdException;
import org.gym.exception.NullEntityException;
import org.gym.mapper.TrainingMapper;
import org.gym.mapper.TrainingMapperImpl;
import org.gym.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.gym.entity.TrainingType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Spy
    private TrainingMapper spyTrainingMapper = new TrainingMapperImpl();

    private Training training;
    private TrainingDto trainingDto;

    @BeforeEach
    void setUp() {
        training = Training.builder().id(1L).traineeId(1L).trainerId(2L).trainingName("new program for this week").trainingDate(LocalDate.of(2025, 2, 13)).trainingDuration(30L).trainingType(STRETCHING).build();
        trainingDto = spyTrainingMapper.convertToDto(training);
    }

    @Test
    void getByIdIfExist() {
        when(trainingDao.findById(1L)).thenReturn(training);
        TrainingDto mockedTrainingDto = trainingService.getById(1L);
        assertNotNull(mockedTrainingDto);
        verify(trainingDao, times(1)).findById(1L);
    }

    @Test
    void getByIdNotFound() {
        when(trainingDao.findById(1L)).thenThrow(new EntityNotFoundException(String.format("Training with id %d wasn't found!", 1L)));
        assertThrows(EntityNotFoundException.class, () -> trainingService.getById(1L));
        verify(trainingDao, times(1)).findById(1L);
    }

    @Test
    void getByIdNullThenException() {
        assertThrows(InvalidIdException.class, () -> trainingService.getById(null));
        verify(trainingDao, never()).findById(any(Long.class));
    }

    @Test
    void getByIdNegativeIdThenException() {
        assertThrows(InvalidIdException.class, () -> trainingService.getById(-1L));
        verify(trainingDao, never()).findById(any(Long.class));
    }

    @Test
    void createTrainingSuccessfully() {
        when(trainingDao.save(any(Training.class))).thenReturn(training);

        TrainingDto trainingDtoActual = trainingService.save(trainingDto);

        assertNotNull(trainingDtoActual);
        assertEquals(trainingDto, trainingDtoActual);
        verify(trainingDao, times(1)).save(any(Training.class));
    }

    @Test
    void createTrainingIfNullThenNullEntityException() {
        assertThrows(NullEntityException.class, () -> trainingService.save(null));
        verify(trainingDao, never()).save(any(Training.class));
    }
}
