package ch.hslu.springbootbackend.springbootbackend.Service.CsvService;

import ch.hslu.springbootbackend.springbootbackend.Entity.Profession;
import ch.hslu.springbootbackend.springbootbackend.Repository.ProfessionRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class CSVProfessionService implements CsvService{

    static String[] HEADER_QUESTION = {"pr_id", "label"};

    @Autowired
    ProfessionRepository professionRepository;
    @Autowired
    QuestionRepository questionRepository;

    private ConcurrentHashMap<String, Profession> currentCreatedProfessions;

    private final Logger LOG = LoggerFactory.getLogger(CsvQuestionService.class);
    private ExecutorService executor = Executors.newFixedThreadPool(8);

    public List<Profession> saveNewEntities(MultipartFile file) {
        try {
            Instant start = Instant.now();
            List<Profession> professions = parseCsv(file.getInputStream());
            Instant finish = Instant.now();
            List<Profession> persistedProfessions = professionRepository.saveAll(professions);
            LOG.info("Imported " + persistedProfessions.size() + " in " + Duration.between(start, finish).toMillis());
            return persistedProfessions;
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }
    public List<Profession> parseCsv(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Profession> newProfessions = new ArrayList<>();

            currentCreatedProfessions = this.mapFromList(professionRepository.findAll());
            //currentCreatedAnswers = this.mapFromList(answerRepository.findAll());
            //currentCreatedCategorySets = this.mapFromListCategorySet(categorySetRepository.findAll());
            //currentCreatedMedia = this.mapFromListMedia(mediaRepository.findAll());
            //currentCreatedUser = this.mapFromListUser(userRepository.findAll());

            List <CSVRecord> csvRecords = csvParser.getRecords();



            for(CSVRecord csvRecord : csvRecords) {
                Future<Profession> professionFuture = executor.submit(() -> createProfessionFromCSV(csvRecord));
                while (!professionFuture.isDone()) {
                }
                if(professionFuture.get() != null) {
                    newProfessions.add(professionFuture.get());
                }
                LOG.warn("question finished" + newProfessions.size());

            }
            return newProfessions;

        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    private Profession createProfessionFromCSV(CSVRecord csvRecord){
        String professionName = csvRecord.get("label");
        LOG.warn(professionName);
        if(currentCreatedProfessions.contains(professionName)){
            return null;
        }
        if(professionName.equals("null")){
            return null;
        }else {
            return new Profession(professionName);
        }
    }
    private ConcurrentHashMap<String, Profession> mapFromList(List<Profession> professionList){
        ConcurrentHashMap<String,Profession> map = new ConcurrentHashMap<>();
        for (Profession i : professionList) map.put(i.getName(),i);
        return map;
    }


}
