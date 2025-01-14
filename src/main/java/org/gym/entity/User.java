package org.gym.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"firstName", "lastName", "password"})
public class User implements Identifiable {
    private long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    private Boolean isActive;
}
