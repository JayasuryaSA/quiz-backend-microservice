package com.example.quiz_service.controller;


import com.example.quiz_service.model.QuestionWrapper;
import com.example.quiz_service.model.QuizDto;
import com.example.quiz_service.model.Response;
import com.example.quiz_service.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("quiz")
public class QuizController {

    @Autowired
    QuizService quizService;

    @PostMapping("create")
    public ResponseEntity<String> createQuiz(@RequestBody QuizDto quizDto) {
        return quizService.createQuiz(quizDto.getCategoryName(),quizDto.getNumQuestions(), quizDto.getTitle());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(@PathVariable Integer id) {
        List<QuestionWrapper> questionsForUser = quizService.getQuizQuestions(id);
        return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
    }

    @PostMapping("submit/{id}")
    public ResponseEntity<Integer> submitQuiz(@PathVariable Integer id, @RequestBody
    List<Response> response) {
        return quizService.calculateResult(id, response);
    }

}
