package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.Entity.Media;
import ch.hslu.springbootbackend.springbootbackend.Repository.MediaRepository;
import ch.hslu.springbootbackend.springbootbackend.payload.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost:80", maxAge = 3600)
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/medias")
@PreAuthorize("hasRole('ROLE_EXAM') or hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
public class MediaController {

    @Autowired
    private final MediaRepository mediaRepository;


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
            //var imgFile = new FileSystemResource("/Users/fred/Downloads/Lernapp Elektro Data/" + foundedMedia.getPath());
            var imgFile = new FileSystemResource("/var/" + foundedMedia.getPath());
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

    @PostMapping("/medias")
    public ResponseEntity<MessageResponse> uploadMediaFile(@RequestParam("file") MultipartFile file) {
        String message = "";



        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
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
