package com.example.Api_Auth.Service;

import com.example.Api_Auth.DTO.RolesDTO;
import org.springframework.http.ResponseEntity;

public interface RolesService {

    public ResponseEntity<?> AjouterRoles(RolesDTO rolesDTO);

}
