package org.gym.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@ToString(callSuper = true)
public class Trainer extends User {
    @JsonProperty("specialization")
    private TrainingType specialization;
}
