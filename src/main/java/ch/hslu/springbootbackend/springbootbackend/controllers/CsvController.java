package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.Service.CsvService.*;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.QuestionService;
import ch.hslu.springbootbackend.springbootbackend.payload.response.MessageResponse;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;

// TODO: Kann nur eine Mapping-Methode nehmen und als parameter noch die entity übergeben...
// TODO: Somit müsste nur eine Methode implementiert werden!!


//@CrossOrigin(origins = "http://localhost:80", maxAge = 3600)
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/csv")
public class CsvController {

    private final Logger LOG = LoggerFactory.getLogger(CsvController.class);

    private CsvQuestionService csvQuestionService;
    private CsvCategoryService csvCategoryService;
    private CsvCategorySetService csvCategorySetService;
    private CsvMediaService csvMediaService;

    public CsvController(CsvQuestionService csvQuestionService, CsvCategoryService csvCategoryService, CsvCategorySetService csvCategorySetService, CsvMediaService csvMediaService) {
        this.csvQuestionService = csvQuestionService;
        this.csvCategoryService = csvCategoryService;
        this.csvCategorySetService = csvCategorySetService;
        this.csvMediaService = csvMediaService;
    }


    @PostMapping("/questions")
    public ResponseEntity<MessageResponse> uploadQuestionFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (csvQuestionService.hasCSVFormat(file)) {
            try {
                csvQuestionService.saveNewEntities(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "! " + e.getMessage();
                LOG.error(message);
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
    }

    @PostMapping("/categories")
    public ResponseEntity<MessageResponse> uploadCategorieFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (csvCategoryService.hasCSVFormat(file)) {
            try {
                csvCategoryService.saveNewEntities(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "! " + e.getMessage();
                LOG.error(message);
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
    }

    @PostMapping("/categorysets")
    public ResponseEntity<MessageResponse> uploadCategorieSetFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (csvCategorySetService.hasCSVFormat(file)) {
            try {
                csvCategorySetService.saveNewEntities(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "! " + e.getMessage();
                LOG.error(message);
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
    }

    @PostMapping("/medias")
    public ResponseEntity<MessageResponse> uploadMediaFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (csvMediaService.hasCSVFormat(file)) {
            try {
                csvMediaService.saveNewEntities(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "! " + e.getMessage();
                LOG.error(message);
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
    }

}
