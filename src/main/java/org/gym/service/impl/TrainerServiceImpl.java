package org.gym.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gym.dao.TrainerDao;
import org.gym.entity.Trainer;
import org.gym.dto.TrainerDto;
import org.gym.exception.EntityNotFoundException;
import org.gym.exception.InvalidIdException;
import org.gym.exception.NullEntityException;
import org.gym.mapper.TrainerMapper;
import org.gym.service.PasswordGeneratorService;
import org.gym.service.TrainerService;
import org.gym.service.UserNameGeneratorService;
import org.springframework.stereotype.Service;

import static org.gym.config.Config.ENTITY_CANT_BE_NULL;
import static org.gym.config.Config.ID_CANT_BE_NULL_OR_NEGATIVE;

@Slf4j
@Service
@AllArgsConstructor
public class TrainerServiceImpl implements TrainerService {
    private final TrainerDao trainerDao;
    private final TrainerMapper trainerMapper;
    private final UserNameGeneratorService userNameGeneratorService;
    private final PasswordGeneratorService passwordGeneratorService;

    @Override
    public TrainerDto getById(Long id) throws InvalidIdException, EntityNotFoundException {
        if(id == null || id < 0) {
            throw new InvalidIdException(String.format(ID_CANT_BE_NULL_OR_NEGATIVE, "getById"));
        }

        LOGGER.info("getById is finding trainer with id {}", id);
        return trainerMapper.convertToDto(trainerDao.findById(id));
    }

    @Override
    public TrainerDto save(TrainerDto trainerDto) throws NullEntityException {
        if(trainerDto == null) {
            throw new NullEntityException(String.format(ENTITY_CANT_BE_NULL, "save"));
        }

        Trainer trainer = trainerMapper.convertToEntity(trainerDto);
        trainer.setUserName(userNameGeneratorService.generate(trainer.getFirstName(), trainer.getLastName()));
        trainer.setPassword(passwordGeneratorService.generate());

        Trainer savedTrainer = trainerDao.save(trainer);
        LOGGER.info("save has saved trainer {}", savedTrainer);
        return trainerMapper.convertToDto(savedTrainer);
    }

    @Override
    public TrainerDto update(Long id, TrainerDto trainerDto) throws InvalidIdException, NullEntityException, EntityNotFoundException {
        if(id == null || id < 0) {
            throw new InvalidIdException(String.format(ID_CANT_BE_NULL_OR_NEGATIVE, "update"));
        }
        if(trainerDto == null) {
            throw new NullEntityException(String.format(ENTITY_CANT_BE_NULL, "update"));
        }

        Trainer oldTrainer = trainerDao.findById(id);
        Trainer trainer = trainerMapper.convertToEntity(trainerDto);
        trainer.setUserName(userNameGeneratorService.generate(trainer.getFirstName(), trainer.getLastName()));
        trainer.setId(id);
        trainer.setPassword(oldTrainer.getPassword());
        LOGGER.info("update is updating trainer with id {}", id);
        return trainerMapper.convertToDto(trainerDao.update(id, trainer));
    }
}
