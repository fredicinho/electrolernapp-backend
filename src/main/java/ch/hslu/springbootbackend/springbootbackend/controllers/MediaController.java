package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.Entity.Media;
import ch.hslu.springbootbackend.springbootbackend.Repository.MediaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
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
    private final Logger LOG = LoggerFactory.getLogger(QuestionController.class);

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getMedia(@PathVariable(value = "id") Integer imageId) {
        Optional<Media> result = mediaRepository.findById(imageId);
        LOG.warn(result.toString());
        if (result.get() != null) {
            Media foundedMedia = result.get();
            // TODO: Uncomented path for local development
            var imgFile = new FileSystemResource("/Users/fred/Downloads/Lernapp Elektro Data/" + foundedMedia.getPath());
            //var imgFile = new FileSystemResource("/var/" + foundedMedia.getPath());
            byte[] bytes = new byte[0];
            try {
                bytes = StreamUtils.copyToByteArray(imgFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.unprocessableEntity().build();
            }
            return ResponseEntity
                    .ok()
                    .contentType(this.getContentType(foundedMedia.getType()))
                    .body(bytes);
        }

        return ResponseEntity.notFound().build();
    }

    private MediaType getContentType(final String type) throws UnsupportedOperationException {
        switch (type) {
            case "JPG":
                return MediaType.IMAGE_JPEG;

            case "GIF":
                return MediaType.IMAGE_GIF;

            case "PNG":
                return MediaType.IMAGE_PNG;

            default:
                throw new UnsupportedOperationException("The Format :: " + type + " can't be retrieved by Spring right now...");
        }
    }

}
