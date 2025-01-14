package org.gym.mapper;

import org.gym.entity.Trainee;
import org.gym.dto.TraineeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TraineeMapper {
    TraineeDto convertToDto(Trainee trainee);
    Trainee convertToEntity(TraineeDto traineeDto) ;
}
