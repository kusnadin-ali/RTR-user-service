package com.tujuhsembilan.user_service.controller;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tujuhsembilan.user_service.dto.Auth.AuthenticationRequest;
import com.tujuhsembilan.user_service.dto.Auth.AuthenticationResponse;
import com.tujuhsembilan.user_service.dto.User.UserCustomerDto;
import com.tujuhsembilan.user_service.security.jwt.JwtUtil;
import com.tujuhsembilan.user_service.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Tag(name = "Authentication", description = "API untuk autentikasi")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private final UserService userService;
    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class.getName());

    @PostMapping("/login")
    @Operation(summary = "Login untuk mendapatkan token JWT")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest) throws BadCredentialsException {
        try {
            LOG.info(authenticationRequest.toString());
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException badCredentialsException) {
            LOG.error("Incorrect username or password");
            throw badCredentialsException;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
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
