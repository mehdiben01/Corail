package com.example.Api_Core.Mapper;

import com.example.Api_Core.DTO.MagasinDTO;
import com.example.Api_Core.Entity.Magasin;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MagasinMapper {
    private ModelMapper modelMapper = new ModelMapper();
    public MagasinDTO fromMagasin(Magasin magasin){
        return modelMapper.map(magasin,MagasinDTO.class);
    }
    public Magasin fromMagasininDTO(MagasinDTO magasinDTO){
        return modelMapper.map(magasinDTO,Magasin.class);
    }
}
