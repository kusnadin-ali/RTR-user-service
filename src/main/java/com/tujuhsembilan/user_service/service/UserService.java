package com.tujuhsembilan.user_service.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tujuhsembilan.user_service.constant.UserTypeEnum;
import com.tujuhsembilan.user_service.dto.User.UserCustomerDto;
import com.tujuhsembilan.user_service.dto.User.UserPojo;
import com.tujuhsembilan.user_service.dto.User.UserRestaurantCreateDto;
import com.tujuhsembilan.user_service.dto.User.UserUpdateDto;
import com.tujuhsembilan.user_service.model.User;
import com.tujuhsembilan.user_service.repository.UserRepository;
import com.tujuhsembilan.user_service.util.ResponseUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public boolean isUserExistByUsername(String username) {
        return userRepository.findByUsernameAndIsDeleteFalse(username).isPresent();
    }

    public ResponseEntity<?> saveUser(UserCustomerDto userCustomerDto){
        try {
            User user = new User();
            userCustomerDto.setPassword(passwordEncoder.encode(userCustomerDto.getPassword()));
            BeanUtils.copyProperties(userCustomerDto, user);
            user.setUserType(UserTypeEnum.CUSTOMER.getName());
            System.out.println(user);
            userRepository.save(user);
            userCustomerDto.setPassword("****");
            return ResponseEntity.ok(userCustomerDto);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeleteFalse(username);
    }

    public ResponseEntity<?> getListStaffUserRestaurant() {
        List<UserPojo> users = userRepository.getAllByUserType(UserTypeEnum.STAFF.name());

        return ResponseUtil.success(users);
    }

    public ResponseEntity<?> getListCustomer() {
        List<UserPojo> users = userRepository.getAllByUserType(UserTypeEnum.CUSTOMER.name());

        return ResponseUtil.success(users);
    }

    public ResponseEntity<?> updateDetailUser(UserUpdateDto request) {
        Optional<User> user = userRepository.findByUsernameAndIsDeleteFalse(request.getUsername());

        if(!user.isPresent()){
            return ResponseUtil.error(null, "02", "User not found");
        }

        User updateUser = user.get();
        updateUser.setName(request.getName());
        updateUser.setEmail(request.getEmail());

        userRepository.save(updateUser);

        return ResponseUtil.success();
    }

    public ResponseEntity<?> deleteUser(String username) {
        Optional<User> user = userRepository.findByUsernameAndIsDeleteFalse(username);

        if(!user.isPresent()){
            return ResponseUtil.error(null, "02", "User not found");
        }

        if(user.get().getUserType().equals(UserTypeEnum.CUSTOMER.name())) {
            return ResponseUtil.error(null, "02", "User Can't be deleted");
        }

        user.get().setIsDelete(true);

        userRepository.save(user.get());

        return ResponseUtil.success();
    }

    public ResponseEntity<?> addStaffOrAdminRestaurant(UserRestaurantCreateDto request) {
        try {
            log.info("masuk sini");
            Optional<User> existing = userRepository.findByUsernameAndIsDeleteFalse(request.getUsername());
            if(existing.isPresent()){
                return ResponseUtil.error(null, "02", "User already exist");
            }

            User newUser = new User();

            newUser.setName(request.getName());
            newUser.setEmail(request.getEmail());
            newUser.setUsername(request.getUsername());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setUserType(request.getUserType().toString());
            log.info(newUser.toString());
            userRepository.save(newUser);
            request.setPassword("*****");

            return ResponseUtil.success(request);
        } catch (Exception e) {
            log.info("error disini");
            throw e;
            // return ResponseUtil.error(null, "02", "Internal server error");
        }
    }
}
