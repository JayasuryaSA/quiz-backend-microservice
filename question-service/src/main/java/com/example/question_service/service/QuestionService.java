package com.example.question_service.service;


import com.example.question_service.model.Question;
import com.example.question_service.model.QuestionWrapper;
import com.example.question_service.model.Response;
import com.example.question_service.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    //get all questions
    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    // Get questions by category
    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionRepository.findByCategory(category), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    // Add a new question
    public ResponseEntity<String> addQuestion(Question question) {
        Question out = questionRepository.save(question);
        return new ResponseEntity<>("Question added successfully with id: " + out.getId(),
                HttpStatus.CREATED);
    }


    // Update an existing question by ID
    public ResponseEntity<String> updateQuestion(Integer id, Question updatedQuestion) {
        try {
            Question existingQuestion = questionRepository.findById(id).orElse(null);
            if (existingQuestion != null) {
                // Update question details
                existingQuestion.setQuestionTitle(updatedQuestion.getQuestionTitle());
                existingQuestion.setOption1(updatedQuestion.getOption1());
                existingQuestion.setOption2(updatedQuestion.getOption2());
                existingQuestion.setOption3(updatedQuestion.getOption3());
                existingQuestion.setOption4(updatedQuestion.getOption4());
                existingQuestion.setCategory(updatedQuestion.getCategory());
                existingQuestion.setRightAnswer(updatedQuestion.getRightAnswer());
                existingQuestion.setDifficultyLevel(updatedQuestion.getDifficultyLevel());

                questionRepository.save(existingQuestion);
                return new ResponseEntity<>("Question updated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Question not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
    }

    // Delete a question by name
    public ResponseEntity<String> deleteQuestionByName(String name) {
        try {
            List<Question> questions = questionRepository.findByQuestionTitle(name);
            if (!questions.isEmpty()) {
                questionRepository.deleteAll(questions);
                return new ResponseEntity<>("Questions with name '" + name + "' deleted successfully",
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Question with name '" + name + "' not found",
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
    }

    // Delete a question by ID
    public ResponseEntity<String> deleteQuestionById(Integer id) {
        try {
            if (questionRepository.existsById(id)) {
                questionRepository.deleteById(id);
                return new ResponseEntity<>("Question deleted successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Question with ID " + id + " not found",
                        HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQuestions) {
        List<Integer> questions = questionRepository.findRandomQuestionsByCategory(categoryName, numQuestions);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }



    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        List<QuestionWrapper> wrappers = new ArrayList<>();
        List<Question> questions = new ArrayList<>();

        // Fetch each question by its ID
        for(Integer id : questionIds) {
            questions.add(questionRepository.findById(id).get());
        }


        for(Question question : questions) {
            QuestionWrapper wrapper = new QuestionWrapper();
            wrapper.setId(question.getId());
            wrapper.setQuestionTitle(question.getQuestionTitle());
            wrapper.setOption1(question.getOption1());
            wrapper.setOption2(question.getOption2());
            wrapper.setOption3(question.getOption3());
            wrapper.setOption4(question.getOption4());
            wrappers.add(wrapper);
        }

        return new ResponseEntity<>(wrappers, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {
        int right = 0;

        for(Response response : responses) {
            Question question = questionRepository.findById(response.getId()).get();
            if(response.getResponse().equals(question.getRightAnswer()))
                right++;
        }

        return new ResponseEntity<>(right, HttpStatus.OK);
    }

}




