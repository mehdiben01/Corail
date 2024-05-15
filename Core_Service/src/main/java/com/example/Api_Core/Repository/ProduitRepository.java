package com.example.Api_Core.Repository;

import com.example.Api_Core.Entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProduitRepository extends JpaRepository<Produit,Long> {

    Produit findProduitByReference(String reference);
}
