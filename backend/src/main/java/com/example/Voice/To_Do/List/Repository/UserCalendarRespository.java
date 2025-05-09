package com.example.Voice.To_Do.List.Repository;

import com.example.Voice.To_Do.List.Model.UserCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCalendarRespository extends JpaRepository<UserCalendar , Long> {

    Optional<UserCalendar> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
    void deleteByUserId(Long userId);

}
