package org.gym.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.TrainerDto;
import org.gym.dto.TrainingDto;
import org.gym.exception.InvalidIdException;
import org.gym.exception.NullEntityException;
import org.gym.facade.TrainingFacade;
import org.gym.service.TrainingService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainingFacadeImpl implements TrainingFacade {

    private final TrainingService trainingService;

    @Override
    public TrainingDto createTraining(TrainingDto trainingDto) {
        TrainingDto trainingDtoResult = null;
        try {
            trainingDtoResult = trainingService.save(trainingDto);
        } catch (NullEntityException e) {
            LOGGER.warn(e.getMessage());
        }
        return trainingDtoResult;
    }

    @Override
    public TrainingDto getTrainingById(Long id) {
        TrainingDto trainingDtoResult = null;
        try {
            trainingDtoResult = trainingService.getById(id);
        } catch (InvalidIdException e) {
            LOGGER.warn(e.getMessage());
        }
        return trainingDtoResult;
    }
}
