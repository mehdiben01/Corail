package com.example.Api_Core.Service;

import com.example.Api_Core.DTO.QRDataDTO;
import com.google.zxing.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface QRCode {
    String extractDataForQRCode(String jsonContent);
    byte[] generateQRCode(String text) throws Exception;

    ResponseEntity<byte[]> uploadFile(MultipartFile file);
 //   String readQRCodeFromImage(MultipartFile file);
    String storeData(QRDataDTO dto);
    ResponseEntity<?> AddTicketToBdd(@PathVariable String id);

}
