package com.example.Api_Core.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties({"magasins"})
public class Marque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String ice;
    @Transient
    private MultipartFile logo;
    private String cheminImage;
    private String siteweb;
    @OneToMany(mappedBy = "marque",fetch = FetchType.LAZY)
    private List<Magasin> magasins;
    @JsonIgnore
    @OneToMany(mappedBy = "marque",fetch = FetchType.LAZY)
    private List<Produit> produits;
    @JsonIgnore
    @Override
    public String toString() {
        return "Marque{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", ice='" + ice + '\'' +
                ", logo='" + logo + '\'' +
                ", siteweb='" + siteweb + '\'' +
                '}';
    }
}
