package com.example.Api_Core.Controller;

import com.example.Api_Core.DTO.TicketDTO;
import com.example.Api_Core.Entity.Categorie;
import com.example.Api_Core.Entity.Marque;
import com.example.Api_Core.Entity.Ticket;
import com.example.Api_Core.Repository.CategorieRepository;
import com.example.Api_Core.Repository.MarqueRepository;
import com.example.Api_Core.Service.QRCodeImpl;
import com.example.Api_Core.Service.TicketServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/core")
public class TicketController {

    private QRCodeImpl qrCode;
    private TicketServiceImpl ticketService;
    private CategorieRepository categorieRepository;

    private MarqueRepository marqueRepository;



    public TicketController(QRCodeImpl qrCode, TicketServiceImpl ticketService, CategorieRepository categorieRepository, MarqueRepository marqueRepository) {


        this.qrCode = qrCode;


        this.ticketService = ticketService;
        this.categorieRepository = categorieRepository;
        this.marqueRepository = marqueRepository;
    }


    @PostMapping("/upload")
    public ResponseEntity<byte[]> uploadFile(@RequestParam("file") MultipartFile file) {
      return qrCode.uploadFile(file);
    }

    @PostMapping("/data/{id}")
    public ResponseEntity<?> getData(@PathVariable String id) {
        return qrCode.AddTicketToBdd(id);
    }

    @GetMapping("/Tickets")
    public ResponseEntity<List<Object[]>> getTicketsClient(){
        return ticketService.getAllTicketsByClient();
    }
    @GetMapping("/Tickets-Filter")
    public ResponseEntity<List<Object[]>> getTicketsClientFilter(@RequestParam("categorie") String categorie, @RequestParam String dateT) throws ParseException {
        Categorie categorieObj = categorieRepository.findCategorieByNom(categorie);
        return ticketService.getAllTicketsByClientAndFilter(categorieObj,dateT);
    }
    @GetMapping("/Tickets-Brand")
    public ResponseEntity<List<Object[]>> getTicketsClientBrand(@RequestParam("marque") Long marque){
        Marque marqueobj = marqueRepository.findMarqueById(marque);
        return ticketService.getAllTicketsClientByBrand(marqueobj);
    }
    @GetMapping("/TicketId")
    public ResponseEntity<List<Object[]>> getTicketDetails(@RequestParam("id")Long id){
        return ticketService.getDetailsTicket(id);
    }

    @GetMapping("/AllNotif")
    public ResponseEntity<List<Object[]>> getNotifByClient(){
        return ticketService.AllNotifClient();
    }

    @PostMapping("/NotNotif")
    public ResponseEntity<List<Object[]>> getNotNotifByClient(){
        return ticketService.NotNotifClient();
    }

    @GetMapping("/CheckedNotif")
    public ResponseEntity<List<Object[]>> getCheckedNotifClient(){
        return ticketService.ChckedNotifClient();
    }









/*    @PostMapping("/read_qrcode")
    public ResponseEntity<?> readQRCodeFromImage(MultipartFile file){
        String qrCodeData = qrCode.readQRCodeFromImage(file);
        return ticketService.AjouterTicket(qrCodeData);
    } */

}
