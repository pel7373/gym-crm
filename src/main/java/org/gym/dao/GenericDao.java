package org.gym.dao;

import org.gym.exception.EntityNotFoundException;

public interface GenericDao<T, I> {
    T findById(I id) throws EntityNotFoundException;
    T save(T t);
}
