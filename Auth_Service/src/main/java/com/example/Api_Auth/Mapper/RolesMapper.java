package com.example.Api_Auth.Mapper;

import com.example.Api_Auth.DTO.RolesDTO;
import com.example.Api_Auth.Entity.Roles;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RolesMapper {

    private ModelMapper modelMapper = new ModelMapper();

    public RolesDTO fromRoles(Roles roles){
        return modelMapper.map(roles,RolesDTO.class);
    }

    public Roles fromRolesDTO(RolesDTO rolesDTO){
        return modelMapper.map(rolesDTO, Roles.class);
    }

}
