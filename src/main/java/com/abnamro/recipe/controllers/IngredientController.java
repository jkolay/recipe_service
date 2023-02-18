package com.abnamro.recipe.controllers;

import com.abnamro.recipe.model.request.CreateIngredientRequest;
import com.abnamro.recipe.model.response.IngredientResponse;
import com.abnamro.recipe.service.IngredientService;
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
 * This is the controller/api  class for Ingredients
 */
@RestController
@RequestMapping(value = "api/v1/ingredient")
public class IngredientController {

    private final Logger logger = LoggerFactory.getLogger(IngredientController.class);
    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /**
     * This endpoint helps to retrieve all the ingredients based on page and size
     * @param page the page number
     * @param size required record size in a page
     * @return list of ingredients
     */
    @Operation(description = "List all ingredients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request"),
    })
    @RequestMapping(method = RequestMethod.GET, path = "/page/{page}/size/{size}")
    public List<IngredientResponse> getIngredientList(@PathVariable(name = "page") int page,
                                                      @PathVariable(name = "size") int size) {
        logger.info("Getting the ingredients");
        return ingredientService.list(page, size);
    }

    /**
     * this end points helps to retrieve an ingredient
     * @param id id of the ingredient which needs to be retrieved
     * @return the ingredient
     */
    @Operation(description = "List one ingredient by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request"),
            @ApiResponse(responseCode = "404", description = "Ingredient not found by the given ID")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public IngredientResponse getIngredient(@Parameter(description = "Ingredient ID", required = true) @PathVariable(name = "id") Integer id) {
        logger.info("Getting the ingredient by its id. Id: {}", id);
        return ingredientService.getIngredient(id);
    }

    /**
     * this api helps to create ingredient in the sapplication
     * @param request the ingredient request object
     * @return the ingredient response after creation
     */
    @Operation(description = "Create an ingredient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ingredient created"),
            @ApiResponse(responseCode = "400", description = "Bad input")
    })
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public IngredientResponse createIngredient(
            @Parameter(description = "Properties of the Ingredient", required = true) @Valid @RequestBody CreateIngredientRequest request) {
        logger.info("Creating the ingredient with properties");
        return ingredientService.create(request);
    }
    /**
     * this api helps to create ingredient in the application
     * @param requests the ingredient request objects
     * @return the ingredient responses after creation
     */
    @Operation(description = "Create list of ingredient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ingredient created"),
            @ApiResponse(responseCode = "400", description = "Bad input")
    })
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public List<IngredientResponse> createIngredients(
            @Parameter(description = "Properties of the Ingredient", required = true) @Valid @RequestBody List<CreateIngredientRequest> requests) {
        logger.info("Creating the ingredient with properties");
        return ingredientService.createIngredients(requests);
    }

    /**
     * this api deletes an ingredient
     * @param id the id of the ingredient which need sto be deleted
     */
    @Operation(description = "Delete the ingredient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Ingredient not found by the given ID")
    })
    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteIngredient(@Parameter(description = "ingredient ID", required = true) @NotNull(message = "{id.notNull}") @RequestParam(name = "id") Integer id) {
        logger.info("Deleting the ingredient by its id. Id: {}", id);
        ingredientService.delete(id);
    }
}
