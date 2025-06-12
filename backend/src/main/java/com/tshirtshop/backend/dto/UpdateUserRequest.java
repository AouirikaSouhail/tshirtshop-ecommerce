package com.tshirtshop.backend.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateUserRequest {
    private String fullName;
    private String email;
    private String password;
}
