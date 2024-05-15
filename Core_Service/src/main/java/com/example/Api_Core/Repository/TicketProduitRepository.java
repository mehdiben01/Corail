package com.example.Api_Core.Repository;

import com.example.Api_Core.Entity.Ticket;
import com.example.Api_Core.Entity.TicketProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketProduitRepository extends JpaRepository<TicketProduit,Long> {




}
