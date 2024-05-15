package com.example.Api_Core.Controller;

import com.example.Api_Core.DTO.CategorieDTO;
import com.example.Api_Core.DTO.MarqueDTO;
import com.example.Api_Core.Service.CategorieServiceImpl;
import com.example.Api_Core.Service.MarqueService;
import com.example.Api_Core.Service.MarqueServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/core")
public class ProduitController {

    private CategorieServiceImpl categorieService;

    private MarqueServiceImpl marqueService;

    public ProduitController(CategorieServiceImpl categorieService, MarqueServiceImpl marqueService) {
        this.categorieService = categorieService;
        this.marqueService = marqueService;
    }

    @PostMapping("/AjouterCategorie")
    public ResponseEntity<?> AddCategory(@RequestBody  CategorieDTO categorieDTO){
        return categorieService.AjouterCategorie(categorieDTO);
    }

    @PostMapping("/AjouterMarque")
    public ResponseEntity<?> AddMarque(@ModelAttribute MarqueDTO marqueDTO,@RequestParam("file") MultipartFile file){
        return marqueService.AjouterMarque(marqueDTO,file);
    }
}
