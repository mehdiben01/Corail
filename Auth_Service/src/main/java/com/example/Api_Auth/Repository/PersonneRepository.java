package com.example.Api_Auth.Repository;

import com.example.Api_Auth.Entity.Personne;
import com.example.Api_Auth.Entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonneRepository extends JpaRepository<Personne,Long> {
    Personne findPersonneByEmailOrTel(String email, String tel);

    Personne findPersonneByTel(String tel);


    Personne findPersonneByEmail(String email);

    Personne findPersonneByOtp(String otp);







}
