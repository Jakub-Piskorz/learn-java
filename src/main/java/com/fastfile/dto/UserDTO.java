package com.fastfile.dto;

import com.fastfile.model.User;
import lombok.*;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String userType;
    private long usedStorage;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.userType = user.getUserType();
        this.usedStorage = user.getUsedStorage();
    }
}