package ch.hslu.springbootbackend.springbootbackend.Service.CSV;
import ch.hslu.springbootbackend.Utils.QuestionType;
import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Entity.Category;
import ch.hslu.springbootbackend.springbootbackend.Entity.CategorySet;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Repository.AnswerRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.CategoryRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.CategorySetRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import ch.hslu.springbootbackend.springbootbackend.controllers.CsvController;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CsvService {
    private final Logger LOG = LoggerFactory.getLogger(CsvService.class);

    public static String TYPE = "text/csv";

    static String[] HEADER_QUESTION = { "QuestionPhrase", "Answer1", "Answer2", "Answer3", "Answer4", "Answer5", "CorrectAnswer" };
    static String[] HEADER_CATEGORY = {"id", "NAME"};
    static String[] HEADER_CATEGORYSET = {"id", "categorieid", "title", "categorieSetNumber"};

    private AnswerRepository answerRepository;
    private QuestionRepository questionRepository;
    private CategoryRepository categoryRepository;
    private CategorySetRepository categorySetRepository;

    public CsvService(AnswerRepository answerRepository, QuestionRepository questionRepository, CategoryRepository categoryRepository, CategorySetRepository categorySetRepository) {
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
        this.categorySetRepository = categorySetRepository;
    }

    public boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    // Csv Question Service
    public List<Question> saveNewQuestions(MultipartFile file) {
        try {
            List<Question> tutorials = csvToQuestions(file.getInputStream());
            return questionRepository.saveAll(tutorials);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    private List<Question> csvToQuestions(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Question> newQuestions = new ArrayList<Question>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                List<Answer> possibleAnswers = new ArrayList<>();
                possibleAnswers.add(checkIfAnswerExists(csvRecord.get("Answer1")));
                possibleAnswers.add(checkIfAnswerExists(csvRecord.get("Answer2")));
                possibleAnswers.add(checkIfAnswerExists(csvRecord.get("Answer3")));
                possibleAnswers.add(checkIfAnswerExists(csvRecord.get("Answer4")));
                possibleAnswers.add(checkIfAnswerExists(csvRecord.get("Answer5")));
                Answer correctAnswer = checkIfAnswerExists(csvRecord.get("CorrectAnswer"));
                QuestionType questionType = QuestionType.valueOf(csvRecord.get("QuestionType"));
                Question newQuestion = new Question(
                        csvRecord.get("QuestionPhrase"),
                        possibleAnswers,
                        correctAnswer,
                        questionType
                );
                newQuestions.add(newQuestion);
            }

            return newQuestions;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    private Answer checkIfAnswerExists(String answerPhrase) {
        List<Answer> foundedAnswer = answerRepository.findByAnswerPhrase(answerPhrase);
        if (foundedAnswer.isEmpty()) {
            return new Answer(answerPhrase);
        } else {
            return foundedAnswer.get(0);
        }
    }

    // CSV Categorie Service
    public List<Category> saveNewCategories(MultipartFile file) {
        try {
            List<Category> categories = csvToCategory(file.getInputStream());
            return categoryRepository.saveAll(categories);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    // TODO: For new Categories you dont have to pass the id. It will create one for themselves...
    private List<Category> csvToCategory(InputStream inputStream) {
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

    // CSV CategorieSet Service
    public List<CategorySet> saveNewCategorieSets(MultipartFile file) {
        try {
            List<CategorySet> categorySets = csvToCategorySet(file.getInputStream());
            return categorySetRepository.saveAll(categorySets);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    // TODO: For new CategorySets you dont have to pass the id. It will create one for themselves...
    private List<CategorySet> csvToCategorySet(InputStream inputStream) {
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
        List<CategorySet> sets = categorySetRepository.findByTitleAndCategorySetNumber(title, categorySetNumber);
        LOG.info(sets.toString());
        return !categorySetRepository.findByTitleAndCategorySetNumber(title, categorySetNumber).isEmpty();

    }

}
