package org.gym.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Training implements Identifiable {
    private long id;
    private long traineeId;
    private long trainerId;
    private String trainingName;
    @JsonProperty("trainingType")
    private TrainingType trainingType;
    private LocalDate trainingDate;
    private long trainingDuration;
}
