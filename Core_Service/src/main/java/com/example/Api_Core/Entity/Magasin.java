package com.example.Api_Core.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Magasin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ville;
    private String adresse;
    private float lantitude;
    private float longtitude;
    private String tel;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Marque marque;
    @JsonIgnore
    @OneToMany(mappedBy = "magasin" , fetch = FetchType.LAZY)
    private List<Ticket> tickets;
    @Override
    public String toString() {
        return "Magasin{" +
                "id=" + id +
                ", ville='" + ville + '\'' +
                ", adresse='" + adresse + '\'' +
                ", lantitude=" + lantitude +
                ", longtitude=" + longtitude +
                ",tel="+tel+
                '}';
    }
}
