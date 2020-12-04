package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.DTO.ExamResultDTO;
import ch.hslu.springbootbackend.springbootbackend.DTO.ExamSetDTO;
import ch.hslu.springbootbackend.springbootbackend.DTO.QuestionDTO;
import ch.hslu.springbootbackend.springbootbackend.DTO.UserDTO;
import ch.hslu.springbootbackend.springbootbackend.Service.CsvService.*;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.ExamResultService;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.ExamSetService;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.QuestionService;
import ch.hslu.springbootbackend.springbootbackend.payload.response.MessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// TODO: Kann nur eine Mapping-Methode nehmen und als parameter noch die entity übergeben...
// TODO: Somit müsste nur eine Methode implementiert werden!!


//@CrossOrigin(origins = "http://localhost:80", maxAge = 3600)
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/csv")
//@PreAuthorize("hasRole('ROLE_ADMIN')")
public class CsvController {

    private final Logger LOG = LoggerFactory.getLogger(CsvController.class);
    @Autowired
    CsvProfessionService csvProfessionService;
    @Autowired
    CsvUserService csvUserService;
    @Autowired
    private CsvQuestionService csvQuestionService;
    @Autowired
    private CsvCategoryService csvCategoryService;
    @Autowired
    private CsvCategorySetService csvCategorySetService;
    @Autowired
    private CsvMediaService csvMediaService;
    @Autowired
    QuestionService questionService;
    @Autowired
    ExamSetService examSetService;
    @Autowired
    ExamResultService examResultService;

    @PostMapping("/all")
    public ResponseEntity<List<MessageResponse>> uploadAllFiles(@RequestParam("categoryFile") MultipartFile categoryFile, @RequestParam("categorySetFile") MultipartFile categorySetFile, @RequestParam("mediaFile") MultipartFile mediaFile, @RequestParam("questionFile") MultipartFile questionFile) {
        List<MessageResponse> messageList = new ArrayList<>();
        String categoryMessage = "";

        if (csvCategoryService.hasCSVFormat(categoryFile)) {
            try {
                csvCategoryService.saveNewEntities(categoryFile);

                categoryMessage = "Uploaded the file successfully: " + categoryFile.getOriginalFilename();
                messageList.add(new MessageResponse(categoryMessage));
            } catch (Exception e) {
                categoryMessage = "Could not upload the file: " + categoryFile.getOriginalFilename() + "! " + e.getMessage();
                LOG.error(categoryMessage);
                e.printStackTrace();
                messageList.add(new MessageResponse(categoryMessage));
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(messageList);
            }
        }


        String categorySetMessage = "";

        if (csvCategorySetService.hasCSVFormat(categorySetFile)) {
            try {
                csvCategorySetService.saveNewEntities(categorySetFile);

                categorySetMessage = "Uploaded the file successfully: " + categorySetFile.getOriginalFilename();
                messageList.add(new MessageResponse(categorySetMessage));
            } catch (Exception e) {
                categorySetMessage = "Could not upload the file: " + categorySetFile.getOriginalFilename() + "! " + e.getMessage();
                LOG.error(categorySetMessage);
                e.printStackTrace();
                messageList.add(new MessageResponse(categorySetMessage));
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(messageList);
            }
        }


        String mediaMessage = "";

        if (csvMediaService.hasCSVFormat(mediaFile)) {
            try {
                csvMediaService.saveNewEntities(mediaFile);

                mediaMessage = "Uploaded the file successfully: " + mediaFile.getOriginalFilename();
                messageList.add(new MessageResponse(mediaMessage));
            } catch (Exception e) {
                mediaMessage = "Could not upload the file: " + mediaFile.getOriginalFilename() + "! " + e.getMessage();
                LOG.error(mediaMessage);
                e.printStackTrace();
                messageList.add(new MessageResponse(mediaMessage));
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(messageList);
            }
        }


        String questionMessage = "";

        if (csvQuestionService.hasCSVFormat(questionFile)) {
            try {
                csvQuestionService.saveNewEntities(questionFile);

                questionMessage = "Uploaded the file successfully: " + questionFile.getOriginalFilename();
                messageList.add(new MessageResponse(questionMessage));
            } catch (Exception e) {
                questionMessage = "Could not upload the file: " + questionFile.getOriginalFilename() + "! " + e.getMessage();
                LOG.error(questionMessage);
                e.printStackTrace();
                messageList.add(new MessageResponse(questionMessage));
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(messageList);
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageList);
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

    @PostMapping("/professions")
    public ResponseEntity<MessageResponse> uploadProfessionFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (csvProfessionService.hasCSVFormat(file)) {
            try {
                csvProfessionService.saveNewEntities(file);

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

    @PostMapping("/users")
    public ResponseEntity<MessageResponse> uploadUserFile(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (csvUserService.hasCSVFormat(file)) {
            try {
                csvUserService.saveNewEntities(file);

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

    @GetMapping("/users/export")
    public void exportUsersToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);


        List<UserDTO> listUserDTO = csvUserService.getAllAsDTO();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"id", "username", "email", "profession", "roles", "schoolClassesIn"};
        String[] nameMapping = {"id", "username", "email", "profession", "roles", "schoolClassesIn"};

        //csvWriter.write(csvHeader);
        for (UserDTO user : listUserDTO) {
            csvWriter.write(user, nameMapping);
        }

        csvWriter.close();
    }

    @GetMapping("/questions/export")
    public void exportQuestionsToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=questions_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);


        List<QuestionDTO> listQuestionDto = questionService.getAllQuestions();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        //String[] csvHeader = {"id", "username", "email", "profession", "roles", "schoolClassesIn"};
        String[] nameMapping = {"id", "questionPhrase", "possibleAnswers", "correctAnswers", "questionType", "pointsToAchieve", "categorySetIds", "questionImageId", "answerImageId", "questionLevel", "professionsList" };

        //csvWriter.write(csvHeader);
        for (QuestionDTO question : listQuestionDto) {
            csvWriter.write(question, nameMapping);
        }

        csvWriter.close();
    }

    @GetMapping("/examSets/export")
    public void exportExamSetsToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=questions_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);


        List<ExamSetDTO> examSetDTOS = examSetService.getAllExamSets();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        //String[] csvHeader = {"id", "username", "email", "profession", "roles", "schoolClassesIn"};
        String[] nameMapping = {"examSetId", "title", "startDate", "endDate", "questionsInExamSet", "schoolClassesInExamSet", "createdBy", "description" };

        //csvWriter.write(csvHeader);
        for (ExamSetDTO examSetDTO : examSetDTOS) {
            csvWriter.write(examSetDTO, nameMapping);
        }

        csvWriter.close();
    }
    @GetMapping("/examResults/export")
    public void exportExamResultsToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=questions_" + currentDateTime + ".csv";
        response.setHeader(headerKey, headerValue);


        List<ExamResultDTO> examResultDTOS = examResultService.getAll();

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        //String[] csvHeader = {"id", "username", "email", "profession", "roles", "schoolClassesIn"};
        String[] nameMapping = {"examResultId", "pointsAchieved", "username", "questionId", "examSetId", "sendedAnswers", "changedByTeacher"};

        //csvWriter.write(csvHeader);
        for (ExamResultDTO examResultDTO : examResultDTOS) {
            csvWriter.write(examResultDTO, nameMapping);
        }

        csvWriter.close();
    }


}