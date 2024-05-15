package com.example.Api_Core.Mapper;

import com.example.Api_Core.DTO.CategorieDTO;
import com.example.Api_Core.Entity.Categorie;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CategorieMapper {
    private ModelMapper modelMapper = new ModelMapper();

    public CategorieDTO fromCategorie(Categorie categorie){
        return modelMapper.map(categorie,CategorieDTO.class);
    };

    public Categorie fromCategorieDTO(CategorieDTO categorieDTO){
        return modelMapper.map(categorieDTO,Categorie.class);
    }

}
