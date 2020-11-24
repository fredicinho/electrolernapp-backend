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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/statistics")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER') or hasRole('ROLE_USER') ")
public class StatisticController {
    private final Logger LOG = LoggerFactory.getLogger(StatisticController.class);


    @Autowired
    private StatisticService statisticService;

    @GetMapping("/User/{id}")
    public List<StatisticDTO> getStatisticByUserId(@PathVariable(value = "id") long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            return statisticService.getByUsername(auth.getName());
        }else{
            return statisticService.getByUserId(userId);
        }
    }

    @GetMapping("/UserAndQuestion/")
    public List<StatisticDTO> getStatisticByUserAndQuestion(@RequestParam Integer userId
                                                        , @RequestParam Integer questionId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            return statisticService.getByUsernameAndQuestion(auth.getName(), questionId);
        }else {
            return statisticService.getByUserAndQuestion(userId, questionId);
        }
    }

    @GetMapping("/Question/{id}")
    public List<StatisticDTO> getStatisticByQuestionId(@PathVariable(value = "id") Integer questionId) throws ResourceNotFoundException {
            return statisticService.getByQuestionId(questionId);

    }

    @PostMapping("")
    public ResponseEntity<StatisticDTO> addStatistic(@RequestBody StatisticDTO newStatistic) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            newStatistic.setUsername(auth.getName());
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
    @PreAuthorize("hasRole('ROLE_TEACHER') or hasRole('ROLE_ADMIN')")
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
