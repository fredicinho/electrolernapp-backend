package ch.hslu.springbootbackend.springbootbackend.Service.CsvService;

import ch.hslu.springbootbackend.springbootbackend.Entity.Category;
import ch.hslu.springbootbackend.springbootbackend.Entity.CategorySet;
import ch.hslu.springbootbackend.springbootbackend.Repository.CategoryRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.CategorySetRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvCategorySetService implements CsvService {
    static String[] HEADER_CATEGORYSET = {"id", "categorieid", "title", "categorieSetNumber"};

    private final CategorySetRepository categorySetRepository;
    private final CategoryRepository categoryRepository;

    CsvCategorySetService(CategorySetRepository categorySetRepository, CategoryRepository categoryRepository) {
        this.categorySetRepository = categorySetRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<CategorySet> saveNewEntities(MultipartFile file) {
        try {
            List<CategorySet> categorySets = parseCsv(file.getInputStream());
            return categorySetRepository.saveAll(categorySets);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    // TODO: For new CategorySets you dont have to pass the id. It will create one for themselves...
    public List<CategorySet> parseCsv(InputStream inputStream) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<CategorySet> newCategorieSets = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                int categoryId = Integer.parseInt(csvRecord.get("categorieid"));
                List<Category> categoryOfCategorySet = categoryRepository.findById(categoryId);
                if (categoryOfCategorySet.isEmpty()) {
                    throw new IOException("The Category with the passed id = " + categoryId + " doesn't exists!");
                }
                if (checkIfCategorySetExists(csvRecord.get("title"), csvRecord.get("categorieSetNumber"))) {
                    newCategorieSets.add(new CategorySet(Integer.parseInt(csvRecord.get("categorieSetId")), categoryOfCategorySet.get(0), csvRecord.get("title"), csvRecord.get("categorieSetNumber")));
                }
            }
            return newCategorieSets;

        } catch (IOException e) {
            throw new RuntimeException("Fail to parse CSV file: " + e.getMessage());
        }
    }

    private boolean checkIfCategorySetExists(String title, String categorySetNumber) {
        return !categorySetRepository.findByTitleAndCategorySetNumber(title, categorySetNumber).isEmpty();
    }
}
