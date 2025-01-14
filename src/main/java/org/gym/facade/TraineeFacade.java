package org.gym.facade;

import org.gym.dto.TraineeDto;

public interface TraineeFacade {
    TraineeDto createTrainee(TraineeDto traineeDto);
    TraineeDto getTraineeById(Long id);
    TraineeDto updateTrainee(Long id, TraineeDto traineeDto);
    void deleteTrainee(Long id);
}
