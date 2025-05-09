package com.example.Voice.To_Do.List.DAO;

import com.example.Voice.To_Do.List.Model.Task;
import com.example.Voice.To_Do.List.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
    List<Task> findByUserId(Long userId);

//    Optional<List<Task>> findAllByUserId(Long userId);
//    boolean existsByUserId(Long userId);

}
