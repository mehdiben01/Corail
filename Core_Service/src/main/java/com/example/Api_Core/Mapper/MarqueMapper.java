package com.example.Api_Core.Mapper;

import com.example.Api_Core.DTO.MarqueDTO;
import com.example.Api_Core.Entity.Marque;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MarqueMapper {
    private ModelMapper modelMapper = new ModelMapper();
    public MarqueDTO fromMarque(Marque marque){
        return modelMapper.map(marque,MarqueDTO.class);
    }
    public  Marque fromMarqueDTO(MarqueDTO marqueDTO){
        return modelMapper.map(marqueDTO,Marque.class);
    }
}
