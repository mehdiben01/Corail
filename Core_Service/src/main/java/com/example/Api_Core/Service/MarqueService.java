package com.example.Api_Core.Service;

import com.example.Api_Core.DTO.MarqueDTO;
import com.example.Api_Core.Entity.Marque;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface MarqueService {
    ResponseEntity<?> AjouterMarque(MarqueDTO marqueDTO, MultipartFile imageFile);
}
