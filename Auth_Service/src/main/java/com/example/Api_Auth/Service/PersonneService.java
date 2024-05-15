package com.example.Api_Auth.Service;

import com.example.Api_Auth.DTO.LoginClientDTO;
import com.example.Api_Auth.DTO.PersonneDTO;
import com.example.Api_Auth.Entity.Personne;
import com.example.Api_Auth.Entity.Roles;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


public interface PersonneService {

    public List<Roles> roles();


    public ResponseEntity<?> AjouterPersonne(PersonneDTO personneDTO);



    public Personne loadUserByUsername(String email);

    public ResponseEntity<?> verifyuser(String email, String otp);

    public ResponseEntity<?> verifySousRetail(String otp);

    public ResponseEntity<?> MdpOublie(String email);

    public ResponseEntity<?> processClientLogin(LoginClientDTO loginClientDTO);

    public ResponseEntity<?> login(LoginClientDTO loginClientDTO);
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response);

    public Map<String, String> InfoUser();

    public ResponseEntity<?> UpdateUser(String NomComplet);
    public ResponseEntity<?> UpdatePass(String OldPassword, String NewPassword);

    public ResponseEntity<?> ParrainerAmi(String emailAmi );

    public ResponseEntity<?> AddImagePersonne(PersonneDTO personneDTO,MultipartFile imageFile);


}
