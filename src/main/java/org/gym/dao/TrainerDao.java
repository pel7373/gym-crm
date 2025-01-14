package org.gym.dao;

import org.gym.entity.Trainer;

import java.util.List;

public interface TrainerDao extends GenericDao<Trainer, Long>{
    List<Trainer> findAll();
    Trainer update(Long id, Trainer t);
    boolean isUserNameExists(String userName);
}
