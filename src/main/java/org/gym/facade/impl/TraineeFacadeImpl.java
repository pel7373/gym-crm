package org.gym.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.TraineeDto;
import org.gym.exception.EntityNotFoundException;
import org.gym.exception.InvalidIdException;
import org.gym.exception.NullEntityException;
import org.gym.facade.TraineeFacade;
import org.gym.service.TraineeService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TraineeFacadeImpl implements TraineeFacade {

    private final TraineeService traineeService;

    @Override
    public TraineeDto createTrainee(TraineeDto traineeDto) {
        TraineeDto traineeDtoResult = null;
        try {
            traineeDtoResult = traineeService.save(traineeDto);
        } catch (NullEntityException e) {
            LOGGER.warn(e.getMessage());
        }
        return traineeDtoResult;
    }

    @Override
        public TraineeDto getTraineeById(Long id) {
        TraineeDto traineeDtoResult = null;
        try {
            traineeDtoResult = traineeService.getById(id);
        } catch (InvalidIdException e) {
            LOGGER.warn(e.getMessage());
        }
        return traineeDtoResult;
    }

    @Override
    public TraineeDto updateTrainee(Long id, TraineeDto traineeDto) {
        TraineeDto traineeDtoResult = null;
        try {
            traineeDtoResult = traineeService.update(id, traineeDto);
        } catch (InvalidIdException | NullEntityException | EntityNotFoundException e) {
            LOGGER.warn(e.getMessage());
        }
        return traineeDtoResult;
    }

    @Override
    public void deleteTrainee(Long id) {
        try {
            traineeService.deleteById(id);
        } catch (InvalidIdException e) {
            LOGGER.warn(e.getMessage());
        }
    }
}
