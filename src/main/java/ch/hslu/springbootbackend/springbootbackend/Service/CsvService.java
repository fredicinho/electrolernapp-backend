package ch.hslu.springbootbackend.springbootbackend.Service;
import ch.hslu.springbootbackend.Utils.QuestionType;
import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Entity.Category;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Repository.AnswerRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.CategoryRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    public static String TYPE = "text/csv";

    static String[] HEADER_QUESTION = { "QuestionPhrase", "Answer1", "Answer2", "Answer3", "Answer4", "Answer5", "CorrectAnswer" };
    static String[] HEADER_CATEGORY = {"id", "NAME"};

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    // Csv Question Service
    public void saveNewQuestions(MultipartFile file) {
        try {
            List<Question> tutorials = csvToQuestions(file.getInputStream());
            questionRepository.saveAll(tutorials);
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
    public void saveNewCategories(MultipartFile file) {
        try {
            List<Category> categories = csvToCategory(file.getInputStream());
            categoryRepository.saveAll(categories);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

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

}
