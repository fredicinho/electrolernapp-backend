package ch.hslu.springbootbackend.springbootbackend.Service.CsvService;

import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Entity.CategorySet;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Repository.AnswerRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.CategorySetRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import ch.hslu.springbootbackend.springbootbackend.Utils.QuestionType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CsvQuestionService implements CsvService {

    static String[] HEADER_QUESTION = {"chapterId", "QuestionPhrase", "Answer1", "Answer2", "Answer3", "Answer4", "Answer5", "CorrectAnswerAsLetter", "textIfCorrect", "textIfIncorrect", "questionImageId", "solutionImageId"};

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CategorySetRepository categorySetRepository;

    CsvQuestionService(QuestionRepository questionRepository, AnswerRepository answerRepository, CategorySetRepository categorySetRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.categorySetRepository = categorySetRepository;
    }

    public List<Question> saveNewEntities(MultipartFile file) {
        try {
            List<Question> questions = parseCsv(file.getInputStream());
            List<Question> persistedQuestions = questionRepository.saveAll(questions);
            this.insertQuestionsIntoCategorySets(persistedQuestions);
            return persistedQuestions;
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
                CategorySet categorySet = this.getCategorySet(Integer.parseInt(csvRecord.get("chapterId")));
                List<Answer> possibleAnswers = new ArrayList<>();
                possibleAnswers.add(checkIfAnswerExists(csvRecord.get("Answer1")));
                possibleAnswers.add(checkIfAnswerExists(csvRecord.get("Answer2")));
                possibleAnswers.add(checkIfAnswerExists(csvRecord.get("Answer3")));
                possibleAnswers.add(checkIfAnswerExists(csvRecord.get("Answer4")));
                possibleAnswers.add(checkIfAnswerExists(csvRecord.get("Answer5")));
                Answer correctAnswer = parseLetterToAnswer(csvRecord.get("CorrectAnswer"), possibleAnswers);
                QuestionType questionType = QuestionType.valueOf("Multiple Choice");
                Question newQuestion = new Question(
                        csvRecord.get("QuestionPhrase"),
                        possibleAnswers,
                        correctAnswer,
                        questionType,
                        categorySet
                );
                newQuestions.add(newQuestion);

            }

            return newQuestions;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    /**
     * Checks if an Answer-Object with this answerphrase exists. If it exists it return the founded Answer. Else it creates a new one.
     *
     * @param answerPhrase
     * @return Answer
     */
    private Answer checkIfAnswerExists(String answerPhrase) {
        List<Answer> foundedAnswer = answerRepository.findByAnswerPhrase(answerPhrase);
        if (foundedAnswer.isEmpty()) {
            return new Answer(answerPhrase);
        } else {
            return foundedAnswer.get(0);
        }
    }

    private CategorySet getCategorySet(final int categorySetId) {
        Optional<CategorySet> categorySet = categorySetRepository.findById(categorySetId);
        if (categorySet.isPresent()) {
            return categorySet.get();
        } else {
            throw new NoSuchElementException("The categorySet with the id :: " + categorySetId + " doesn't exists!");
        }

    }

    private void insertQuestionsIntoCategorySets(final List<Question> questions) {
        for (Question question : questions) {
            question.getCategorySet().insertQuestion(question);
            categorySetRepository.save(question.getCategorySet());
        }
    }

    private Answer parseLetterToAnswer(String answerAsLetter, List<Answer> possibleAnswers) {
        Answer correctAnswer = null;
        switch (answerAsLetter) {
            case "a":
                correctAnswer = possibleAnswers.get(0);
                break;
            case "b":
                correctAnswer = possibleAnswers.get(1);
                break;
            case "c":
                correctAnswer = possibleAnswers.get(2);
                break;
            case "d":
                correctAnswer = possibleAnswers.get(3);
                break;
            case "e":
                correctAnswer = possibleAnswers.get(43);
                break;
            default:
                throw new IllegalArgumentException("The correct Answerletter :: " + answerAsLetter + " is not available among the possible Answers!");
        }

        return correctAnswer;
    }

}
