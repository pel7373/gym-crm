package org.gym.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dao.TraineeDao;
import org.gym.entity.Trainee;
import org.gym.dto.TraineeDto;
import org.gym.exception.EntityNotFoundException;
import org.gym.exception.InvalidIdException;
import org.gym.exception.NullEntityException;
import org.gym.mapper.TraineeMapper;
import org.gym.service.PasswordGeneratorService;
import org.gym.service.TraineeService;
import org.gym.service.UserNameGeneratorService;
import org.springframework.stereotype.Service;

import static org.gym.config.Config.ENTITY_CANT_BE_NULL;
import static org.gym.config.Config.ID_CANT_BE_NULL_OR_NEGATIVE;

@Slf4j
@Service
@AllArgsConstructor
public class TraineeServiceImpl implements TraineeService {
    private final TraineeDao traineeDao;
    private final TraineeMapper traineeMapper;
    private final UserNameGeneratorService userNameGeneratorService;
    private final PasswordGeneratorService passwordGeneratorService;

    @Override
    public TraineeDto getById(Long id) throws InvalidIdException, EntityNotFoundException {
        if(id == null || id < 0) {
            throw new InvalidIdException(String.format(ID_CANT_BE_NULL_OR_NEGATIVE, "getById"));
        }

        LOGGER.info("getById is finding trainee with id {}", id);
        return traineeMapper.convertToDto(traineeDao.findById(id));
    }

    @Override
    public TraineeDto save(TraineeDto traineeDto) throws NullEntityException {
        if(traineeDto == null) {
            throw new NullEntityException(String.format(ENTITY_CANT_BE_NULL, "save"));
        }

        Trainee trainee = traineeMapper.convertToEntity(traineeDto);
        trainee.setUserName(userNameGeneratorService.generate(trainee.getFirstName(), trainee.getLastName()));
        trainee.setPassword(passwordGeneratorService.generate());

        Trainee savedTrainee = traineeDao.save(trainee);
        LOGGER.info("save has saved trainee {}", savedTrainee);
        return traineeMapper.convertToDto(savedTrainee);
    }

    @Override
    public TraineeDto update(Long id, TraineeDto traineeDto) throws InvalidIdException, NullEntityException, EntityNotFoundException {
        if(id == null || id < 0) {
            throw new InvalidIdException(String.format(ID_CANT_BE_NULL_OR_NEGATIVE, "update"));
        }

        if(traineeDto == null) {
            throw new NullEntityException(String.format(ENTITY_CANT_BE_NULL, "update"));
        }

        Trainee oldTrainee = traineeDao.findById(id);
        Trainee trainee = traineeMapper.convertToEntity(traineeDto);
        trainee.setUserName(userNameGeneratorService.generate(trainee.getFirstName(), trainee.getLastName()));
        trainee.setId(id);
        trainee.setPassword(oldTrainee.getPassword());
        LOGGER.info("update is updating trainee with id {}", id);
        return traineeMapper.convertToDto(traineeDao.update(id, trainee));
    }

    @Override
    public void deleteById(Long id) throws InvalidIdException {
        if(id == null || id < 0) {
            throw new InvalidIdException(String.format(ID_CANT_BE_NULL_OR_NEGATIVE, "delete"));
        }

        LOGGER.info("deleteById is deleting trainee with id {}", id);
        traineeDao.deleteById(id);
    }
}
