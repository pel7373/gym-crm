package org.gym.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dto.TrainerDto;
import org.gym.exception.EntityNotFoundException;
import org.gym.exception.InvalidIdException;
import org.gym.exception.NullEntityException;
import org.gym.facade.TrainerFacade;
import org.gym.service.TrainerService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrainerFacadeImpl implements TrainerFacade {

    private final TrainerService trainerService;

    @Override
    public TrainerDto createTrainer(TrainerDto trainerDto) {
        TrainerDto trainerDtoResult = null;
        try {
            trainerDtoResult = trainerService.save(trainerDto);
        } catch (NullEntityException e) {
            LOGGER.warn(e.getMessage());
        }
        return trainerDtoResult;
    }

    @Override
    public TrainerDto getTrainerById(Long id) {
        TrainerDto trainerDtoResult = null;
        try {
            trainerDtoResult = trainerService.getById(id);
        } catch (InvalidIdException e) {
            LOGGER.warn(e.getMessage());
        }
        return trainerDtoResult;
    }

    @Override
    public TrainerDto updateTrainer(Long id, TrainerDto trainerDto) {
        TrainerDto trainerDtoResult = null;
        try {
            trainerDtoResult = trainerService.update(id, trainerDto);
        } catch (InvalidIdException | NullEntityException | EntityNotFoundException e) {
            LOGGER.warn(e.getMessage());
        }
        return trainerDtoResult;
    }
}
