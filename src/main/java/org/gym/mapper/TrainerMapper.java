package org.gym.mapper;

import org.gym.entity.Trainer;
import org.gym.dto.TrainerDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainerMapper {
    TrainerDto convertToDto(Trainer trainer);
    Trainer convertToEntity(TrainerDto trainerDto) ;
}
