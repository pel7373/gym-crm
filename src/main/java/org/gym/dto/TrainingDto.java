package org.gym.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.gym.entity.TrainingType;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@SuperBuilder
public class TrainingDto implements Serializable {
    private long id;
    private long traineeId;
    private long trainerId;
    private String trainingName;
    @JsonProperty("trainingType")
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private long trainingDuration;
}
