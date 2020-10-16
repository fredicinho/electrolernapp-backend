package ch.hslu.springbootbackend.springbootbackend.Service;
import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Repository.AnswerRepository;
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

    // TODO: Change Headers for QuestionObject
    static String[] HEADERs = { "QuestionPhrase", "Answer1", "Answer2", "Answer3", "Answer4", "Answer5", "CorrectAnswer" };

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    public void save(MultipartFile file) {
        try {
            List<Question> tutorials = csvToQuestions(file.getInputStream());
            questionRepository.saveAll(tutorials);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<Question> getAllTutorials() {
        return questionRepository.findAll();
    }

    public boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
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
                Question newQuestion = new Question(
                        csvRecord.get("QuestionPhrase"),
                        possibleAnswers,
                        correctAnswer
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

}
