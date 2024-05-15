package com.example.Api_Core.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reference;
    private String nom;
    private double prix;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Marque marque;
    @JsonIgnore
    @ManyToOne
    private Categorie categorie;


}
