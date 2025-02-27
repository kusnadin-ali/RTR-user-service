package com.tujuhsembilan.user_service.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tujuhsembilan.user_service.constant.UserTypeEnum;
import com.tujuhsembilan.user_service.dto.User.UserRestaurantCreateDto;
import com.tujuhsembilan.user_service.dto.User.UserUpdateDto;
import com.tujuhsembilan.user_service.service.UserService;
import com.tujuhsembilan.user_service.util.ResponseUtil;
import com.tujuhsembilan.user_service.util.ValidateRoleUtil;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ValidateRoleUtil validateRoleUtil;

    @GetMapping("/restaurant")
    public ResponseEntity<?> getRestaurant(@RequestHeader("Authorization") String header) {
        List<String> allowedRoles = Arrays.asList(UserTypeEnum.STAFF.toString(), UserTypeEnum.ADMIN.toString());
        // validate UserType
        if (!validateRoleUtil.validateRole(header, allowedRoles)) {
            return ResponseUtil.error(null, "01", "Unauthorized");
        }
        return userService.getListStaffUserRestaurant();
    }

    @GetMapping("/customer")
    public ResponseEntity<?> getCustomers(@RequestHeader("Authorization") String header) {
        List<String> allowedRoles = Arrays.asList(UserTypeEnum.STAFF.toString(), UserTypeEnum.ADMIN.toString());
        // validate UserType
        if (!validateRoleUtil.validateRole(header, allowedRoles)) {
            return ResponseUtil.error(null, "01", "Unauthorized");
        }

        return userService.getListCustomer();
    }

    @PutMapping("/edit")
    public ResponseEntity<?> editUserDetail(
        @RequestHeader("Authorization") String header,
            @RequestBody UserUpdateDto request
    ) {
        List<String> allowedRoles = Arrays.asList(UserTypeEnum.STAFF.toString(), UserTypeEnum.ADMIN.toString(), UserTypeEnum.CUSTOMER.toString());
        // validate UserType
        if (!validateRoleUtil.validateRole(header, allowedRoles)) {
            return ResponseUtil.error(null, "01", "Unauthorized");
        }

        return userService.updateDetailUser(request);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<?> deleteUserDetail(@RequestHeader("Authorization") String header, @PathVariable("username") String username) {
        List<String> allowedRoles = Arrays.asList(UserTypeEnum.ADMIN.toString());
        // validate UserType
        if (!validateRoleUtil.validateRole(header, allowedRoles)) {
            return ResponseUtil.error(null, "01", "Unauthorized");
        }

        return userService.deleteUser(username);
    }

    @PostMapping("/restaurant/add")
    public ResponseEntity<?> addRestaurant(@RequestHeader("Authorization") String header, @RequestBody UserRestaurantCreateDto request) {
        List<String> allowedRoles = Arrays.asList(UserTypeEnum.ADMIN.toString());
        // validate UserType
        if (!validateRoleUtil.validateRole(header, allowedRoles)) {
            return ResponseUtil.error(null, "01", "Unauthorized");
        }

        return userService.addStaffOrAdminRestaurant(request);
    }
}
