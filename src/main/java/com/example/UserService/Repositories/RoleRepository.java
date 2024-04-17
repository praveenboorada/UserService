package com.example.UserService.Repositories;

import com.example.UserService.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository {
    List<Role> findAllRoleByIdIn(List<Long> roleIds);
}
