package com.abnamro.recipe.controllers;


import com.abnamro.recipe.mapper.CommonConfigMapper;
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

@RestController
@RequestMapping(value = "api/v1/ingredient")
public class IngredientController {

    private final Logger logger = LoggerFactory.getLogger(IngredientController.class);

    private final IngredientService ingredientService;
    private final CommonConfigMapper commonConfigMapper;

    @Autowired
    public IngredientController(IngredientService ingredientService, CommonConfigMapper commonConfigMapper) {
        this.ingredientService = ingredientService;
        this.commonConfigMapper = commonConfigMapper;
    }

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

    @Operation(description = "List one ingredient by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful request"),
            @ApiResponse(responseCode = "404", description = "Ingredient not found by the given ID")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public IngredientResponse getIngredient(@Parameter(description = "Ingredient ID", required = true) @PathVariable(name = "id") Integer id) {
        logger.info("Getting the ingredient by its id. Id: {}", id);
        return commonConfigMapper.mapIngredientToIngredientResponse(ingredientService.findById(id));
    }

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
