package com.abnamro.recipe.controllers;


import com.abnamro.recipe.model.request.CreateRecipeRequest;
import com.abnamro.recipe.model.request.UpdateRecipeRequest;
import com.abnamro.recipe.model.response.RecipeResponse;
import com.abnamro.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    @RequestMapping(method = RequestMethod.GET, path = "search/page/{page}/size/{size}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<List<RecipeResponse>> getRecipeList(@PathVariable(name = "page") int page, @PathVariable(name = "size") int size) {
        logger.info("Getting the recipes");
        return ResponseEntity.status(HttpStatus.FOUND).body(recipeService.getRecipeList(page, size));

    }

    /**
     * This is the end point to retrieve all recipe based on page number and size of records numbers
     *
     * @param recipeTpe search recipes by
     * @return the list of records
     */
    @Operation(description = "List all recipes by recipe type")
    @RequestMapping(method = RequestMethod.GET, path = "search/type/{type}")
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<List<RecipeResponse>> getRecipeListByRecipeType(@PathVariable(name = "type") String recipeTpe ) {
        logger.info("Getting the recipes");
        return ResponseEntity.status(HttpStatus.FOUND).body(recipeService.getRecipeListByRecipeType(recipeTpe));

    }

    /**
     * this api retrieves a recipe
     *
     * @param id id of the recipe which needs to be retrieved
     * @return the recipe
     */
    @Operation(description = "List one recipe by its ID")
    @RequestMapping(value = "search/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<RecipeResponse> getRecipe(@Parameter(description = "Recipe ID", required = true) @PathVariable(name = "id") Integer id) {
        logger.info("Getting the recipe by its id. Id: {}", id);
        return ResponseEntity.status(HttpStatus.FOUND).body(recipeService.getRecipeById(id));
    }

    /**
     * this api retrieves list recipes based on serving
     *
     * @param id id of the recipe which needs to be retrieved
     * @return the recipe
     */
    @Operation(description = "List recipes by serving")
    @RequestMapping(value = "search/serving/{numberOfServing}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<List<RecipeResponse>> getRecipesByServing(@Parameter(description = "Serving number", required = true) @PathVariable(name = "numberOfServing") Integer numberOfServing) {
        logger.info("Getting the recipes by its serving {}", numberOfServing);
        return ResponseEntity.status(HttpStatus.FOUND).body(recipeService.getRecipesByServing(numberOfServing));
    }

    /**
     * this api creates a recipe in the system
     *
     * @param request the recipe request object
     * @return the response after creation of the recipe
     */
    @Operation(description = "Create a recipe")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RecipeResponse> createRecipe(@Parameter(description = "Properties of the recipe", required = true) @Valid @RequestBody CreateRecipeRequest request) {
        logger.info("Creating the recipe with properties");
        return ResponseEntity.status(HttpStatus.CREATED).body(recipeService.createRecipe(request));
    }

    /**
     * this api modifies the recipe
     *
     * @param updateRecipeRequest the recipe update request object
     */
    @Operation(description = "Update the recipe")
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RecipeResponse> updateRecipe(@Parameter(description = "Properties of the recipe", required = true) @Valid @RequestBody UpdateRecipeRequest updateRecipeRequest) {
        logger.info("Updating the recipe by given properties");
        return ResponseEntity.ok(recipeService.updateRecipe(updateRecipeRequest));
    }

    /**
     * This api deletes the recipe from the application
     *
     * @param id the id of the recipe which needs to be deleted
     */

    @Operation(description = "Delete the recipe")
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteRecipe(@Parameter(description = "Recipe ID", required = true) @NotNull(message = "{id.notNull}") @RequestParam(name = "id") Integer id) {
        logger.info("Deleting the recipe by its id. Id: {}", id);
        recipeService.deleteRecipe(id);
    }

}
