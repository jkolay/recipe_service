package com.abnamro.recipe.controllers;


import com.abnamro.recipe.model.request.CreateRecipeRequest;
import com.abnamro.recipe.model.request.RecipeSearchRequest;
import com.abnamro.recipe.model.request.UpdateRecipeRequest;
import com.abnamro.recipe.model.response.RecipeResponse;
import com.abnamro.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * This is the controller/api class for recipe operations
 */
@RestController
@RequestMapping(value = "api/v1/recipe")
public class RecipeController {
    private final Logger logger = LoggerFactory.getLogger(RecipeController.class);

    private final RecipeService recipeService;

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * This is the end point to retrieve all recipe based on page number and size of records numbers
     *
     * @param page the page number which needs to be fetched
     * @param size the number of records needs to be in a single page
     * @return the list of records
     */
    @Operation(description = "List all recipes")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful request"),})
    @RequestMapping(method = RequestMethod.GET, path = "/page/{page}/size/{size}")
    public List<RecipeResponse> getRecipeList(@PathVariable(name = "page") int page, @PathVariable(name = "size") int size) {
        logger.info("Getting the recipes");
        return recipeService.getRecipeList(page, size);

    }

    /**
     * this api retrieves a recipe
     *
     * @param id id of the recipe which needs to be retrieved
     * @return the recipe
     */
    @Operation(description = "List one recipe by its ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful request"), @ApiResponse(responseCode = "404", description = "Recipe not found by the given ID")})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public RecipeResponse getRecipe(@Parameter(description = "Recipe ID", required = true) @PathVariable(name = "id") Integer id) {
        logger.info("Getting the recipe by its id. Id: {}", id);
        return recipeService.getRecipeById(id);
    }

    /**
     * this api creates a recipe in the system
     *
     * @param request the recipe request object
     * @return the response after creation of the recipe
     */
    @Operation(description = "Create a recipe")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Recipe created"), @ApiResponse(responseCode = "400", description = "Bad input")})
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeResponse createRecipe(@Parameter(description = "Properties of the recipe", required = true) @Valid @RequestBody CreateRecipeRequest request) {
        logger.info("Creating the recipe with properties");
        return recipeService.createRecipe(request);
    }

    /**
     * this api modifies the recipe
     *
     * @param updateRecipeRequest the recipe update request object
     */
    @Operation(description = "Update the recipe")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Ingredient created"), @ApiResponse(responseCode = "400", description = "Bad input")})
    @RequestMapping(method = RequestMethod.PUT)
    public void updateRecipe(@Parameter(description = "Properties of the recipe", required = true) @Valid @RequestBody UpdateRecipeRequest updateRecipeRequest) {
        logger.info("Updating the recipe by given properties");
        recipeService.updateRecipe(updateRecipeRequest);
    }

    /**
     * This api deletes the recipe from the application
     *
     * @param id the id of the recipe which needs to be deleted
     */

    @Operation(description = "Delete the recipe")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful operation"), @ApiResponse(responseCode = "400", description = "Invalid input"), @ApiResponse(responseCode = "404", description = "Recipe not found by the given ID")})
    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteRecipe(@Parameter(description = "Recipe ID", required = true) @NotNull(message = "{id.notNull}") @RequestParam(name = "id") Integer id) {
        logger.info("Deleting the recipe by its id. Id: {}", id);
        recipeService.deleteRecipe(id);
    }

    /**
     * This method is the api to search recipes by given parameters
     *
     * @param page                the page number
     * @param size                the number of recipes in a page
     * @param recipeSearchRequest the search request object
     * @return the list of recipe response
     */

    @Operation(description = "Search recipes by given parameters")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successful request"), @ApiResponse(responseCode = "404", description = "Different error messages related to criteria and recipe")

    })
    @RequestMapping(method = RequestMethod.POST, path = "/search")
    public List<RecipeResponse> searchRecipe(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size, @Parameter(description = "Properties of the the search") @RequestBody @Valid RecipeSearchRequest recipeSearchRequest) {
        logger.info("Searching the recipe by given criteria");
        return recipeService.findBySearchCriteria(recipeSearchRequest, page, size);
    }
}
