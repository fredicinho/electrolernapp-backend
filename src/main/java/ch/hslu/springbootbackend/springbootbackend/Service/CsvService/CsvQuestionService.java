package ch.hslu.springbootbackend.springbootbackend.Service.CsvService;

import ch.hslu.springbootbackend.springbootbackend.Entity.*;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.CategorySet;
import ch.hslu.springbootbackend.springbootbackend.Repository.*;
import ch.hslu.springbootbackend.springbootbackend.Utils.QuestionType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
    static String[] HEADER_QUESTION = {"categorySetId", "questionType", "questionPhrase", "Answer1", "Answer2", "Answer3", "Answer4", "Answer5", "CorrectAnswerAsLetter", "textIfCorrect", "textIfIncorrect", "questionImageId", "solutionImageId", "author", "pointsToAchieve", "profession", "level"};

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CategorySetRepository categorySetRepository;
    private final MediaRepository mediaRepository;
    private final UserRepository userRepository;
    @Autowired
    ProfessionRepository professionRepository;
    private ConcurrentHashMap<String, Answer> currentCreatedAnswers;
    private ConcurrentHashMap<String, User> currentCreatedUser;
    private ConcurrentHashMap<Integer, Media> currentCreatedMedia;
    private ConcurrentHashMap<String, CategorySet> currentCreatedCategorySets;
    private ConcurrentHashMap<String, Profession> currentCreatedProfessions;
    private ConcurrentHashMap<String, Question> currentCreatedQuestions;
    private ExecutorService executor = Executors.newFixedThreadPool(8);
    List<Question> newQuestions = new ArrayList<>();
    List<Long> timePerQuestion = new ArrayList<>();

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
            this.writeToFile(timePerQuestion);
            return persistedQuestions;
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List parseCsv(InputStream is) {

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {



            currentCreatedAnswers = this.mapFromList(answerRepository.findAll());
            currentCreatedCategorySets = this.mapFromListCategorySet(categorySetRepository.findAll());
            currentCreatedMedia = this.mapFromListMedia(mediaRepository.findAll());
            currentCreatedUser = this.mapFromListUser(userRepository.findAll());
            currentCreatedProfessions = this.mapFromListProfession(professionRepository.findAll());
            currentCreatedQuestions = this.mapFromListQuestion(questionRepository.findAll());

            List <CSVRecord> csvRecords = csvParser.getRecords();



            for(CSVRecord csvRecord : csvRecords) {
                Instant start = Instant.now();
                Future<Question> questionFuture = executor.submit(() -> createQuestionsFromCSV(csvRecord));
                while (!questionFuture.isDone()) {
                }
                if(questionFuture.get() != null) {
                    //newQuestions.add(questionFuture.get());
                    Instant finish = Instant.now();
                    currentCreatedQuestions.put(questionFuture.get().getQuestionPhrase(), questionFuture.get());
                    this.timePerQuestion.add(Duration.between(start, finish).toMillis());
                }

                LOG.warn("question finished" + currentCreatedQuestions.size());


            }

            return new ArrayList<>(currentCreatedQuestions.values());

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

    private ConcurrentHashMap<String, Profession> mapFromListProfession(List<Profession> answerList){
        ConcurrentHashMap<String,Profession> map = new ConcurrentHashMap<>();
        for (Profession i : answerList) map.put(i.getName(),i);
        return map;
    }
    private ConcurrentHashMap<String, Question> mapFromListQuestion(List<Question> answerList){
        ConcurrentHashMap<String,Question> map = new ConcurrentHashMap<>();
        for (Question i : answerList) map.put(i.getQuestionPhrase(),i);
        return map;
    }
    private Question insertNewProfession(Question question, String profession){
        if(profession != "null"){
            if(currentCreatedProfessions.containsKey(profession)) {
                question.getProfessions().add(currentCreatedProfessions.get(profession));
            }
        }
        return question;
    }

    private QuestionLevel getQuestionLevel(String level){

        switch (level){
            case "1":
                return QuestionLevel.LEVEL_1;
            case "2":
                return QuestionLevel.LEVEL_2;
            case "3":
                return QuestionLevel.LEVEL_3;
            default:
                return QuestionLevel.NO_LEVEL;
        }
    }

    private Question createQuestionsFromCSV(CSVRecord csvRecord) {
        if (currentCreatedQuestions.containsKey(this.escapeAndEncodeString(csvRecord.get("QuestionPhrase")))) {
            Question question = this.insertNewProfession(currentCreatedQuestions.get(this.escapeAndEncodeString(csvRecord.get("QuestionPhrase"))), csvRecord.get("profession"));
            currentCreatedQuestions.replace(this.escapeAndEncodeString(csvRecord.get("QuestionPhrase")), question);
            return null;
        } else {
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
            QuestionLevel questionLevel;
            List<Profession> profession = new ArrayList<>();
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
            if (currentCreatedProfessions.containsKey(csvRecord.get("profession"))) {
                if (csvRecord.get("profession") != "null") {
                    profession.add(currentCreatedProfessions.get(csvRecord.get("profession")));
                }
            }
            questionLevel = this.getQuestionLevel(csvRecord.get("level"));

            Question newQuestion = new Question(
                    this.escapeAndEncodeString(csvRecord.get("QuestionPhrase")),
                    possibleAnswers,
                    correctAnswers,
                    questionType,
                    user,
                    categorySet,
                    questionImage,
                    solutionImage,
                    pointsToAchieve,
                    profession,
                    questionLevel

            );
            return newQuestion;
        }
    }

    private void writeToFile(List<Long> list) throws IOException {
        FileWriter writer = new FileWriter("output_2.txt");
        for(Long str: list) {
            writer.write(str + System.lineSeparator());
        }
        writer.close();
    }

}
