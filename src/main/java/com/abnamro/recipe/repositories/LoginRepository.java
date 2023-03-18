package com.abnamro.recipe.repositories;


import com.abnamro.recipe.model.persistence.UserDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginRepository extends JpaRepository<UserDao, Long> {

  List<UserDao> findByEmail(String email);
}