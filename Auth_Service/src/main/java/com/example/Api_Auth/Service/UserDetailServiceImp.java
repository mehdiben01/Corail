package com.example.Api_Auth.Service;

import com.example.Api_Auth.DTO.AuthenticationResponse;
import com.example.Api_Auth.DTO.LoginClientDTO;
import com.example.Api_Auth.DTO.PersonneDTO;
import com.example.Api_Auth.Entity.Personne;
import com.example.Api_Auth.Entity.Roles;
import com.example.Api_Auth.Repository.PersonneRepository;
import com.example.Api_Auth.Repository.RolesRepository;
import com.example.Api_Auth.Security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserDetailServiceImp implements UserDetailsService {
    private PersonneRepository personneRepository ;
    @Autowired
    private RolesRepository roleRepository;


    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        System.out.println("Execution de methode loadUserByUserName ");

        Personne personne = personneRepository.findPersonneByEmailOrTel(identifier,identifier);


        if (personne == null) throw new UsernameNotFoundException(String.format("Utilisateur %s non retrouvé", identifier));

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        personne.getRoles().forEach(r->{
            authorities.add(new SimpleGrantedAuthority(r.getNom()));
        });
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(personne.getUsername())
                .password(personne.getMdp())
                .roles(authorities.toString())
                .build();

        return  userDetails;
    }

    public AuthenticationResponse authenticate(LoginClientDTO req) {
        try {
            System.out.println("Starting authentication for user: " + req.getIdentifier());

            Personne personne;

            if (isClient(req.getIdentifier())) {
                return null;
            } else {
                personne = personneRepository.findPersonneByEmail(req.getIdentifier());
            }

            if (personne == null) {
                System.out.println("User not found in the database.");
                return null;
            }

            if (!personne.isVerified()) {
                return AuthenticationResponse.builder()
                        .verified(false)
                        .expiration(jwtService.getJwtExpirationTime())
                        .build();
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    personne.getUsername(),
                    req.getMdp()
            ));

            System.out.println("Authentication successful for user: " + req.getIdentifier());

            var jwtToken = jwtService.generateToken(personne);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .verified(true)
                    .expiration(jwtService.getJwtExpirationTime())
                    .build();

        } catch (AuthenticationException e) {
            System.out.println("Authentication failed for user: " + req.getIdentifier());
            e.printStackTrace();
            return null;
        }
    }

    public AuthenticationResponse authenticateclient(LoginClientDTO req) {
        try {
            System.out.println("Starting authentication for user: " + req.getIdentifier());

            Personne personne;

            if (isClient(req.getIdentifier())) {
                personne = personneRepository.findPersonneByEmailOrTel(req.getIdentifier(), req.getIdentifier());

            } else {
                return null;
            }


            if (personne == null) {
                System.out.println("Utilisateur introuvable.");
                return null;
            }

            if (!personne.isVerified()) {
                return AuthenticationResponse.builder()
                        .verified(false)
                        .expiration(jwtService.getJwtExpirationTime())
                        .build();
            }

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    personne.getUsername(),
                    req.getMdp()
            ));

            System.out.println("Authentication successful for user: " + req.getIdentifier());



            var jwtToken = jwtService.generateToken(personne);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .verified(true)
                    .expiration(jwtService.getJwtExpirationTime())
                    .build();

        } catch (AuthenticationException e) {
            System.out.println("Authentication failed for user: " + req.getIdentifier());
            e.printStackTrace();
            return null;
        }
    }


    private boolean isClient(String identifier) {
        Personne personne = personneRepository.findPersonneByEmailOrTel(identifier, identifier);
        return personne != null && personne.getRoles().stream().anyMatch(role -> "CLIENT".equals(role.getNom()));
    }




}
