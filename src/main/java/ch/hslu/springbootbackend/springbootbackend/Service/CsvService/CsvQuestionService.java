package ch.hslu.springbootbackend.springbootbackend.Service.CsvService;

import ch.hslu.springbootbackend.springbootbackend.Entity.*;
import ch.hslu.springbootbackend.springbootbackend.Repository.AnswerRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.CategorySetRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.MediaRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.QuestionRepository;
import ch.hslu.springbootbackend.springbootbackend.Utils.QuestionType;
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
import java.util.*;

import org.apache.commons.text.StringEscapeUtils;

@Service
public class CsvQuestionService implements CsvService {

    private final Logger LOG = LoggerFactory.getLogger(CsvQuestionService.class);
    static String[] HEADER_QUESTION = {"chapterId", "QuestionPhrase", "Answer1", "Answer2", "Answer3", "Answer4", "Answer5", "CorrectAnswerAsLetter", "textIfCorrect", "textIfIncorrect", "questionImageId", "solutionImageId"};

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CategorySetRepository categorySetRepository;
    private final MediaRepository mediaRepository;
    private List<Answer> currentCreatedAnswers;

    CsvQuestionService(QuestionRepository questionRepository, AnswerRepository answerRepository, CategorySetRepository categorySetRepository, MediaRepository mediaRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.categorySetRepository = categorySetRepository;
        this.mediaRepository = mediaRepository;
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

            this.currentCreatedAnswers = new ArrayList<>();

            for (CSVRecord csvRecord : csvRecords) {
                CategorySet categorySet = null;
                try {
                    categorySet = this.getCategorySet(Integer.parseInt(csvRecord.get("chapterId")));
                } catch (NumberFormatException ex) {
                    LOG.warn("Couldn't parse the founded Chapter ID :: " + csvRecord.get("chapterId") + " of the Data :: " + csvRecord.toString());
                    continue;
                }
                List<Answer> possibleAnswers = new ArrayList<>();
                for (int i = 1; i<=5; i++) {
                    if (checkIfAnswerIsVoid(csvRecord.get("Answer" + i))) {
                        break;
                    } else {
                        Answer newAnswer = checkIfAnswerExists(escapeAndEncodeString(csvRecord.get("Answer" + i)));
                        possibleAnswers.add(newAnswer);
                        currentCreatedAnswers.add(newAnswer);
                    }
                }
                List<Answer> correctAnswers = parseLetterToAnswer(csvRecord.get("CorrectAnswerAsLetter"), possibleAnswers);
                QuestionType questionType = QuestionType.fromString("Multiple Choice");
                Media questionImage = null;
                Media solutionImage = null;
                if (this.checkIfMediaAvailableInCsv(csvRecord.get("questionImageId"))) {
                    questionImage = this.getMediaById(Integer.parseInt(csvRecord.get("questionImageId")));
                }
                if (this.checkIfMediaAvailableInCsv(csvRecord.get("solutionImageId"))) {
                    solutionImage = this.getMediaById(Integer.parseInt(csvRecord.get("solutionImageId")));
                }

                Question newQuestion = new Question(
                        this.escapeAndEncodeString(csvRecord.get("QuestionPhrase")),
                        possibleAnswers,
                        correctAnswers,
                        questionType,
                        categorySet,
                        questionImage,
                        solutionImage
                );
                newQuestions.add(newQuestion);

            }

            return newQuestions;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    /**
     * Checks if an Answer-Object with this answerphrase exists inMemory or in Database.
     * If it exists it return the founded Answer. Else it creates a new one.
     *
     * @param answerPhrase
     * @return Answer
     */
    private Answer checkIfAnswerExists(String answerPhrase) {
        for (Answer answer : this.currentCreatedAnswers) {
            if (answer.getAnswerPhrase() == answerPhrase) {
                return answer;
            }
        }
        List<Answer> foundedAnswer = answerRepository.findByAnswerPhrase(answerPhrase);
        if (foundedAnswer.isEmpty()) {
            return new Answer(answerPhrase);
        } else {
            return foundedAnswer.get(0);
        }
    }

    private boolean checkIfAnswerIsVoid(String answerPhrase) {
        if (answerPhrase == "VOID" || answerPhrase == "") {
            return true;
        }
        return false;
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

    private List<Answer> parseLetterToAnswer(String answerAsLetter, List<Answer> possibleAnswers) {
        List<Answer> correctAnswer = new LinkedList<>();
        if (answerAsLetter.contains("a")) {
            correctAnswer.add(possibleAnswers.get(0));
        }
        if (answerAsLetter.contains("b")) {
            correctAnswer.add(possibleAnswers.get(1));
        }
        if (answerAsLetter.contains("c")) {
            correctAnswer.add(possibleAnswers.get(2));
        }
        if (answerAsLetter.contains("d")) {
            correctAnswer.add(possibleAnswers.get(3));
        }
        if (answerAsLetter.contains("e")) {
            correctAnswer.add(possibleAnswers.get(4));
        }

        return correctAnswer;
    }

    private String escapeAndEncodeString(final String string) {
        String escapedSpanTagInString = string.replaceAll("<(/|S)[^>]*>", "");
        String escapedAndEncodedHTMLEntitiesInString = StringEscapeUtils.unescapeHtml4(escapedSpanTagInString);
        return escapedAndEncodedHTMLEntitiesInString;
    }

    private Media getMediaById(final int mediaId) {
        Optional<Media> foundedMedia = mediaRepository.findById(mediaId);
        if (foundedMedia.isPresent()) {
            return foundedMedia.get();
        } else {
            throw new NoSuchElementException("The Media with the id :: " + mediaId + " doesn't exists in the database!");
        }
    }

    private boolean checkIfMediaAvailableInCsv(final String mediaId) {
        try {
            int idAsInteger = Integer.parseInt(mediaId);
            return true;
        } catch (NumberFormatException ex) {
            LOG.info(ex.getMessage());
            return false;
        }
    }

}
