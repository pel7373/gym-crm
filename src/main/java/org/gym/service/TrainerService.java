package org.gym.service;

import org.gym.dto.TrainerDto;
import org.gym.exception.EntityNotFoundException;
import org.gym.exception.InvalidIdException;
import org.gym.exception.NullEntityException;

public interface TrainerService {
    TrainerDto getById(Long id) throws InvalidIdException, EntityNotFoundException;
    TrainerDto save(TrainerDto trainerDto) throws NullEntityException;
    TrainerDto update(Long id, TrainerDto trainerDto) throws InvalidIdException, NullEntityException, EntityNotFoundException;
}
