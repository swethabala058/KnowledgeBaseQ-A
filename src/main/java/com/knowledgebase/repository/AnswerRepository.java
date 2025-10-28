package com.knowledgebase.repository;

import com.knowledgebase.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestion_QuestionId(Long questionId);
    List<Answer> findByUser_UserId(Long userId);
}
