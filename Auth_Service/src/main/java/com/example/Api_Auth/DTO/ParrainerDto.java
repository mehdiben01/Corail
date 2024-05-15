package com.example.Api_Auth.DTO;

import com.example.Api_Auth.Entity.Personne;
import jakarta.annotation.security.DenyAll;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ParrainerDto {
    private Long id;
    private String email;
    private Personne personne;
}
