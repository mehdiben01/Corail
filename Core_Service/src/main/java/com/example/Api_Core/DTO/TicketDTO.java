package com.example.Api_Core.DTO;

import com.example.Api_Core.Entity.Magasin;
import com.example.Api_Core.Entity.Marque;
import com.example.Api_Core.Entity.Ticket;
import com.example.Api_Core.Entity.TicketProduit;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDTO {

    private String reference;
    private Date date;
    @Column(name = "client")
    private String client;
    private Magasin magasin;
    private boolean checked;


}
