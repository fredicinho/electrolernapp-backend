package ch.hslu.springbootbackend.springbootbackend.Service.EntityService;

import ch.hslu.springbootbackend.springbootbackend.DTO.ExamSetDTO;
import ch.hslu.springbootbackend.springbootbackend.Entity.Sets.ExamSet;
import ch.hslu.springbootbackend.springbootbackend.Repository.ExamSetRepository;
import ch.hslu.springbootbackend.springbootbackend.Strategy.DTOParserExamSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamSetService {

    @Autowired
    ExamSetRepository examSetRepository;
    @Autowired
    DTOParserExamSet dtoParserExamSet;


    private boolean ressourceExists = false;

    public ExamSetDTO createNewExamSet(ExamSetDTO examSetDTO) {
        Optional<ExamSet> examSetOptional = examSetRepository.findByTitle(examSetDTO.getTitle());
        if (examSetOptional.isPresent()) {
            ressourceExists = true;
            return dtoParserExamSet.generateDTOFromObject(examSetOptional.get().getExamSetId());
        }
        ExamSet examSet = dtoParserExamSet.generateObjectFromDTO(examSetDTO);
        return dtoParserExamSet.generateDTOFromObject(examSetRepository.save(examSet).getExamSetId());
    }

    public ExamSetDTO getExamSetById(int examSet){
        return dtoParserExamSet.generateDTOFromObject(examSetRepository.getOne(examSet).getExamSetId());
    }

    public List<ExamSetDTO> getAllExamSets(){
        return dtoParserExamSet.generateDTOsFromObjects(examSetRepository.findAll());
    }


    public boolean ressourceExists() {
        return ressourceExists;
    }

    public void setRessourceExists(boolean ressourceExists) {
        this.ressourceExists = ressourceExists;
    }
}
