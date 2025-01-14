package org.gym.dao;

import org.gym.entity.Trainee;

import java.util.List;

public interface TraineeDao extends GenericDao<Trainee, Long> {
    List<Trainee> findAll();
    Trainee update(Long id, Trainee t);
    void deleteById(Long id);
    boolean isUserNameExists(String userName);
}
