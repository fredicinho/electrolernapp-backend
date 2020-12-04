package ch.hslu.springbootbackend.springbootbackend.Service.CsvService;

import ch.hslu.springbootbackend.springbootbackend.DTO.UserDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.*;
import ch.hslu.springbootbackend.springbootbackend.Repository.ProfessionRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.RoleRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.SchoolClassRepository;
import ch.hslu.springbootbackend.springbootbackend.Repository.UserRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

@Service
public class CsvUserService implements CsvService{

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    ProfessionRepository professionRepository;
    @Autowired
    SchoolClassRepository schoolClassRepository;

    private final Logger LOG = LoggerFactory.getLogger(CsvUserService.class);
    private ConcurrentHashMap<String, User> currentCreatedUser;
    private ConcurrentHashMap<String, Profession> currentCreatedProfessions;
    private ConcurrentHashMap<String, Role> currentCreatedRoles;
    private ExecutorService executor = Executors.newFixedThreadPool(8);
    private ArrayList<User> userToCreate;

    @Override
    public List saveNewEntities(MultipartFile file) {
        try {
            Instant start = Instant.now();
            List<User> users = parseCsv(file.getInputStream());
            Instant finish = Instant.now();
            List<User> persistedUsers = userRepository.saveAll(users);
            LOG.info("Imported " + persistedUsers.size() + " in " + Duration.between(start, finish).toMillis());
            return persistedUsers;
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    @Override
    public List parseCsv(InputStream inputStream) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            currentCreatedUser = this.mapFromListUser(userRepository.findAll());
            currentCreatedProfessions = this.mapFromListProfession(professionRepository.findAll());
            currentCreatedRoles = this.mapFromListRole(roleRepository.findAll());
            userToCreate = new ArrayList<>();

            List <CSVRecord> csvRecords = csvParser.getRecords();



            for(CSVRecord csvRecord : csvRecords) {
                Instant start = Instant.now();
                Future<User> userFuture = executor.submit(() -> createUserFromCSV(csvRecord));
                while (!userFuture.isDone()) {
                }
                if(userFuture.get() != null) {
                    //newQuestions.add(questionFuture.get());
                    currentCreatedUser.put(userFuture.get().getUsername(), userFuture.get());
                    userToCreate.add(userFuture.get());
                }

                LOG.warn("user finished" + currentCreatedUser.size());


            }

            return userToCreate;

        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    private User createUserFromCSV(CSVRecord csvRecord){
        String username = csvRecord.get("username");
        String email = csvRecord.get("email");
        String password = csvRecord.get("password");
        String profession = csvRecord.get("profession");
        String role = csvRecord.get("role");
        Set<Role> roleSet = new HashSet<>();
        List<SchoolClass> schoolClasses = this.getSchoolClasses(Integer.valueOf(csvRecord.get("schoolClassId")));
        if(currentCreatedUser.containsKey(username)){
            return null;
        }
        roleSet.add(this.getRole(role));


        if(username.equals("null")){
            return null;
        }else {
            return new User(username, email, password, currentCreatedProfessions.get(profession), roleSet, schoolClasses);
        }
    }
    private ConcurrentHashMap<String, User> mapFromListUser(List<User> answerList){
        ConcurrentHashMap<String,User> map = new ConcurrentHashMap<>();
        for (User i : answerList) map.put(i.getUsername(),i);
        return map;
    }

    private ConcurrentHashMap<String, Role> mapFromListRole(List<Role> roleList){
        ConcurrentHashMap<String,Role> map = new ConcurrentHashMap<>();
        for (Role i : roleList) map.put(i.getName().toString(),i);
        return map;
    }
    private ConcurrentHashMap<String, Profession> mapFromListProfession(List<Profession> answerList){
        ConcurrentHashMap<String,Profession> map = new ConcurrentHashMap<>();
        for (Profession i : answerList) map.put(i.getName(),i);
        return map;
    }

    private Role getRole(String role){
        switch (role) {
            case "ROLE_ADMIN":
                return currentCreatedRoles.get(ERole.ROLE_ADMIN.toString());
            case "ROLE_EXAM":
                return currentCreatedRoles.get(ERole.ROLE_EXAM.toString());
            case "ROLE_TEACHER":
                return currentCreatedRoles.get(ERole.ROLE_TEACHER.toString());
            default:
                return currentCreatedRoles.get(ERole.ROLE_USER.toString());
        }
    }

    public List<UserDTO> getAllAsDTO(){
        List<User> listUser = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();
        for(User user : listUser){
            UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail(), this.getRoles(user.getRoles()), this.getExamSets(user.getInSchoolClasses()));
            if(user.getProfession() != null){
                userDTO.setProfession(user.getProfession().getName());
                userDTOList.add(userDTO);
            }else{
                userDTO.setProfession(null);
                userDTOList.add(userDTO);
            }

        }
        return userDTOList;
    }
    public List<User> getAll(){
        return userRepository.findAll();
    }

    public List<String> getRoles(Set<Role> roles) {
        List<String> stringList = new ArrayList<>();
        for(Role role : roles){
            stringList.add(role.getName().toString());
        }return stringList;

    }
    public List<Integer> getExamSets(List<SchoolClass> schoolClasses) {
        List<Integer> stringList = new ArrayList<>();
        for(SchoolClass schoolClass : schoolClasses){
            stringList.add(schoolClass.getId());
        }return stringList;
    }

    private List<SchoolClass> getSchoolClasses(int id){
        List<SchoolClass> schoolClasses = new ArrayList<>();
        Optional<SchoolClass> schoolClassOptional = schoolClassRepository.findById(id);
        if(schoolClassOptional.isPresent()){
            schoolClasses.add(schoolClassOptional.get());
        }
        return schoolClasses;
    }

}
