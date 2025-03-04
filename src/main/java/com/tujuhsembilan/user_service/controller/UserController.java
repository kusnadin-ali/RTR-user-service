package com.tujuhsembilan.user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tujuhsembilan.user_service.dto.User.UserRestaurantCreateDto;
import com.tujuhsembilan.user_service.dto.User.UserUpdateDto;
import com.tujuhsembilan.user_service.service.UserService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/restaurant")
    @PreAuthorize("@roleEvaluator.hasAnyRole('ROLE_STAFF', 'ROLE_ADMIN')")
    public ResponseEntity<?> getRestaurant() {
        return userService.getListStaffUserRestaurant();
    }

    @GetMapping("/customer")
    @PreAuthorize("@roleEvaluator.hasAnyRole('ROLE_STAFF', 'ROLE_ADMIN')")
    public ResponseEntity<?> getCustomers() {
        return userService.getListCustomer();
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editUserDetail(
            @RequestBody UserUpdateDto request
    ) {
        return userService.updateDetailUser(request);
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("@roleEvaluator.hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUserDetail( @PathVariable String username) {
        return userService.deleteUser(username);
    }

    @PostMapping("/restaurant/add")
    @PreAuthorize("@roleEvaluator.hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addRestaurant( @RequestBody UserRestaurantCreateDto request) {
        return userService.addStaffOrAdminRestaurant(request);
    }
}
