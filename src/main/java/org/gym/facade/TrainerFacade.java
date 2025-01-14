package org.gym.facade;

import org.gym.dto.TrainerDto;

public interface TrainerFacade {
    TrainerDto createTrainer(TrainerDto trainerDto);
    TrainerDto getTrainerById(Long id);
    TrainerDto updateTrainer(Long id, TrainerDto trainerDto);
}
