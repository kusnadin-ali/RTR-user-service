package com.tujuhsembilan.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tujuhsembilan.user_service.dto.User.UserPojo;
import com.tujuhsembilan.user_service.model.User;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndIsDeleteFalse(String username);

    @Query(value = "select u.name from public.user u where u.is_delete = false", nativeQuery = true)
    List<UserPojo> getAll();

    @Query(value = "select u.name, u.email, u.username, u.user_type as usertype from public.user u where u.user_type = :userType and u.is_delete = false", nativeQuery = true)
    List<UserPojo> getAllByUserType(String userType);
}
