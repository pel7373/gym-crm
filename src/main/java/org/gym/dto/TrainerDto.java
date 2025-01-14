package org.gym.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.gym.entity.TrainingType;

import java.io.Serializable;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@SuperBuilder
public class TrainerDto implements Serializable {
    private long id;
    @ToString.Exclude
    private String firstName;
    @ToString.Exclude
    private String lastName;
    private String userName;
    private Boolean isActive;
    private TrainingType specialization;
}
