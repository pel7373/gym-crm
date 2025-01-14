package org.gym.service;

import org.gym.dto.TrainingDto;
import org.gym.exception.EntityNotFoundException;
import org.gym.exception.InvalidIdException;
import org.gym.exception.NullEntityException;

public interface TrainingService {
    TrainingDto getById(Long id) throws InvalidIdException, EntityNotFoundException;
    TrainingDto save(TrainingDto trainingDto) throws NullEntityException;
}
