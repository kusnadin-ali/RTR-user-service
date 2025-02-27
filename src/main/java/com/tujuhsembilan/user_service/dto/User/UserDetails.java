package com.tujuhsembilan.user_service.dto.User;

import com.tujuhsembilan.user_service.constant.UserTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {
    private String name;

    private String email;

    private String username;

    private UserTypeEnum userType;
}
