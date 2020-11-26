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
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.*;


@Service
public class CsvQuestionService implements CsvService {

    private final Logger LOG = LoggerFactory.getLogger(CsvQuestionService.class);
    static String[] HEADER_QUESTION = {"categorySetId", "questionType", "questionPhrase", "Answer1", "Answer2", "Answer3", "Answer4", "Answer5", "CorrectAnswerAsLetter", "textIfCorrect", "textIfIncorrect", "questionImageId", "solutionImageId", "author", "pointsToAchieve"};

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CategorySetRepository categorySetRepository;
    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;
    private ConcurrentHashMap<String, Answer> currentCreatedAnswers;
    private ConcurrentHashMap<String, User> currentCreatedUser;
    private ConcurrentHashMap<Integer, Media> currentCreatedMedia;
    private ConcurrentHashMap<String, CategorySet> currentCreatedCategorySets;
    private ExecutorService executor = Executors.newFixedThreadPool(8);

    CsvQuestionService(QuestionRepository questionRepository, AnswerRepository answerRepository, CategorySetRepository categorySetRepository, MediaRepository mediaRepository, UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.categorySetRepository = categorySetRepository;
        this.mediaRepository = mediaRepository;
        this.userRepository = userRepository;
    }

    public List<Question> saveNewEntities(MultipartFile file) {
        try {
            Instant start = Instant.now();
            List<Question> questions = parseCsv(file.getInputStream());
            Instant finish = Instant.now();
            List<Question> persistedQuestions = questionRepository.saveAll(questions);
            userRepository.saveAll(currentCreatedUser.values());
            LOG.info("Imported " + persistedQuestions.size() + " in " + Duration.between(start, finish).toMillis());
            return persistedQuestions;
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<Question> parseCsv(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<Question> newQuestions = new ArrayList<>();

            currentCreatedAnswers = this.mapFromList(answerRepository.findAll());
            currentCreatedCategorySets = this.mapFromListCategorySet(categorySetRepository.findAll());
            currentCreatedMedia = this.mapFromListMedia(mediaRepository.findAll());
            currentCreatedUser = this.mapFromListUser(userRepository.findAll());

            List <CSVRecord> csvRecords = csvParser.getRecords();



            for(CSVRecord csvRecord : csvRecords) {
                Future<Question> questionFuture = executor.submit(() -> createQuestionsFromCSV(csvRecord));
                while (!questionFuture.isDone()) {
                }
                newQuestions.add(questionFuture.get());
                LOG.warn("question finished" + newQuestions.size());

            }
            return newQuestions;

        } catch (IOException | InterruptedException | ExecutionException e) {
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
        //Optional<CategorySet> categorySet = categorySetRepository.findByCategorySetNumber(categorySetNumber);
        if (currentCreatedCategorySets.containsKey(categorySetNumber)) {
            return currentCreatedCategorySets.get(categorySetNumber);
        } else {
            LOG.warn("Category set with id " + categorySetNumber + "not found" );
            return null;
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
        //Optional<Media> foundedMedia = mediaRepository.findById(mediaId);
        if (currentCreatedMedia.containsKey(mediaId)) {
            return currentCreatedMedia.get(mediaId);
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

    private ConcurrentHashMap<String, Answer> mapFromList(List<Answer> answerList){
        ConcurrentHashMap<String,Answer> map = new ConcurrentHashMap<>();
        for (Answer i : answerList) map.put(i.getAnswerPhrase(),i);
        return map;
    }

    private ConcurrentHashMap<Integer, Media> mapFromListMedia(List<Media> answerList){
        ConcurrentHashMap<Integer,Media> map = new ConcurrentHashMap<>();
        for (Media i : answerList) map.put(i.getId(),i);
        return map;
    }

    private ConcurrentHashMap<String, User> mapFromListUser(List<User> answerList){
        ConcurrentHashMap<String,User> map = new ConcurrentHashMap<>();
        for (User i : answerList) map.put(i.getUsername(),i);
        return map;
    }

    private ConcurrentHashMap<String, CategorySet> mapFromListCategorySet(List<CategorySet> answerList){
        ConcurrentHashMap<String,CategorySet> map = new ConcurrentHashMap<>();
        for (CategorySet i : answerList) map.put(i.getCategorySetNumber(),i);
        return map;
    }

    private Question createQuestionsFromCSV(CSVRecord csvRecord) {
        List<Question> questionList = new ArrayList<>();
            List<CategorySet> categorySet = new ArrayList<>();
            try {
                CategorySet foundedCategorySet = this.getCategorySet(csvRecord.get("categorySetId"));
                categorySet.add(foundedCategorySet);

            } catch (NumberFormatException ex) {
                LOG.warn("Couldn't parse the founded categorySetId :: " + csvRecord.get("categorySetId") + " of the Data :: " + csvRecord.toString());
            }

            List<Answer> possibleAnswers = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                String answerPhrase = csvRecord.get("Answer" + i);
                if (this.checkIfAnswerIsEmpty(answerPhrase)) {
                    continue;
                }
                String escapedAndEncodedString = this.escapeAndEncodeString(answerPhrase);
                //Optional<Answer> foundedAnswer = answerRepository.findByAnswerPhrase(escapedAndEncodedString);
                if (currentCreatedAnswers.containsKey(escapedAndEncodedString)) {
                    possibleAnswers.add(currentCreatedAnswers.get(escapedAndEncodedString));
                } else {
                    Answer newAnswerOfQuestion = new Answer(escapedAndEncodedString);
                    possibleAnswers.add(answerRepository.save(newAnswerOfQuestion));
                    currentCreatedAnswers.put(newAnswerOfQuestion.getAnswerPhrase(), newAnswerOfQuestion);
                }
            }

            List<Answer> correctAnswers = this.parseLettersToAnswers(csvRecord.get("CorrectAnswerAsLetter"), possibleAnswers);
            QuestionType questionType = QuestionType.fromString(csvRecord.get("questionType"));
            Media questionImage = null;
            Media solutionImage = null;
            User user = null;
            int pointsToAchieve = Integer.parseInt(csvRecord.get("pointsToAchieve"));
            String escapedAndEncodedString = this.escapeAndEncodeString(csvRecord.get("autor"));
            if (currentCreatedUser.containsKey(escapedAndEncodedString)) {
                user = currentCreatedUser.get(escapedAndEncodedString);
            } else {
                user = new User(this.escapeAndEncodeString(csvRecord.get("autor")), null, null);
                //userRepository.save(user);
                currentCreatedUser.put(escapedAndEncodedString, user);
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
            return newQuestion;
    }

}
