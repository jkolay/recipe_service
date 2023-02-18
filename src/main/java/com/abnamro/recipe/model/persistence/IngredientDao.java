package com.abnamro.recipe.model.persistence;

import com.abnamro.recipe.model.constant.DatabaseConstant;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@DynamicUpdate
@Table(name = "ingredients")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "incrementDomain")
    @GenericGenerator(name = "incrementDomain", strategy = "increment")
    private Integer id;

    @NotBlank
    @Column(nullable = false)
    private String ingredient;

    @ManyToMany(mappedBy = DatabaseConstant.JOINED_TABLE_NAME, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnoreProperties(DatabaseConstant.JOINED_TABLE_NAME)
    private Set<RecipeDao> recipeDaoIngredients;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column
    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
