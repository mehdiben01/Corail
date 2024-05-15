package com.example.Api_Core.Service;

import com.example.Api_Core.DTO.QRDataDTO;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class QRCodeImpl implements QRCode {

    private Map<String, QRDataDTO> dataMap = new HashMap<>();


    private TicketServiceImpl ticketService;

    public QRCodeImpl( TicketServiceImpl ticketService) {
        this.ticketService = ticketService;
    }

    @Override
    public String extractDataForQRCode(String jsonContent) {
        JSONObject jsonObject = new JSONObject(jsonContent);
        JSONObject qrData = new JSONObject();
        String dateTicket = jsonObject.getString("date_ticket");
        String timeTicket = jsonObject.getString("time_ticket");
        String dateTimeTicket = dateTicket + " " + timeTicket;
        qrData.put("Nom_marque", jsonObject.getInt("Nom_marque"));
        qrData.put("date_ticket", dateTimeTicket);
        qrData.put("ticket_number", jsonObject.getString("ticket_number"));
        JSONArray produits = jsonObject.getJSONArray("produits");
        JSONArray produitsFormatted = new JSONArray();
        for (int i = 0; i < produits.length(); i++) {
            JSONObject produit = produits.getJSONObject(i);
            JSONObject produitFormatted = new JSONObject();
            produitFormatted.put("category", produit.getInt("category"));
            produitFormatted.put("reference", produit.getString("reference"));
            produitFormatted.put("quantity", produit.getInt("quantity"));
            produitFormatted.put("prix", produit.getDouble("prix"));
            produitFormatted.put("TVA", produit.getDouble("TVA"));
            produitsFormatted.put(produitFormatted);
        }
        qrData.put("produits", produitsFormatted);
        return qrData.toString();
    }

    @Override
    public byte[] generateQRCode(String link) throws Exception {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());

            int size = 200;

            BitMatrix bitMatrix = new QRCodeWriter().encode(link, BarcodeFormat.QR_CODE, size, size, hints);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new Exception("Failed to generate QR code: " + e.getMessage());
        }
    }

    public String storeData(QRDataDTO dto) {
        String id = UUID.randomUUID().toString();
        dataMap.put(id, dto);
        return id;
    }



    @Override
    public ResponseEntity<byte[]> uploadFile(MultipartFile file) {
        try {

            String jsonContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            String extractedData = extractDataForQRCode(jsonContent);

            QRDataDTO qrDataDTO = new QRDataDTO();
            qrDataDTO.setData(extractedData);

            String dtoId = storeData(qrDataDTO);

            byte[] qrcodeBytes = generateQRCode(dtoId);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrcodeBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to generate QR code".getBytes());
        }
    }



    @Override
    public ResponseEntity<?> AddTicketToBdd(String id) {
        QRDataDTO data = dataMap.get(id);

        if (data != null) {
            return ticketService.AjouterTicket(data.getData());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Data not found for the given ID");
        }
    }



 /*   public String readQRCodeFromImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return "File is empty or null";
        }

        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));

            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);

            return result.getText();
        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
            return "Failed to read QR code: " + e.getMessage();
        }
    } */



}
