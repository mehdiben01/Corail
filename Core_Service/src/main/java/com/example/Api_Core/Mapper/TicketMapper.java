package com.example.Api_Core.Mapper;

import com.example.Api_Core.DTO.TicketDTO;
import com.example.Api_Core.Entity.Ticket;
import com.example.Api_Core.Entity.TicketProduit;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {
    private ModelMapper modelMapper = new ModelMapper();
    public TicketDTO fromTicket(Ticket ticket){
        return modelMapper.map(ticket,TicketDTO.class);
    }
    public Ticket fromTicketDTO(TicketDTO ticketDTO){
        return modelMapper.map(ticketDTO,Ticket.class);
    }
}
