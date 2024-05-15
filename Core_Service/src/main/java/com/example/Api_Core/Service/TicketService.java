package com.example.Api_Core.Service;

import com.example.Api_Core.Entity.Categorie;
import com.example.Api_Core.Entity.Marque;
import com.example.Api_Core.Entity.Ticket;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;

public interface TicketService {
    ResponseEntity<?> AjouterTicket(String qrCodedata);

    ResponseEntity<List<Object[]>> getAllTicketsByClient();

    ResponseEntity<List<Object[]>> getAllTicketsByClientAndFilter(Categorie categorie, String dateT) throws ParseException;

    ResponseEntity<List<Object[]>> getAllTicketsClientByBrand(Marque marque);

    ResponseEntity<List<Object[]>> getDetailsTicket(Long id);

    ResponseEntity<List<Object[]>> AllNotifClient();

    ResponseEntity<List<Object[]>> NotNotifClient();

    ResponseEntity<List<Object[]>> ChckedNotifClient();







}
