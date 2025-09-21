package com.example.quiz_service.service;


import com.example.quiz_service.feign.QuizInterface;

import com.example.quiz_service.model.QuestionWrapper;
import com.example.quiz_service.model.Quiz;
import com.example.quiz_service.model.Response;
import com.example.quiz_service.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



import java.util.List;


@Service
public class QuizService {
    @Autowired
    QuizRepository quizRepository;


    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
            List<Integer> questions = quizInterface.getQuestionsForQuiz(category,numQ).getBody();
            Quiz quiz = new Quiz();
            quiz.setTitle(title);
            quiz.setQuestionIds(questions);
            quizRepository.save(quiz);

    return new ResponseEntity<>("success",HttpStatus.CREATED);
    }



    public List<QuestionWrapper> getQuizQuestions(Integer id) {

        Quiz quiz = quizRepository.findById(id).get();

        List<Integer> questionIds = quiz.getQuestionIds();

        ResponseEntity<List<QuestionWrapper>> questions = quizInterface.getQuestionsFromId(questionIds);

        return questions.getBody();


    }


    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {

        ResponseEntity<Integer> score = quizInterface.getScore(responses);
        return score;
        }

}
