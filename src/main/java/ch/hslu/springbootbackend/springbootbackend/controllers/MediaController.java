package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.Entity.Media;
import ch.hslu.springbootbackend.springbootbackend.Repository.MediaRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:80", maxAge = 3600)
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/medias")
public class MediaController {

    private MediaRepository mediaRepository;

    public MediaController(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    // TODO: Media Type needs to be defined in Database!!!
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getMedia(@PathVariable(value = "id") Integer imageId) {
        Optional<Media> result = mediaRepository.findById(imageId);
        if (result.get() != null) {
            Media foundedMedia = result.get();
            var imgFile = new ClassPathResource("static/" + foundedMedia.getPath());
            byte[] bytes = new byte[0];
            try {
                bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.unprocessableEntity().build();
            }
            return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/testGif")
    public ResponseEntity<byte[]> getGif() throws IOException {

        var imgFile = new ClassPathResource("static/Medien/Bilder/000004_Frage_Bild.gif");
        byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_GIF)
                .body(bytes);
    }

    @GetMapping("/testJpg")
    public ResponseEntity<byte[]> getJpg() throws IOException {

        var imgFile = new ClassPathResource("static/Medien/Bilder/000006_Frage_Bild.jpg");
        byte[] bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());

        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_GIF)
                .body(bytes);
    }

}
