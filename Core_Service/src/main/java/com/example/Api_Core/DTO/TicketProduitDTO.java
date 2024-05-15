package com.example.Api_Core.DTO;

import com.example.Api_Core.Entity.Produit;
import com.example.Api_Core.Entity.Ticket;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketProduitDTO {
    private Long id;

    private int quantite;
    private double tva;
    @JsonIgnore
    private Ticket ticket;
    private Produit produit;
}
