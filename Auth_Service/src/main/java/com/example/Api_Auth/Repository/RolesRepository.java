package com.example.Api_Auth.Repository;

import com.example.Api_Auth.Entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Long> {
    Roles findRolesByNom(String nom);

}
