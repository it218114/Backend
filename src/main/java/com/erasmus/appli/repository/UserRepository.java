package com.erasmus.appli.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erasmus.appli.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
  Optional<Users> findByEmail(String email);
  
  Users findByIdAndEnabled(long id, String isEnabled);

  Boolean existsByEmail(String email);
  
  List<Users> findAllByRole(String role);

  List<Users> findAllByRoleNot(String string);

List<Users> findAllByRoleNotAndEnabled(String role, String enabled);
}
