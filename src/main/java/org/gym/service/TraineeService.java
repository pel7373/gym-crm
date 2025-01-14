package org.gym.service;

import org.gym.dto.TraineeDto;
import org.gym.exception.EntityNotFoundException;
import org.gym.exception.InvalidIdException;
import org.gym.exception.NullEntityException;

public interface TraineeService {
    TraineeDto getById(Long id) throws InvalidIdException, EntityNotFoundException;
    TraineeDto save(TraineeDto traineeDto) throws NullEntityException;
    TraineeDto update(Long id, TraineeDto traineeDto) throws InvalidIdException, NullEntityException, EntityNotFoundException;
    void deleteById(Long id) throws InvalidIdException;
}
