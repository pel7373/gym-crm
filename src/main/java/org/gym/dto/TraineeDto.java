package org.gym.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
@SuperBuilder
public class TraineeDto implements Serializable {
    private long id;
    @ToString.Exclude
    private String firstName;
    @ToString.Exclude
    private String lastName;
    private String userName;
    private Boolean isActive;
    @ToString.Exclude
    private LocalDate dateOfBirth;
    @ToString.Exclude
    private String address;
}
