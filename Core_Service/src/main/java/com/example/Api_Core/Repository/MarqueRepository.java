package com.example.Api_Core.Repository;

import com.example.Api_Core.Entity.Marque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarqueRepository extends JpaRepository<Marque,Long> {
    Marque findMarqueById(Long id);

    boolean existsMarqueByIce(String ice);
}
