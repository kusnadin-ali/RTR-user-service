package com.tujuhsembilan.user_service.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tujuhsembilan.core.utils.JwtUtil;

import io.jsonwebtoken.Claims;

@Component
public class ValidateRoleUtil {

    @Autowired
    private JwtUtil jwtUtil;

    public boolean validateRole(String header, List<String> roles) {
        try {
            String token = header.replace("Bearer ", "");

            Claims claims = jwtUtil.extractAllClaims(token);

            String userType = claims.get("userType", String.class);

            return roles.contains(userType);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}
