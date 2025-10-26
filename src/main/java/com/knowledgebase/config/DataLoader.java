package com.knowledgebase.config;

import com.knowledgebase.model.*;
import com.knowledgebase.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public DataLoader(UserRepository userRepository,
                     CategoryRepository categoryRepository,
                     QuestionRepository questionRepository,
                     AnswerRepository answerRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Only load data if tables are empty
        if (userRepository.count() == 0) {
            loadSampleData();
        }
    }

    private void loadSampleData() {
        // Create sample users
        User user1 = new User();
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");
        user1.setRole("USER");
        user1.setJoinDate(LocalDateTime.now());
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("Jane Smith");
        user2.setEmail("jane.smith@example.com");
        user2.setRole("USER");
        user2.setJoinDate(LocalDateTime.now());
        userRepository.save(user2);

        User user3 = new User();
        user3.setName("Admin User");
        user3.setEmail("admin@example.com");
        user3.setRole("ADMIN");
        user3.setJoinDate(LocalDateTime.now());
        userRepository.save(user3);

        // Create sample categories
        Category category1 = new Category();
        category1.setName("General");
        category1.setDescription("General questions and discussions");
        categoryRepository.save(category1);

        Category category2 = new Category();
        category2.setName("Technology");
        category2.setDescription("Technology-related questions");
        categoryRepository.save(category2);

        Category category3 = new Category();
        category3.setName("Programming");
        category3.setDescription("Programming and development questions");
        categoryRepository.save(category3);

        Category category4 = new Category();
        category4.setName("Database");
        category4.setDescription("Database management and SQL questions");
        categoryRepository.save(category4);

        // Create sample questions
        Question question1 = new Question();
        question1.setTitle("How to get started with Spring Boot?");
        question1.setContent("I'm new to Spring Boot and want to know the best practices for setting up a new project.");
        question1.setUser(user1);
        question1.setCategory(category3);
        question1.setCreatedAt(LocalDateTime.now());
        question1.setUpdatedAt(LocalDateTime.now());
        questionRepository.save(question1);

        Question question2 = new Question();
        question2.setTitle("What is the difference between MySQL and PostgreSQL?");
        question2.setContent("Can someone explain the main differences between MySQL and PostgreSQL databases?");
        question2.setUser(user2);
        question2.setCategory(category4);
        question2.setCreatedAt(LocalDateTime.now());
        question2.setUpdatedAt(LocalDateTime.now());
        questionRepository.save(question2);

        Question question3 = new Question();
        question3.setTitle("How to optimize database queries?");
        question3.setContent("I have slow queries in my application. What are some techniques to optimize them?");
        question3.setUser(user1);
        question3.setCategory(category4);
        question3.setCreatedAt(LocalDateTime.now());
        question3.setUpdatedAt(LocalDateTime.now());
        questionRepository.save(question3);

        Question question4 = new Question();
        question4.setTitle("Best practices for REST API design");
        question4.setContent("What are the best practices for designing RESTful APIs?");
        question4.setUser(user3);
        question4.setCategory(category2);
        question4.setCreatedAt(LocalDateTime.now());
        question4.setUpdatedAt(LocalDateTime.now());
        questionRepository.save(question4);

        // Create sample answers
        Answer answer1 = new Answer();
        answer1.setContent("Spring Boot makes it easy to create stand-alone, production-grade Spring based Applications. Start by using Spring Initializr to generate a basic project structure.");
        answer1.setUser(user2);
        answer1.setQuestion(question1);
        answer1.setCreatedAt(LocalDateTime.now());
        answer1.setUpdatedAt(LocalDateTime.now());
        answerRepository.save(answer1);

        Answer answer2 = new Answer();
        answer2.setContent("MySQL is more widely used and has better performance for read-heavy applications, while PostgreSQL offers more advanced features and better support for complex queries.");
        answer2.setUser(user1);
        answer2.setQuestion(question2);
        answer2.setCreatedAt(LocalDateTime.now());
        answer2.setUpdatedAt(LocalDateTime.now());
        answerRepository.save(answer2);

        Answer answer3 = new Answer();
        answer3.setContent("Use EXPLAIN to analyze query execution plans, add appropriate indexes, avoid SELECT *, and consider query optimization techniques like pagination.");
        answer3.setUser(user3);
        answer3.setQuestion(question3);
        answer3.setCreatedAt(LocalDateTime.now());
        answer3.setUpdatedAt(LocalDateTime.now());
        answerRepository.save(answer3);

        Answer answer4 = new Answer();
        answer4.setContent("Use proper HTTP methods (GET, POST, PUT, DELETE), meaningful resource names, consistent URL structure, and proper status codes.");
        answer4.setUser(user1);
        answer4.setQuestion(question4);
        answer4.setCreatedAt(LocalDateTime.now());
        answer4.setUpdatedAt(LocalDateTime.now());
        answerRepository.save(answer4);

        System.out.println("Sample data loaded successfully!");
    }
}
