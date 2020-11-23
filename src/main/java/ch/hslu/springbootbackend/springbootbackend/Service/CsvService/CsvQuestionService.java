package ch.hslu.springbootbackend.springbootbackend.Service.CsvService;

import ch.hslu.springbootbackend.springbootbackend.Entity.Answer;
import ch.hslu.springbootbackend.springbootbackend.Entity.Media;
import ch.hslu.springbootbackend.springbootbackend.Entity.Question;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.CategorySet;
import ch.hslu.springbootbackend.springbootbackend.Entity.User;
import ch.hslu.springbootbackend.springbootbackend.Repository.*;
import ch.hslu.springbootbackend.springbootbackend.Utils.QuestionType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CsvQuestionService implements CsvService {

    private final Logger LOG = LoggerFactory.getLogger(CsvQuestionService.class);
    static String[] HEADER_QUESTION = {"categorySetId", "questionType", "questionPhrase", "Answer1", "Answer2", "Answer3", "Answer4", "Answer5", "CorrectAnswerAsLetter", "textIfCorrect", "textIfIncorrect", "questionImageId", "solutionImageId", "author", "pointsToAchieve"};

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CategorySetRepository categorySetRepository;
    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;
    private List<Answer> currentCreatedAnswers;

    CsvQuestionService(QuestionRepository questionRepository, AnswerRepository answerRepository, CategorySetRepository categorySetRepository, MediaRepository mediaRepository, UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.categorySetRepository = categorySetRepository;
        this.mediaRepository = mediaRepository;
        this.userRepository = userRepository;
    }

    public List<Question> saveNewEntities(MultipartFile file) {
        try {
            List<Question> questions = parseCsv(file.getInputStream());
            //List<Question> persistedQuestions = questionRepository.saveAll(questions);
            return questions;
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<Question> parseCsv(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Question> newQuestions = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                List<CategorySet> categorySet = new ArrayList<>();
                try {
                    CategorySet foundedCategorySet = this.getCategorySet(csvRecord.get("categorySetId"));
                    categorySet.add(foundedCategorySet);

                } catch (NumberFormatException ex) {
                    LOG.warn("Couldn't parse the founded categorySetId :: " + csvRecord.get("categorySetId") + " of the Data :: " + csvRecord.toString());
                    continue;
                }

                List<Answer> possibleAnswers = new ArrayList<>();
                for (int i = 1; i <= 5; i++) {
                    String answerPhrase = csvRecord.get("Answer" + i);
                    if (this.checkIfAnswerIsEmpty(answerPhrase)) {
                        continue;
                    }
                    String escapedAndEncodedString = this.escapeAndEncodeString(answerPhrase);
                    Optional<Answer> foundedAnswer = answerRepository.findByAnswerPhrase(escapedAndEncodedString);
                    if (foundedAnswer.isPresent()) {
                        possibleAnswers.add(foundedAnswer.get());
                    } else {
                        Answer newAnswerOfQuestion = new Answer(escapedAndEncodedString);
                        possibleAnswers.add(answerRepository.save(newAnswerOfQuestion));
                    }
                }

                List<Answer> correctAnswers = this.parseLettersToAnswers(csvRecord.get("CorrectAnswerAsLetter"), possibleAnswers);
                QuestionType questionType = QuestionType.fromString(csvRecord.get("questionType"));
                Media questionImage = null;
                Media solutionImage = null;
                User user;
                int pointsToAchieve = Integer.parseInt(csvRecord.get("pointsToAchieve"));

                if(!userRepository.existsByUsername(escapeAndEncodeString(csvRecord.get("autor")))){
                    user = new User(this.escapeAndEncodeString(csvRecord.get("autor")), null, null);
                }else{
                    user = userRepository.findByUsername(this.escapeAndEncodeString(csvRecord.get("autor"))).get();
                }
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
                        user,
                        categorySet,
                        questionImage,
                        solutionImage,
                        pointsToAchieve
                );
                Question newPersistedQuestion = questionRepository.save(newQuestion);
                newQuestions.add(newPersistedQuestion);
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


    private boolean checkIfAnswerIsEmpty(String answerPhrase) {
        return answerPhrase.equalsIgnoreCase("VOID") || answerPhrase.equalsIgnoreCase("");
    }

    private CategorySet getCategorySet(final String categorySetNumber) {
        Optional<CategorySet> categorySet = categorySetRepository.findByCategorySetNumber(categorySetNumber);
        if (categorySet.isPresent()) {
            return categorySet.get();
        } else {
            LOG.warn("Category set with id " + categorySetNumber + "not found" );
            return new CategorySet();
        }

    }


    private List<Answer> parseLettersToAnswers(String answersAsLetters, List<Answer> possibleAnswers) {
        List<Answer> correctAnswers = new ArrayList<>();
        if (answersAsLetters.contains("a")) {
            correctAnswers.add(possibleAnswers.get(0));
        }
        if (answersAsLetters.contains("b")) {
            correctAnswers.add(possibleAnswers.get(1));
        }
        if (answersAsLetters.contains("c")) {
            correctAnswers.add(possibleAnswers.get(2));
        }
        if (answersAsLetters.contains("d")) {
            correctAnswers.add(possibleAnswers.get(3));
        }
        if (answersAsLetters.contains("e")) {
            correctAnswers.add(possibleAnswers.get(4));
        }
        return correctAnswers;
    }

    private String escapeAndEncodeString(final String string) {
        String escapedSpanTagInString = string.replaceAll("<(/|S|B)[^>]*>", "");
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
            return false;
        }
    }

}
