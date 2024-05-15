package com.example.Api_Core.Repository;

import com.example.Api_Core.DTO.TicketDTO;
import com.example.Api_Core.Entity.Categorie;
import com.example.Api_Core.Entity.Marque;
import com.example.Api_Core.Entity.Ticket;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
    boolean existsTicketByReference(String reference);


    @Query("SELECT marque.cheminImage, magasin.adresse, ticket.reference, Date(ticket.date), ticket.client, produit.nom, produit.prix, ticket_produit.quantite, ticket_produit.tva " +
            "FROM Ticket ticket " +
            "JOIN ticket.produits ticket_produit " +
            "JOIN ticket_produit.produit produit " +
            "JOIN produit.marque marque " +
            "JOIN marque.magasins magasin " +
            "WHERE ticket.id = :id " +
            "AND ticket.client= :identifier ")
    List<Object[]> findTicketDetailsById(@Param("id") Long id,@Param("identifier") String identifier);

    @Query("SELECT ticket.id, marque.id, marque.cheminImage, marque.nom, Date(ticket.date), SUM((ticketproduit.produit.prix*ticketproduit.quantite)+((ticketproduit.produit.prix*ticketproduit.quantite*ticketproduit.tva)/100)) as  TTC "+
    "FROM Ticket ticket,Produit produit,Marque marque, Magasin magasin, TicketProduit ticketproduit "+
     "WHERE ticketproduit.ticket = ticket " +
    "AND ticketproduit.produit=produit "+
    "AND ticket.magasin=magasin "+
    "AND magasin.marque=marque "+
    "AND ticket.client= :identifier "+
    "GROUP BY ticketproduit.ticket")
    List<Object[]> AllTicketsByClient(@Param("identifier") String identifier);

    @Query("SELECT ticket.id, marque.id, marque.cheminImage, marque.nom, Date(ticket.date), SUM((ticketproduit.produit.prix*ticketproduit.quantite)+((ticketproduit.produit.prix*ticketproduit.quantite*ticketproduit.tva)/100)) as TTC "+
            "FROM Ticket ticket,Produit produit,Marque marque, Magasin magasin, TicketProduit ticketproduit "+
            "WHERE ticketproduit.ticket = ticket " +
            "AND ticketproduit.produit=produit "+
            "AND ticket.magasin=magasin "+
            "AND magasin.marque=marque "+
            "AND ticket.client= :identifier "+
            "AND produit.categorie= :categorie "+
            "AND ticket.date = :dateT " +
            "GROUP BY ticketproduit.ticket")
    List<Object[]> AllTicketsByClientAndFilter(@Param("identifier") String identifier,@Param("categorie") Categorie categorie,  @Param("dateT") Timestamp dateT);

    @Query("SELECT ticket.id, marque.id, marque.cheminImage, marque.nom, Date(ticket.date), SUM((ticketproduit.produit.prix*ticketproduit.quantite)+((ticketproduit.produit.prix*ticketproduit.quantite*ticketproduit.tva)/100)) as  TTC "+
            "FROM Ticket ticket,Produit produit,Marque marque, Magasin magasin, TicketProduit ticketproduit "+
            "WHERE ticketproduit.ticket = ticket " +
            "AND ticketproduit.produit=produit "+
            "AND ticket.magasin=magasin "+
            "AND magasin.marque=marque "+
            "AND ticket.client= :identifier "+
            "AND produit.marque= :marque " +
            "GROUP BY ticketproduit.ticket")
    List<Object[]> AllTicketsByClientAndBrand(@Param("identifier") String identifier, @Param("marque") Marque marque);

    @Query("SELECT ticket.id,  ticket.checked,  marque.nom, Date(ticket.date), SUM((ticketproduit.produit.prix*ticketproduit.quantite)+((ticketproduit.produit.prix*ticketproduit.quantite*ticketproduit.tva)/100)) as  TTC "+
            "FROM Ticket ticket,Produit produit,Marque marque, Magasin magasin, TicketProduit ticketproduit "+
            "WHERE ticketproduit.ticket = ticket " +
            "AND ticketproduit.produit=produit "+
            "AND ticket.magasin=magasin "+
            "AND magasin.marque=marque "+
            "AND ticket.client= :identifier "+
            "GROUP BY ticketproduit.ticket")
    List<Object[]> AllNotificationByCLient(@Param("identifier") String identifier);

    @Query("SELECT ticket.id,  ticket.checked,  marque.nom, Date(ticket.date), SUM((ticketproduit.produit.prix*ticketproduit.quantite)+((ticketproduit.produit.prix*ticketproduit.quantite*ticketproduit.tva)/100)) as  TTC "+
            "FROM Ticket ticket,Produit produit,Marque marque, Magasin magasin, TicketProduit ticketproduit "+
            "WHERE ticketproduit.ticket = ticket " +
            "AND ticketproduit.produit=produit "+
            "AND ticket.magasin=magasin "+
            "AND magasin.marque=marque "+
            "And ticket.checked=false " +
            "AND ticket.client= :identifier "+
            "GROUP BY ticketproduit.ticket")
    List<Object[]> NotCheckedNotificationByCLient(@Param("identifier") String identifier);

    @Modifying
    @Transactional
    @Query("UPDATE Ticket ticket SET ticket.checked=true where ticket.client= :identifier")
    void updateCheckedNotificationsByClient(@Param("identifier") String identifier);

    @Query("SELECT ticket.id,  ticket.checked,  marque.nom, Date(ticket.date), SUM((ticketproduit.produit.prix*ticketproduit.quantite)+((ticketproduit.produit.prix*ticketproduit.quantite*ticketproduit.tva)/100)) as  TTC "+
            "FROM Ticket ticket,Produit produit,Marque marque, Magasin magasin, TicketProduit ticketproduit "+
            "WHERE ticketproduit.ticket = ticket " +
            "AND ticketproduit.produit=produit "+
            "AND ticket.magasin=magasin "+
            "AND magasin.marque=marque "+
            "And ticket.checked=true " +
            "AND ticket.client= :identifier "+
            "GROUP BY ticketproduit.ticket")
    List<Object[]> CheckedNotificationByCLient(@Param("identifier") String identifier);










}



