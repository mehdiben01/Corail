package com.example.Api_Core.DTO;

import com.example.Api_Core.Entity.Magasin;
import com.example.Api_Core.Entity.Ticket;
import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  MarqueDTO {
    private Long id;
    private String nom;
    private String ice;
    private MultipartFile logo;
    private String cheminImage;
    private String siteweb;
    private List<Magasin> magasins;
    private List<Ticket> tickets;

}
