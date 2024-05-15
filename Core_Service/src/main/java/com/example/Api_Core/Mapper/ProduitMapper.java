package com.example.Api_Core.Mapper;

import com.example.Api_Core.DTO.ProduitDTO;
import com.example.Api_Core.Entity.Produit;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProduitMapper {
    private ModelMapper modelMapper = new ModelMapper();
    public ProduitDTO fromProduit(Produit produit){
        return modelMapper.map(produit,ProduitDTO.class);
    }
    public Produit fromProduitDTO(ProduitDTO produitDTO){
        return modelMapper.map(produitDTO,Produit.class);
    }
}
