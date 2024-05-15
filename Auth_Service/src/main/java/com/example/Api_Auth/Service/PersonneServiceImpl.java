package com.example.Api_Auth.Service;

import com.example.Api_Auth.DTO.AuthenticationResponse;
import com.example.Api_Auth.DTO.LoginClientDTO;
import com.example.Api_Auth.DTO.ParrainerDto;
import com.example.Api_Auth.DTO.PersonneDTO;
import com.example.Api_Auth.Entity.Parrainer;
import com.example.Api_Auth.Entity.Personne;
import com.example.Api_Auth.Entity.Roles;
import com.example.Api_Auth.Mapper.PersonneMapper;
import com.example.Api_Auth.Repository.ParrainerRepository;
import com.example.Api_Auth.Repository.PersonneRepository;
import com.example.Api_Auth.Repository.RolesRepository;
import com.example.Api_Auth.Security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class PersonneServiceImpl implements PersonneService {


    private PersonneRepository personneRepository;

    private RolesRepository rolesRepository;

    private PersonneMapper personneMapper;

    private EmailService emailService;

    private PasswordEncoder passwordEncoder;

    private UserDetailServiceImp userDetailServiceImp;

    private RolesService rolesService;

    private JwtService jwtService;
    private ParrainerRepository parrainerRepository;

    private ImageServiceImpl imageService;

    public PersonneServiceImpl(PersonneRepository personneRepository, RolesRepository rolesRepository, PersonneMapper personneMapper, EmailService emailService, PasswordEncoder passwordEncoder, UserDetailServiceImp userDetailServiceImp, RolesService rolesService, JwtService jwtService, ParrainerRepository parrainerRepository, ImageServiceImpl imageService) {
        this.personneRepository = personneRepository;
        this.rolesRepository = rolesRepository;
        this.personneMapper = personneMapper;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailServiceImp = userDetailServiceImp;
        this.rolesService = rolesService;
        this.jwtService = jwtService;
        this.parrainerRepository = parrainerRepository;
        this.imageService = imageService;
    }


    @Override
    public List<Roles> roles() {
        return rolesRepository.findAll();
    }



    @Override
    public ResponseEntity<?> AjouterPersonne(PersonneDTO personneDTO) {
        Personne existingUserEmail = personneRepository.findPersonneByEmail(personneDTO.getEmail());
        Personne existingUserTel = personneRepository.findPersonneByTel(personneDTO.getTel());

        if (existingUserEmail != null || existingUserTel != null) {
            return new ResponseEntity<>("Utilisateur déjà existant", HttpStatus.BAD_REQUEST);
        } else {
            Random r = new Random();
            String otp = String.format("%04d", r.nextInt(10000));
            Personne personne = personneMapper.fromPersonneDTO(personneDTO);
            personne.setOtp(otp);
            personne.setMdp(passwordEncoder.encode(personneDTO.getMdp()));
            boolean hasClientRole = false;
            for (Roles role : personne.getRoles()) {
                if (role.getId()==2) {
                    hasClientRole = true;
                    break;
                } else if (role.getId()==4) {
                    personne.setOtp(personneDTO.getOtp());
                    personne.setVerified(false);
                } else{
                    personne.setOtp(null);
                    personne.setVerified(true);
                }
            }
            Personne savedPersonne = personneRepository.save(personne);
            PersonneDTO personneDTO1 = personneMapper.fromPersonne(savedPersonne);
            if (hasClientRole) {
                String subject = "Email Verification";
                String body = "Votre code de vérification OTP est : " + personneDTO1.getOtp();
                emailService.sendMail(personneDTO1.getEmail(), subject, body);


            } else {
                return new ResponseEntity<>("Opération réussie", HttpStatus.OK);

            }

            personneDTO.setId(savedPersonne.getId());
            personneDTO.setEmail(savedPersonne.getEmail());
            return new ResponseEntity<>("Opération réussie", HttpStatus.OK);
        }

    }




    @Override
    public ResponseEntity<?> verifyuser(String email, String otp){
        Personne personne = personneRepository.findPersonneByEmail(email);
        if(personne != null && personne.isVerified()){
            return new ResponseEntity<>("L'utilisateur est déjà vérifié", HttpStatus.BAD_REQUEST);
        } else if(personne != null && otp.equals(personne.getOtp())){
            personne.setVerified(true);
            personneRepository.save(personne);
            return new ResponseEntity<>("Utilisateur vérifié avec succès", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Utilisateur non vérifié", HttpStatus.BAD_REQUEST);

        }

    }


    @Override
    public ResponseEntity<?> verifySousRetail(String otp) {
        Personne personne = personneRepository.findPersonneByOtp(otp);
        if(personne != null && personne.isVerified()){
            return new ResponseEntity<>("L'utilisateur est déjà vérifié", HttpStatus.BAD_REQUEST);

        }else if(otp.equals(personne.getOtp())){
            personne.setVerified(true);
            personneRepository.save(personne);
            return new ResponseEntity<>("Utilisateur vérifié avec succès", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Utilisateur non vérifié", HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<?> MdpOublie(String email) {
    Personne personne = personneRepository.findPersonneByEmailOrTel(email,email);
    if(personne !=null && isClient(email)){
        String password = RandomStringUtils.randomAlphanumeric(10);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        personne.setMdp(hashedPassword);
        //Personne savedPersonne =
        personneRepository.save(personne);
        String subject = "Nouveau mot de passe";
        String body = "Votre nouveau mot de passe est : " + password +"\n Veuillez personnalisé le mot de passe ";
        emailService.sendMail(personne.getEmail(), subject, body);
        return new ResponseEntity<>("Consultez votre messagerie électronique pour voir le nouveau mot de passe", HttpStatus.OK);
    }else{
        return new ResponseEntity<>("Utilisateur n'exist pas", HttpStatus.BAD_REQUEST);
    }

    }

    @Override
    public ResponseEntity<?> processClientLogin(LoginClientDTO loginClientDTO) {
        AuthenticationResponse rep = userDetailServiceImp.authenticateclient(loginClientDTO);
        Personne personne = personneRepository.findPersonneByEmailOrTel(loginClientDTO.getIdentifier(), loginClientDTO.getIdentifier());

        if (personne == null) {
            return new ResponseEntity<>("Utilisateur introuvable", HttpStatus.BAD_REQUEST);

        }

        if (rep != null) {
            boolean isClient = isClient(loginClientDTO.getIdentifier());

            if (rep.isVerified() && isClient) {
                return ResponseEntity.ok(rep);
            } else {
                return new ResponseEntity<>("Compte utilisateur non vérifié. Contactez le support pour activer votre compte.", HttpStatus.BAD_REQUEST);

            }
        } else {
            return new ResponseEntity<>("Données incorrectes.", HttpStatus.BAD_REQUEST);

        }
    }

    @Override
    public ResponseEntity<?> login(@RequestBody LoginClientDTO loginClientDTO) {
        AuthenticationResponse rep = userDetailServiceImp.authenticate(loginClientDTO);
        Personne personne = personneRepository.findPersonneByEmail(loginClientDTO.getIdentifier());
        System.out.println("resp = " + rep);

        if(personne == null){
            return new ResponseEntity<>("Utilisateur introuvable", HttpStatus.BAD_REQUEST);

        }

        if (rep != null) {
            boolean hasClientRole = personne.getRoles().stream()
                    .anyMatch(role -> "CLIENT".equals(role.getNom()));
            if (rep.isVerified() && !hasClientRole) {
                return ResponseEntity.ok(rep);
            }  else {
                return new ResponseEntity<>("Compte utilisateur non vérifié. Contactez le support pour activer votre compte.", HttpStatus.BAD_REQUEST);

            }
        } else {
            return new ResponseEntity<>("Données incorrectes.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);

            String token = extractTokenFromRequest(request);
            if (token != null) {
                jwtService.revokeToken(token);
            }
        }
        return new ResponseEntity<>("Déconnexion réussie", HttpStatus.OK);
    }

    @Override
    public Map<String, String> InfoUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Personne user = (Personne) authentication.getPrincipal();
        Map<String,String> userInfo = new HashMap<>();
        userInfo.put("nom", user.getNom());
        userInfo.put("prenom", user.getPrenom());
        userInfo.put("email", user.getEmail());
        return userInfo;
    }

    public ResponseEntity<?> UpdateUser(String NomComplet) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Personne personne = (Personne) authentication.getPrincipal();
        Personne personneByEmail = personneRepository.findPersonneByEmail(personne.getEmail());
        if (personneByEmail != null) {
            String[] parts = NomComplet.split(" ");
            if (parts.length >= 2) {
                personneByEmail.setPrenom(parts[0]);
                personneByEmail.setNom(parts[1]);
                personneRepository.save(personneByEmail);
                return new ResponseEntity<>("Utilisateur modifié avec succès", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Nom complet invalide", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Utilisateur introuvable", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> UpdatePass(String OldPass, String NewPass) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Personne personne = (Personne) authentication.getPrincipal();
        Personne personneByEmail = personneRepository.findPersonneByEmail(personne.getEmail());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if(personneByEmail !=null){
            if (passwordEncoder.matches(OldPass, personneByEmail.getMdp())) {
                String hashedPassword = passwordEncoder.encode(NewPass);
                personne.setMdp(hashedPassword);
                personneRepository.save(personne);
                return new ResponseEntity<>("Mot de passe modifié avec succès", HttpStatus.NOT_FOUND);
            }else{
                return new ResponseEntity<>("Ancien mot de passe incorrecte", HttpStatus.NOT_FOUND);
            }
        }else{
            return new ResponseEntity<>("Utilisateur introuvable", HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public ResponseEntity<?> ParrainerAmi(String emailAmi) {
        Parrainer parrainerByEmail = parrainerRepository.findParrainerByEmail(emailAmi);
        if (parrainerByEmail != null) {
            return new ResponseEntity<>("Utilisateur déjà existant", HttpStatus.BAD_REQUEST);
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null) {
                Personne authenticatedPersonne = (Personne) authentication.getPrincipal();
                Parrainer parrainer = new Parrainer();
                parrainer.setEmail(emailAmi);
                parrainer.setPersonne(authenticatedPersonne);
                parrainerRepository.save(parrainer);
                emailService.sendParrainerMail(emailAmi,authenticatedPersonne.getEmail());
                return new ResponseEntity<>("Parrainer invite", HttpStatus.OK);
            } else {
                // Handle the case when the authenticated principal is not a Personne
                return new ResponseEntity<>("Authentication principal is not a Personne", HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @Override
    public ResponseEntity<?> AddImagePersonne(PersonneDTO personneDTO, MultipartFile imageFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Personne personne = (Personne) authentication.getPrincipal();
        Personne personneByEmail = personneRepository.findPersonneByEmail(personne.getEmail());
        String ObjectName = personne.getCheminImage();

        if (ObjectName != null) {
            boolean updateimage = imageService.updateImage(personneByEmail, imageFile, ObjectName);
            if (updateimage) {
                if (!imageFile.isEmpty()) {
                    personneRepository.save(personneByEmail);
                    return ResponseEntity.ok("Image mise à jour avec succès");
                } else {
                    return ResponseEntity.ok("Aucune image à mettre à jour");
                }
            } else {
                return ResponseEntity.ok(ImageServiceImpl.IMAGE_SAVE_ERROR_MESSAGE);
            }
        } else {
            String cheminimage = imageService.addImage(personneByEmail, imageFile);
            if (cheminimage != null) {
                personneRepository.save(personneByEmail);
                return ResponseEntity.ok("Image ajoutée avec succès");
            } else {
                return ResponseEntity.ok(ImageServiceImpl.IMAGE_SAVE_ERROR_MESSAGE);
            }
        }
    }



    private String extractTokenFromRequest(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }


    private boolean isClient(String identifier) {
        Personne personne = personneRepository.findPersonneByEmailOrTel(identifier, identifier);
        return personne != null && personne.getRoles().stream().anyMatch(role -> "CLIENT".equals(role.getNom()));
    }

    @Override
    public Personne loadUserByUsername(String email) {
        return personneRepository.findPersonneByEmail(email);
    }
}
