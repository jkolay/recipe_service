package com.abnamro.recipe.repositories;

import com.abnamro.recipe.model.persistence.AuthorityDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for Authorities
 */
@Repository
public interface AuthorityRepository extends JpaRepository<AuthorityDao,Integer> {
}
