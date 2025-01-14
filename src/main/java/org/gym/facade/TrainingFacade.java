package org.gym.facade;

import org.gym.dto.TrainingDto;

public interface TrainingFacade {
    TrainingDto createTraining(TrainingDto trainingDto);
    TrainingDto getTrainingById(Long id);
}
