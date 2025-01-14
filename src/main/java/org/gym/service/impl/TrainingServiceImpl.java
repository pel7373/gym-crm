package org.gym.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dao.TrainingDao;
import org.gym.entity.Training;
import org.gym.dto.TrainingDto;
import org.gym.exception.EntityNotFoundException;
import org.gym.exception.InvalidIdException;
import org.gym.exception.NullEntityException;
import org.gym.mapper.TrainingMapper;
import org.gym.service.TrainingService;

import org.springframework.stereotype.Service;

import static org.gym.config.Config.ENTITY_CANT_BE_NULL;
import static org.gym.config.Config.ID_CANT_BE_NULL_OR_NEGATIVE;

@Slf4j
@Service
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {
    private final TrainingDao trainingDAO;
    private final TrainingMapper trainingMapper;

    public TrainingDto getById(Long id) throws InvalidIdException, EntityNotFoundException {
        if(id == null || id < 0) {
            throw new InvalidIdException(String.format(ID_CANT_BE_NULL_OR_NEGATIVE, "getById"));
        }

        LOGGER.info("getById is finding training with id {}", id);
        return trainingMapper.convertToDto(trainingDAO.findById(id));
    }

    public TrainingDto save(TrainingDto trainingDto) throws NullEntityException {
        if(trainingDto == null) {
            throw new NullEntityException(String.format(ENTITY_CANT_BE_NULL, "save"));
        }

        Training training = trainingMapper.convertToEntity(trainingDto);
        Training savedTraining = trainingDAO.save(training);
        LOGGER.info("save has saved training {}", savedTraining);
        return trainingMapper.convertToDto(savedTraining);
    }
}
