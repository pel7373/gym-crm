package org.gym.dao.impl;

import lombok.AllArgsConstructor;
import org.gym.dao.TraineeDao;
import org.gym.entity.Trainee;
import org.gym.exception.EntityNotFoundException;
import org.gym.storage.TraineeStorage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class TraineeDaoImpl implements TraineeDao {
    private final TraineeStorage traineeStorage;

    @Override
    public List<Trainee> findAll() {
        return traineeStorage.findAll();
    }

    @Override
    public Trainee findById(Long id) throws EntityNotFoundException {
        return traineeStorage.findById(id);
    }

    @Override
    public Trainee save(Trainee trainee) {
        return traineeStorage.save(trainee);
    }

    @Override
    public Trainee update(Long id, Trainee trainee) { return traineeStorage.update(id, trainee);
    }

    @Override
    public void deleteById(Long id) {
        traineeStorage.deleteById(id);
    }

    @Override
    public boolean isUserNameExists(String userName) {
        return traineeStorage.isUserNameExist(userName);
    }
}
