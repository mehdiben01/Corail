package com.example.Api_Auth.Repository;

import com.example.Api_Auth.Entity.Parrainer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParrainerRepository extends JpaRepository<Parrainer,Long> {
    Parrainer findParrainerByEmail(String email);
}
