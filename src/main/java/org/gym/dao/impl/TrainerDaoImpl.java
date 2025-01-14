package org.gym.dao.impl;

import lombok.AllArgsConstructor;
import org.gym.dao.TrainerDao;
import org.gym.entity.Trainer;
import org.gym.exception.EntityNotFoundException;
import org.gym.storage.TrainerStorage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class TrainerDaoImpl implements TrainerDao {
    private final TrainerStorage trainerStorage;

    @Override
    public List<Trainer> findAll() {
        return trainerStorage.findAll();
    }

    @Override
    public Trainer findById(Long id) throws EntityNotFoundException {
        return trainerStorage.findById(id);
    }

    @Override
    public Trainer save(Trainer trainer) {
        return trainerStorage.save(trainer);
    }

    @Override
    public Trainer update(Long id, Trainer trainer) {
        return trainerStorage.update(id, trainer);
    }

    @Override
    public boolean isUserNameExists(String userName) {
        return trainerStorage.isUserNameExist(userName);
    }
}
