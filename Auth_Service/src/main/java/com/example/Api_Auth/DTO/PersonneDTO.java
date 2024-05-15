package com.example.Api_Auth.DTO;

import com.example.Api_Auth.Entity.Roles;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PersonneDTO {
    private int id;
    private String nom;
    private String civilite;
    private String prenom;
    private MultipartFile logo;
    private String cheminImage;
    private String tel;
    private String ville;
    private String email;
    private String mdp;
    private String photo;
    private String otp;
    private boolean verified;
    private List<Roles> roles;
}
