package com.knowledgebase.controller;

import com.knowledgebase.model.Answer;
import com.knowledgebase.model.Category;
import com.knowledgebase.model.Question;
import com.knowledgebase.model.User;
import com.knowledgebase.repository.AnswerRepository;
import com.knowledgebase.repository.CategoryRepository;
import com.knowledgebase.repository.QuestionRepository;
import com.knowledgebase.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class WebController {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public WebController(QuestionRepository questionRepository,
                        AnswerRepository answerRepository,
                        CategoryRepository categoryRepository,
                        UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String home(Model model, @RequestParam(value = "q", required = false) String q) {
        // Load a few categories for featured section
        List<Category> featuredCategories = categoryRepository.findAll();
        if (featuredCategories.size() > 6) {
            featuredCategories = featuredCategories.subList(0, 6);
        }

        // Add stats
        model.addAttribute("totalQuestions", questionRepository.count());
        model.addAttribute("totalCategories", categoryRepository.count());
        model.addAttribute("totalUsers", userRepository.count());

        model.addAttribute("categories", featuredCategories);
        model.addAttribute("searchQuery", q);
        return "home";
    }

    @GetMapping("/questions")
    public String listQuestions(Model model, @RequestParam(value = "q", required = false) String q,
                               @RequestParam(value = "category", required = false) Long categoryId) {
        List<Question> questions;
        if (categoryId != null) {
            questions = questionRepository.findByCategory_CategoryId(categoryId);
        } else if (q != null && !q.isBlank()) {
            questions = questionRepository.findByTitleContainingIgnoreCase(q);
        } else {
            questions = questionRepository.findAll();
        }
        model.addAttribute("questions", questions);
        model.addAttribute("searchQuery", q);
        model.addAttribute("selectedCategory", categoryId);
        return "questions";
    }

    @GetMapping("/questions/{id:[0-9]+}")
    public String questionDetail(@PathVariable("id") Long id, Model model) {
        Question question = questionRepository.findById(id).orElse(null);
        if (question == null) {
            return "redirect:/";
        }
        List<Answer> answers = answerRepository.findByQuestion_QuestionId(id);
        model.addAttribute("question", question);
        model.addAttribute("answers", answers);
        model.addAttribute("newAnswer", new Answer());
        // Removed users list - will be handled by authentication
        return "question-detail";
    }

    @GetMapping("/questions/add")
    public String addQuestionForm(Model model) {
        model.addAttribute("question", new Question());
        model.addAttribute("categories", categoryRepository.findAll());
        // Removed users list - will be handled by authentication
        return "add-question";
    }

    @PostMapping("/questions")
    public String saveQuestion(@ModelAttribute Question question) {
        // The user is now selected from the form
        question.setCreatedAt(LocalDateTime.now());
        question.setUpdatedAt(LocalDateTime.now());
        questionRepository.save(question);
        return "redirect:/questions/" + question.getQuestionId();
    }

    @PostMapping("/questions/{questionId:[0-9]+}/answers")
    public String saveAnswer(@PathVariable("questionId") Long questionId, @ModelAttribute Answer answer) {
        Question question = questionRepository.findById(questionId).orElse(null);
        if (question != null) {
            answer.setQuestion(question);
            answer.setCreatedAt(LocalDateTime.now());
            answer.setUpdatedAt(LocalDateTime.now());
            answerRepository.save(answer);
        }
        return "redirect:/questions/" + questionId;
    }

    @GetMapping("/categories")
    public String listCategories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "categories";
    }

    @GetMapping("/register")
    public String registerUserForm(Model model) {
        model.addAttribute("user", new User());
        return "register-user";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        user.setJoinDate(LocalDateTime.now());
        userRepository.save(user);
        return "redirect:/users";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }
}
