package com.example.Api_Core.Mapper;

import com.example.Api_Core.DTO.TicketProduitDTO;
import com.example.Api_Core.Entity.Ticket;
import com.example.Api_Core.Entity.TicketProduit;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TicketProduitMapper {
    private ModelMapper modelMapper= new ModelMapper();
    public TicketProduitDTO fromTicketProduit(TicketProduit ticketProduit){
        return modelMapper.map(ticketProduit,TicketProduitDTO.class);
    }
    public TicketProduit fromTicketProduitDTO(TicketProduitDTO ticketProduitDTO){
        return modelMapper.map(ticketProduitDTO,TicketProduit.class);
    }
}
