package org.gym.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@ToString(callSuper = true)
public class Trainee extends User {
    @ToString.Exclude
    private LocalDate dateOfBirth;
    @ToString.Exclude
    private String address;
}
