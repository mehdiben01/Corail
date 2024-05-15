package com.example.Api_Core.Repository;

import com.example.Api_Core.Entity.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie,Long> {
    Categorie findCategorieByNom(String nom);



}
