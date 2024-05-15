package com.example.Api_Core.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketProduit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Ticket ticket;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Produit produit;

    private int quantite;
    private double tva;
}
