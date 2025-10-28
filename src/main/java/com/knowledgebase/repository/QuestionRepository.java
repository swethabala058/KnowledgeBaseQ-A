package com.knowledgebase.repository;

import com.knowledgebase.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTitleContainingIgnoreCase(String keyword);
    List<Question> findByCategory_CategoryId(Long categoryId);
    List<Question> findByUser_UserId(Long userId);
}
