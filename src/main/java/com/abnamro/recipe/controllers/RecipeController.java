package com.abnamro.recipe.controllers;


import com.abnamro.recipe.model.request.RecipeSearchRequest;
import com.abnamro.recipe.model.request.CreateRecipeRequest;
import com.abnamro.recipe.model.request.UpdateRecipeRequest;
import com.abnamro.recipe.model.response.RecipeResponse;
import com.abnamro.recipe.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j(topic = "RecipeController")
public class RecipeController {

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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed the recipes Successfully")
    })
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<List<RecipeResponse>> getRecipeList(@PathVariable(name = "page") int page, @PathVariable(name = "size") int size) {
        log.info("Getting the recipes");
        return ResponseEntity.status(HttpStatus.FOUND).body(recipeService.getRecipeList(page, size));

    }



    /**
     * this api retrieves a recipe
     *
     * @param id id of the recipe which needs to be retrieved
     * @return the recipe
     */
    @Operation(description = "List one recipe by its ID")
    @RequestMapping(value = "search/{id}", method = RequestMethod.GET)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetched the recipe by the id Successfully"),
            @ApiResponse(responseCode = "404", description = "Recipe is not found")
    })
    @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<RecipeResponse> getRecipe(@Parameter(description = "Recipe ID", required = true) @PathVariable(name = "id") Integer id) {
        log.info("Getting the recipe by its id. Id: {}", id);
        return ResponseEntity.status(HttpStatus.FOUND).body(recipeService.getRecipeById(id));
    }



    /**
     * this api creates a recipe in the system
     *
     * @param request the recipe request object
     * @return the response after creation of the recipe
     */
    @Operation(description = "Create a recipe")
    @RequestMapping(method = RequestMethod.POST)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe created Successfully"),
            @ApiResponse(responseCode = "404", description = "Recipe is not created successfully")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RecipeResponse> createRecipe(@Parameter(description = "Properties of the recipe", required = true) @Valid @RequestBody CreateRecipeRequest request) {
        log.info("Creating the recipe with properties");
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe update is completed Successfully"),
            @ApiResponse(responseCode = "404", description = "Recipe update is not successful")
    })
    public ResponseEntity<RecipeResponse> updateRecipe(@Parameter(description = "Properties of the recipe", required = true) @Valid @RequestBody UpdateRecipeRequest updateRecipeRequest) {
        log.info("Updating the recipe by given properties");
        return ResponseEntity.ok(recipeService.updateRecipe(updateRecipeRequest));
    }


    @Operation(description = "search the recipes")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recipe is found successfully"),
            @ApiResponse(responseCode = "404", description = "Recipe is not found")
    })
    public ResponseEntity<List<RecipeResponse>> searchRecipe(@Parameter(description = "Properties of the recipe", required = true) @Valid @RequestBody RecipeSearchRequest recipeSearchRequest) {
        log.info("search the recipe by given properties");
        return ResponseEntity.status(HttpStatus.FOUND).body(recipeService.search(recipeSearchRequest));
    }




    /**
     * This api deletes the recipe from the application
     *
     * @param id the id of the recipe which needs to be deleted
     */

    @Operation(description = "Delete the recipe")
    @RequestMapping(method = RequestMethod.DELETE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe is successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Recipe deletion is not successful")
    })
    @ResponseStatus(HttpStatus.OK)
    public void deleteRecipe(@Parameter(description = "Recipe ID", required = true) @NotNull(message = "{id.notNull}") @RequestParam(name = "id") Integer id) {
        log.debug("Deleting the recipe by its id. Id: {}", id);
        recipeService.deleteRecipe(id);
    }

}
