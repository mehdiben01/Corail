package com.example.Api_Auth.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.generator.internal.GeneratedGeneration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import javax.management.relation.Role;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Personne implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String civilite;
    private String nom;
    private String prenom;
    @Transient
    private MultipartFile logo;
    private String cheminImage;
    private String tel;
    private String ville;
    private String email;
    private String mdp;
    private String photo;
    private String otp;
    private boolean verified=false;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Roles> roles;
    @OneToMany(mappedBy = "personne", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Parrainer> parrainer;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles != null) {
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNom()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public String getPassword() {
        return mdp;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
