package ch.hslu.springbootbackend.springbootbackend.Service.CsvService;

import ch.hslu.springbootbackend.springbootbackend.Entity.Category;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.CategorySet;
import ch.hslu.springbootbackend.springbootbackend.Repository.CategoryRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.CategorySetRepository;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class CsvCategorySetService implements CsvService {
    private final Logger LOG = LoggerFactory.getLogger(CsvQuestionService.class);
    static String[] HEADER_CATEGORYSET = {"id", "categorieid", "title", "categorieSetNumber"};

    @Autowired
    private CategorySetRepository categorySetRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private HashMap<String, CategorySet> currentCreatedCategorySets;


    public List<CategorySet> saveNewEntities(MultipartFile file) {
        try {
            List<CategorySet> categorySets = parseCsv(file.getInputStream());
            return categorySetRepository.saveAll(categorySets);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<CategorySet> parseCsv(InputStream inputStream) {
        currentCreatedCategorySets = this.mapFromList(categorySetRepository.findAll());
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<CategorySet> newCategorieSets = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                    int categoryId = Integer.parseInt(csvRecord.get("categorieid"));
                    Optional<Category> categoryOfCategorySet = categoryRepository.findById(categoryId);
                    if (!categoryOfCategorySet.isPresent()) {
                        LOG.warn("No category with the Id "+categoryId + "categorySet created with category Null");
                    }
                    if(currentCreatedCategorySets.containsKey(csvRecord.get("categorieSetNumber"))){
                        LOG.warn("categorySet with number " + csvRecord.get("categorieSetNumber") + "already exists");
                    }else {
                        CategorySet categorySet = new CategorySet(Integer.parseInt(csvRecord.get("categorieSetId")), categoryOfCategorySet.get(), csvRecord.get("title"), csvRecord.get("categorieSetNumber"));
                        newCategorieSets.add(categorySet);
                        currentCreatedCategorySets.put(categorySet.getCategorySetNumber(), categorySet);
                    }
            }
            return newCategorieSets;

        } catch (IOException e) {
            throw new RuntimeException("Fail to parse CSV file: " + e.getMessage());
        }
    }

    private HashMap<String, CategorySet> mapFromList(List<CategorySet> categorySets){
        HashMap<String,CategorySet> map = new HashMap<>();
        for (CategorySet i: categorySets) map.put(i.getCategorySetNumber(),i);
        return map;
    }

}
