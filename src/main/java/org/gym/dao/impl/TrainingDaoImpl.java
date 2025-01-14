package org.gym.dao.impl;

import lombok.AllArgsConstructor;
import org.gym.dao.TrainingDao;
import org.gym.entity.Training;
import org.gym.storage.TrainingStorage;
import org.gym.exception.EntityNotFoundException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TrainingDaoImpl implements TrainingDao {
    private final TrainingStorage trainingStorage;

    @Override
    public Training findById(Long id) throws EntityNotFoundException {
        return trainingStorage.findById(id);
    }

    @Override
    public Training save(Training training) {
        return trainingStorage.save(training);
    }
}
