package com.example.Api_Core.Repository;

import com.example.Api_Core.DTO.MarqueDTO;
import com.example.Api_Core.Entity.Magasin;
import com.example.Api_Core.Entity.Marque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MagasinRepository extends JpaRepository<Magasin,Long> {

    Magasin findMagasinById(int id);
}
