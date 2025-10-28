package com.knowledgebase.controller;

import com.knowledgebase.model.*;
import com.knowledgebase.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ApiController(QuestionRepository questionRepository,
                        AnswerRepository answerRepository,
                        CategoryRepository categoryRepository,
                        UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    // Users endpoints
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        user.setJoinDate(LocalDateTime.now());
        User saved = userRepository.save(user);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/users")
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    // Categories endpoints
    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category saved = categoryRepository.save(category);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/categories")
    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    // Questions endpoints
    @PostMapping("/questions")
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        question.setCreatedAt(LocalDateTime.now());
        question.setUpdatedAt(LocalDateTime.now());
        Question saved = questionRepository.save(question);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/questions")
    public List<Question> listQuestions(@RequestParam(value = "q", required = false) String q) {
        if (q == null || q.isBlank()) {
            return questionRepository.findAll();
        }
        return questionRepository.findByTitleContainingIgnoreCase(q);
    }

    @GetMapping("/questions/{id}")
    public ResponseEntity<Question> getQuestion(@PathVariable Long id) {
        Optional<Question> question = questionRepository.findById(id);
        return question.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/questions/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody Question questionDetails) {
        Optional<Question> optionalQuestion = questionRepository.findById(id);
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            question.setTitle(questionDetails.getTitle());
            question.setContent(questionDetails.getContent());
            question.setCategory(questionDetails.getCategory());
            question.setUser(questionDetails.getUser());
            question.setUpdatedAt(LocalDateTime.now());
            Question updated = questionRepository.save(question);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Answers endpoints
    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<Answer> createAnswer(@PathVariable Long questionId, @RequestBody Answer answer) {
        Optional<Question> question = questionRepository.findById(questionId);
        if (question.isPresent()) {
            answer.setQuestion(question.get());
            answer.setCreatedAt(LocalDateTime.now());
            answer.setUpdatedAt(LocalDateTime.now());
            Answer saved = answerRepository.save(answer);
            return ResponseEntity.ok(saved);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/questions/{questionId}/answers")
    public ResponseEntity<List<Answer>> getAnswersForQuestion(@PathVariable Long questionId) {
        if (questionRepository.existsById(questionId)) {
            List<Answer> answers = answerRepository.findByQuestion_QuestionId(questionId);
            return ResponseEntity.ok(answers);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/answers/{id}")
    public ResponseEntity<Answer> updateAnswer(@PathVariable Long id, @RequestBody Answer answerDetails) {
        Optional<Answer> optionalAnswer = answerRepository.findById(id);
        if (optionalAnswer.isPresent()) {
            Answer answer = optionalAnswer.get();
            answer.setContent(answerDetails.getContent());
            answer.setUser(answerDetails.getUser());
            answer.setUpdatedAt(LocalDateTime.now());
            Answer updated = answerRepository.save(answer);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/answers/{id}")
    public ResponseEntity<Void> deleteAnswer(@PathVariable Long id) {
        if (answerRepository.existsById(id)) {
            answerRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Search endpoint
    @GetMapping("/search")
    public List<Question> searchQuestions(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "categoryId", required = false) Long categoryId,
                                         @RequestParam(value = "userId", required = false) Long userId) {
        if (keyword != null && !keyword.isBlank()) {
            return questionRepository.findByTitleContainingIgnoreCase(keyword);
        } else if (categoryId != null) {
            return questionRepository.findByCategory_CategoryId(categoryId);
        } else if (userId != null) {
            return questionRepository.findByUser_UserId(userId);
        }
        return questionRepository.findAll();
    }

}
