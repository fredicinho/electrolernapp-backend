package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.DTO.StatisticDTO;
import ch.hslu.springbootbackend.springbootbackend.Exception.ResourceNotFoundException;
import ch.hslu.springbootbackend.springbootbackend.Service.EntityService.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/statistics")
public class StatisticController {
    private final Logger LOG = LoggerFactory.getLogger(StatisticController.class);


    @Autowired
    private StatisticService statisticService;

    @GetMapping("/User/{id}")
    //@PreAuthorize("hasRole('ROLE_USER')")
    public List<StatisticDTO> getStatisticByUserid(@PathVariable(value = "id") long userId) throws ResourceNotFoundException {
            return statisticService.getByUserId(userId);
    }

    @GetMapping("/UserAndQuestion/")
    //@PreAuthorize("hasRole('ROLE_USER')")
    public List<StatisticDTO> getStatisticByUserAndQuestion(@RequestParam Integer userId
                                                        , @RequestParam Integer questionId) throws ResourceNotFoundException {
        return statisticService.getByUserAndQuestion(userId, questionId);

    }

    @GetMapping("/Question/{id}")
    //will be only available for teacher
    //@PreAuthorize("hasRole('ROLE_USER')")
    public List<StatisticDTO> getStatisticByQuestionId(@PathVariable(value = "id") Integer questionId) throws ResourceNotFoundException {
            return statisticService.getByQuestionId(questionId);

    }

    @PostMapping("")
    //@PreAuthorize("")
    public ResponseEntity<StatisticDTO> addStatistic(@RequestBody StatisticDTO newStatistic) {

            StatisticDTO statisticDTO = statisticService.addNewStatistic(newStatistic);
            if(statisticDTO != null) {
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(statisticDTO);
            }else{
                return ResponseEntity.
                        notFound()
                        .build();
            }


    }

    @PostMapping("/statistics")
    //@PreAuthorize("")
    public ResponseEntity<List<StatisticDTO>> addStatisticArray(@RequestBody List<StatisticDTO> newStatistics) {

        List<StatisticDTO> statisticDTOs = statisticService.addNewStatistics(newStatistics);
        if(statisticDTOs.size() != 0) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(statisticDTOs);
        }else{
            return ResponseEntity.
                    notFound()
                    .build();
        }


    }
}
