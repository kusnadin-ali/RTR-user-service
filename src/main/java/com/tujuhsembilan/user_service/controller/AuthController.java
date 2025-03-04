package com.tujuhsembilan.user_service.controller;

import java.util.Objects;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tujuhsembilan.core.utils.ResponseUtil;
import com.tujuhsembilan.user_service.dto.Auth.AuthenticationRequest;
import com.tujuhsembilan.user_service.dto.Auth.AuthenticationResponse;
import com.tujuhsembilan.user_service.dto.User.UserCustomerDto;
import com.tujuhsembilan.user_service.model.User;
import com.tujuhsembilan.core.utils.JwtUtil;
import com.tujuhsembilan.user_service.service.UserService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Claims claims = jwtUtil.extractAllClaims(token);
            return ResponseEntity.ok(claims);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid Token");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) {
        // Cari user berdasarkan username
        Optional<User> userOptional = userService.getUserByUsername(request.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Bandingkan password yang diinput dengan yang ada di database
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername(), user.getUserType());
                AuthenticationResponse response = new AuthenticationResponse(token);
                return ResponseUtil.success(response);
            }
        }

        return ResponseUtil.error(null, "01", "Invalid username or password", 401);
    }
    

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserCustomerDto userDto) {
        if (Objects.isNull(userDto)) {
            throw new Error("Payload cannot be Null");
        }
        if (userService.isUserExistByUsername(userDto.getUsername())) {
            throw new Error("Username is already taken");
        }
        System.out.println("masuk");
        
        return userService.saveUser(userDto);
    }
}
