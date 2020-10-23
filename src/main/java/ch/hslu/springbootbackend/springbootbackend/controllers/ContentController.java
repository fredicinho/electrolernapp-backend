package ch.hslu.springbootbackend.springbootbackend.controllers;

import ch.hslu.springbootbackend.springbootbackend.Entity.MainText;
import ch.hslu.springbootbackend.springbootbackend.Repository.MainTextRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/content")
public class ContentController {

    @Autowired
    MainTextRepository mainTextRepository;

    @GetMapping("/mainText/{id}")
    public MainText getMainText(@PathVariable(value = "id") Integer mainTextId){
        return mainTextRepository.getOne(mainTextId);
    }

    @PostMapping("/mainText")
    public MainText addMainText(@RequestBody MainText mainText){
        return mainTextRepository.save(mainText);
    }
}
