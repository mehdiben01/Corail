package com.example.Api_Core.DTO;

import com.example.Api_Core.Entity.Magasin;
import com.example.Api_Core.Entity.Marque;
import com.example.Api_Core.Entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MagasinDTO {
    private Long id;
    private String ville;
    private String adresse;
    private float lantitude;
    private float longtitude;
    private String tel;
    private Marque marque;

}
