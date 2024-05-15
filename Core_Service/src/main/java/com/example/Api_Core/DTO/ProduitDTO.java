package com.example.Api_Core.DTO;

import com.example.Api_Core.Entity.Categorie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.internal.build.AllowNonPortable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProduitDTO {
    private Long id;
    private String reference;
    private String nom;
    private double prix;
    private Categorie categorie;
}
