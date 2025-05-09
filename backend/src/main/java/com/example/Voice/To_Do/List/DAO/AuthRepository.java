package com.example.Voice.To_Do.List.DAO;

import com.example.Voice.To_Do.List.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<User, Long> {
}
