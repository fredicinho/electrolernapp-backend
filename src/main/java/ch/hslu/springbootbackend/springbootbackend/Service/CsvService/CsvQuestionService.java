package ch.hslu.springbootbackend.springbootbackend.Service.CsvService;

import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Repository.AnswerRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import ch.hslu.springbootbackend.springbootbackend.Utils.QuestionType;
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
public class CsvQuestionService implements CsvService{

    static String[] HEADER_QUESTION = { "QuestionPhrase", "Answer1", "Answer2", "Answer3", "Answer4", "Answer5", "CorrectAnswer" };

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    CsvQuestionService(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    public List<Question> saveNewEntities(MultipartFile file) {
        try {
            List<Question> tutorials = parseCsv(file.getInputStream());
            return questionRepository.saveAll(tutorials);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<Question> parseCsv(InputStream is) {
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
}
