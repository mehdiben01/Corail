package com.example.Api_Core.Service;

import com.example.Api_Core.DTO.MarqueDTO;
import com.example.Api_Core.Entity.Marque;
import com.example.Api_Core.Mapper.MarqueMapper;
import com.example.Api_Core.Repository.MarqueRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MarqueServiceImpl implements MarqueService {

    private MarqueRepository marqueRepository;

    private MarqueMapper marqueMapper;
    private  ImageServiceImpl imageService;

    public MarqueServiceImpl(MarqueRepository marqueRepository, MarqueMapper marqueMapper, ImageServiceImpl imageService) {
        this.marqueRepository = marqueRepository;
        this.marqueMapper = marqueMapper;
        this.imageService = imageService;
    }

    @Override
    public ResponseEntity<?> AjouterMarque(MarqueDTO marqueDTO, MultipartFile file) {
        boolean existingMarque = marqueRepository.existsMarqueByIce(marqueDTO.getIce());
        if(existingMarque){
            return ResponseEntity.ok("Marque deja exist");
        }
        Marque marque = marqueMapper.fromMarqueDTO(marqueDTO);
        String cheminimage = imageService.addImage(marqueDTO, file);
        if(!file.isEmpty()){
            if(cheminimage==null){
                return ResponseEntity.ok(ImageServiceImpl.IMAGE_SAVE_ERROR_MESSAGE);
            }
        }
        marque.setCheminImage(cheminimage);
        if(marqueDTO.getIce() == null || marqueDTO.getNom() == null || marqueDTO.getSiteweb() ==null) {
            return ResponseEntity.ok("Remplir tous les champs");
        }


        marqueRepository.save(marque);
        System.out.println(marque);
        MarqueDTO marqueDTO1 = marqueMapper.fromMarque(marque);
        if(marqueDTO1 != null){
            return ResponseEntity.ok("Marque ajoute avec succes");
        }else{
            return ResponseEntity.ok("Erreur");
        }

    }
}
