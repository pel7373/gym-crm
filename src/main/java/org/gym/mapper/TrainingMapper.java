package org.gym.mapper;

import org.gym.entity.Training;
import org.gym.dto.TrainingDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainingMapper {
        TrainingDto convertToDto(Training training);
        Training convertToEntity(TrainingDto trainingDto);
}
