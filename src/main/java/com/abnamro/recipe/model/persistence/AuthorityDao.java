package com.abnamro.recipe.model.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

/**
 * Dao class for Authority
 */
@Entity
@Table(name = "authorities")
@Getter
@Setter
public class AuthorityDao {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
    @GenericGenerator(name = "native",strategy = "native")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDao userDao;


}
