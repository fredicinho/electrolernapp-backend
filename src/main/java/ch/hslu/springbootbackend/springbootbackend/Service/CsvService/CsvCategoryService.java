package ch.hslu.springbootbackend.springbootbackend.Service.CsvService;

import ch.hslu.springbootbackend.springbootbackend.Entity.Category;
import ch.hslu.springbootbackend.springbootbackend.Repository.CategoryRepository;
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
public class CsvCategoryService implements CsvService {
    static String[] HEADER_CATEGORY = {"id", "NAME"};

    private final CategoryRepository categoryRepository;

    CsvCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> saveNewEntities(MultipartFile file) {
        try {
            List<Category> categories = parseCsv(file.getInputStream());
            return categoryRepository.saveAll(categories);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    // TODO: For new Categories you dont have to pass the id. It will create one for themselves...
    public List<Category> parseCsv(InputStream inputStream) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Category> newCategories = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                if (checkIfCategoryExists(csvRecord.get("name"))) {
                    newCategories.add(new Category(Integer.parseInt(csvRecord.get("id")), csvRecord.get("name"), ""));
                }
            }

            return newCategories;

        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    private boolean checkIfCategoryExists(String categoryName) {
        return categoryRepository.findByName(categoryName).isEmpty();
    }
}
