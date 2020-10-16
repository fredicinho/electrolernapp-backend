package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.Service.CSV.CsvService;
import ch.hslu.springbootbackend.springbootbackend.payload.response.MessageResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;

//@CrossOrigin(origins = "http://localhost:80", maxAge = 3600)
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/csv")
public class CsvController {

    private final Logger LOG = LoggerFactory.getLogger(CsvController.class);

    @Autowired
    CsvService csvService;

    @PostMapping("/questions")
    public ResponseEntity<MessageResponse> uploadQuestionFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (csvService.hasCSVFormat(file)) {
            try {
                csvService.saveNewQuestions(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
    }

    @PostMapping("/categories")
    public ResponseEntity<MessageResponse> uploadCategorieFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (csvService.hasCSVFormat(file)) {
            try {
                csvService.saveNewCategories(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
    }

    @PostMapping("/categorysets")
    public ResponseEntity<MessageResponse> uploadCategorieSetFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (csvService.hasCSVFormat(file)) {
            try {
                csvService.saveNewCategorieSets(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
            } catch (Exception e) {
                LOG.error(e.getMessage());
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
    }

}
