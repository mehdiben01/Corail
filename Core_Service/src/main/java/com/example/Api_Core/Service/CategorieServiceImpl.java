package com.example.Api_Core.Service;

import com.example.Api_Core.DTO.CategorieDTO;
import com.example.Api_Core.Entity.Categorie;
import com.example.Api_Core.Mapper.CategorieMapper;
import com.example.Api_Core.Repository.CategorieRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CategorieServiceImpl implements CategorieService {

    private CategorieRepository categorieRepository;

    private CategorieMapper categorieMapper;

    public CategorieServiceImpl(CategorieRepository categorieRepository, CategorieMapper categorieMapper) {
        this.categorieRepository = categorieRepository;
        this.categorieMapper = categorieMapper;
    }

    @Override
    public ResponseEntity<?> AjouterCategorie(CategorieDTO categorieDTO) {

        Categorie existingCat = categorieRepository.findCategorieByNom(categorieDTO.getNom());
        if(existingCat !=null){
            return ResponseEntity.ok("Categorie deja exist");
        }
        Categorie categorie1 = categorieMapper.fromCategorieDTO(categorieDTO);
        Categorie save = categorieRepository.save(categorie1);
        CategorieDTO categorieDTO1 = categorieMapper.fromCategorie(save);
        if(categorieDTO1!=null){
            return ResponseEntity.ok("Categorie ajoute avec succes");
        }else {
            return ResponseEntity.ok("Erreur");
        }



    }
}
