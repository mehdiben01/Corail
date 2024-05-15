package com.example.Api_Auth.Service;

import com.example.Api_Auth.DTO.RolesDTO;
import com.example.Api_Auth.Entity.Roles;
import com.example.Api_Auth.Mapper.RolesMapper;
import com.example.Api_Auth.Repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RolesServiceImpl implements RolesService {

    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private RolesRepository rolesRepository;
    @Override
    public ResponseEntity<?> AjouterRoles(RolesDTO rolesDTO) {
        Roles existingRolesNom = rolesRepository.findRolesByNom(rolesDTO.getNom());
        if ( existingRolesNom != null) {
            return new ResponseEntity<>("Le rôle existe déjà", HttpStatus.BAD_REQUEST);
        }
        Roles roles =rolesMapper.fromRolesDTO(rolesDTO);
        Roles save =rolesRepository.save(roles);
        RolesDTO rolesDTO1 =rolesMapper.fromRoles(save);
        if (rolesDTO1 != null) {
            return new ResponseEntity<>("Role ajouté avec succès", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Erreur lors de l'ajout du rôle", HttpStatus.BAD_REQUEST);
        }


    }
}
