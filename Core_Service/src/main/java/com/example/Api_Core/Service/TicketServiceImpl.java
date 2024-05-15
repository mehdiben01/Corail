package com.example.Api_Core.Service;

import com.example.Api_Core.DTO.TicketDTO;
import com.example.Api_Core.DTO.TicketProduitDTO;
import com.example.Api_Core.Entity.*;
import com.example.Api_Core.Mapper.TicketMapper;
import com.example.Api_Core.Mapper.TicketProduitMapper;
import com.example.Api_Core.Repository.MagasinRepository;
import com.example.Api_Core.Repository.ProduitRepository;
import com.example.Api_Core.Repository.TicketProduitRepository;
import com.example.Api_Core.Repository.TicketRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

@Service
public class TicketServiceImpl implements TicketService {

    private TicketRepository ticketRepository;
    private ProduitRepository produitRepository;
    private TicketProduitRepository ticketProduitRepository;
    private TicketProduitMapper ticketProduitMapper;
    private TicketMapper ticketMapper;
    private MagasinRepository magasinRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, ProduitRepository produitRepository, TicketProduitRepository ticketProduitRepository, TicketProduitMapper ticketProduitMapper, TicketMapper ticketMapper, MagasinRepository magasinRepository) {
        this.ticketRepository = ticketRepository;
        this.produitRepository = produitRepository;
        this.ticketProduitRepository = ticketProduitRepository;
        this.ticketProduitMapper = ticketProduitMapper;
        this.ticketMapper = ticketMapper;

        this.magasinRepository = magasinRepository;
    }

    @Override
    public ResponseEntity<?> AjouterTicket(String qrCodedata) {
        TicketDTO ticketDTO = new TicketDTO();
        List<TicketProduitDTO> ticketProduitDTO = new ArrayList<>();
      try{

          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          String email = (String) authentication.getPrincipal();


          JSONObject object = new JSONObject(qrCodedata);
          JSONArray produits = object.getJSONArray("produits");
          String date_ticketstr = object.getString("date_ticket");
          String ticket_number = object.getString("ticket_number");
          int magasin = object.getInt("Nom_marque");
          boolean existingTicket = ticketRepository.existsTicketByReference(ticket_number);
          if(existingTicket){
              return ResponseEntity.ok("Ticket exist deja");
          }else{
              SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
              Date date_ticket = dateFormat.parse(date_ticketstr);

              Magasin magasin1 = magasinRepository.findMagasinById(magasin);

              ticketDTO.setDate(date_ticket);
              ticketDTO.setReference(ticket_number);
              ticketDTO.setClient(email);
              ticketDTO.setMagasin(magasin1);
              Ticket ticket= ticketMapper.fromTicketDTO(ticketDTO);
              ticketRepository.save(ticket);

              for (int i = 0; i < produits.length(); i++) {
                  JSONObject produitJson = produits.getJSONObject(i);
                  String reference_produit = produitJson.getString("reference");
                  int quantite_produit = produitJson.getInt("quantity");
                  double tva = produitJson.getDouble("TVA");


                  Produit produit = produitRepository.findProduitByReference(reference_produit);


                  TicketProduitDTO ticketProduitDTO1 = new TicketProduitDTO();
                  ticketProduitDTO1.setQuantite(quantite_produit);
                  ticketProduitDTO1.setTicket(ticket);
                  ticketProduitDTO1.setProduit(produit);
                  ticketProduitDTO1.setTva(tva);
                  ticketProduitRepository.save(ticketProduitMapper.fromTicketProduitDTO(ticketProduitDTO1));

                  ticketProduitDTO.add(ticketProduitDTO1);
              }

             //  List<Object[]> ticketDetails = ticketRepository.findTicketDetailsByReference(ticket_number);

              Map<String, Object> response = new HashMap<>();
              response.put("ticket", ticketDTO);
              response.put("ticketProduits", ticketProduitDTO);
              return ResponseEntity.ok(response);
          }

      }catch (JSONException e) {

          e.printStackTrace();
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON data");
      } catch (Exception e) {
          e.printStackTrace();
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout");
      }
    }

    @Override
    public ResponseEntity<List<Object[]>> getAllTicketsByClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        List<Object[]> objects = ticketRepository.AllTicketsByClient(email);
        return ResponseEntity.ok(objects);
    }

    @Override
    public ResponseEntity<List<Object[]>> getAllTicketsByClientAndFilter(Categorie categorie,String dateT) throws ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDate = dateFormat.parse(dateT);
        Timestamp timestamp = new Timestamp(parsedDate.getTime());
        List<Object[]> objects = ticketRepository.AllTicketsByClientAndFilter(email,categorie,timestamp);
        return ResponseEntity.ok(objects);
    }

    @Override
    public ResponseEntity<List<Object[]>> getAllTicketsClientByBrand(Marque marque) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        List<Object[]> objects = ticketRepository.AllTicketsByClientAndBrand(email,marque);
        return ResponseEntity.ok(objects);
    }

    @Override
    public ResponseEntity<List<Object[]>> getDetailsTicket(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        List<Object[]> ticketDetailsById = ticketRepository.findTicketDetailsById(id, email);
        return ResponseEntity.ok(ticketDetailsById);
    }

    @Override
    public ResponseEntity<List<Object[]>> AllNotifClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        List<Object[]> objects = ticketRepository.AllNotificationByCLient(email);
        return ResponseEntity.ok(objects);
    }

    @Override
    public ResponseEntity<List<Object[]>> NotNotifClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        List<Object[]> Objects = ticketRepository.NotCheckedNotificationByCLient(email);
        ticketRepository.updateCheckedNotificationsByClient(email);
        return ResponseEntity.ok(Objects);
    }

    @Override
    public ResponseEntity<List<Object[]>> ChckedNotifClient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        List<Object[]> objects = ticketRepository.CheckedNotificationByCLient(email);
        return ResponseEntity.ok(objects);
    }



}


