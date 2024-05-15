package com.example.Api_Auth.Mapper;

import com.example.Api_Auth.DTO.PersonneDTO;
import com.example.Api_Auth.Entity.Personne;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PersonneMapper {
    private ModelMapper modelMapper = new ModelMapper();

    public PersonneDTO fromPersonne(Personne personne){
        return modelMapper.map(personne,PersonneDTO.class);
    }

    public Personne fromPersonneDTO(PersonneDTO personneDTO){
        return modelMapper.map(personneDTO,Personne.class);
    }

}
