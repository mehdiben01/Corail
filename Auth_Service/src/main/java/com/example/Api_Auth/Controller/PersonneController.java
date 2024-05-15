package com.example.Api_Auth.Controller;

import com.example.Api_Auth.DTO.AuthenticationResponse;
import com.example.Api_Auth.DTO.LoginClientDTO;
import com.example.Api_Auth.DTO.PersonneDTO;
import com.example.Api_Auth.DTO.RolesDTO;
import com.example.Api_Auth.Entity.Personne;
import com.example.Api_Auth.Entity.Roles;
import com.example.Api_Auth.Repository.PersonneRepository;
import com.example.Api_Auth.Repository.RolesRepository;

import com.example.Api_Auth.Service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class PersonneController {


    private PersonneRepository personneRepository;

    private PersonneServiceImpl personneService;

    private RolesServiceImpl rolesService;

    public PersonneController(PersonneRepository personneRepository, PersonneServiceImpl personneService, RolesServiceImpl rolesService) {

        this.personneRepository = personneRepository;

        this.personneService = personneService;

        this.rolesService = rolesService;
    }



    @GetMapping("/roles")
    public List<Roles>roles(){
        return personneService.roles();
    }

    @PostMapping("Ajouter_Role")
    public ResponseEntity<?> save(@RequestBody RolesDTO rolesDTO) {
       return rolesService.AjouterRoles(rolesDTO);
    }


    @GetMapping("/personnes")
    public List<Personne> personnes(){
        return personneRepository.findAll();
    }


    @PostMapping("/Ajouter_Personne")
    public ResponseEntity<?> save(@RequestBody PersonneDTO personneDTO) {
        return personneService.AjouterPersonne(personneDTO);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginClientDTO loginClientDTO) {
        return  personneService.login(loginClientDTO);
    }

    @PostMapping("/login_client")
    public ResponseEntity<?> clientLogin(@RequestBody LoginClientDTO loginClientDTO) {
        return personneService.processClientLogin(loginClientDTO);
    }


    @PostMapping("/user-verify")
    public ResponseEntity<?> verifyUser(@RequestParam  String email , @RequestParam  String otp) {
        return personneService.verifyuser(email,otp);
    }



    @PostMapping("/SousRetail-verify")
    public ResponseEntity<?> verifySousReatil(@RequestParam String otp ){
        return personneService.verifySousRetail(otp);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        return personneService.logout(request,response);
    }

    @PostMapping("/mdp_oublie")
    public ResponseEntity<?> mdp_oublie(@RequestParam String email) {
        return personneService.MdpOublie(email);
    }

    @GetMapping("/user")
    public Map<String, String> infouser(){
        return personneService.InfoUser();
    }

    @PostMapping("/UpdateUser")
    public ResponseEntity<?> UpdateUser(@RequestParam String NomComplet){
        return personneService.UpdateUser(NomComplet);
    }

    @PostMapping("/UpdatePass")
    public ResponseEntity<?> UpdatePassword(@RequestParam String OldPass, @RequestParam String NewPass){
        return personneService.UpdatePass(OldPass,NewPass);
    }
    @PostMapping("/parrainer")
    public ResponseEntity<?> ParrainerAmi(@RequestParam String EmailAmi){
        return personneService.ParrainerAmi(EmailAmi);
    }

    @PostMapping("/addImage")
    public ResponseEntity<?> AddImage(@ModelAttribute PersonneDTO personneDTO,@RequestParam("file") MultipartFile file){
        return personneService.AddImagePersonne(personneDTO,file);
    }









}

