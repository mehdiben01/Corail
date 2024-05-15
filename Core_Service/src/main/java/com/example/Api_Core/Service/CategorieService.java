package com.example.Api_Core.Service;

import com.example.Api_Core.DTO.CategorieDTO;
import com.example.Api_Core.Entity.Categorie;
import org.springframework.http.ResponseEntity;

public interface CategorieService {

    ResponseEntity<?> AjouterCategorie(CategorieDTO categorieDTO);
}
