package com.abnamro.recipe.config;


public class MessageConfig {
    public static final String ID_NOT_NULL = "Please provide a valid ID.";
    public static final String INGREDIENT_SIZE_NOT_VALID = "Name can be 100 characters long at maximum.";
    public static final String INGREDIENT_PATTERN_NOT_VALID = "The ingredient name should contain only letters and the following characters: ',.- and space.";
    public static final String INGREDIENT_NAME_NOT_NULL = "Please provide a valid name for the ingredient";
    public static final String RECIPE_NAME_NOT_NULL = "Please provide a valid name for the recipe";
    public static final String RECIPE_SIZE_NOT_VALID = "Name can be 100 characters long at maximum.";
    public static final String RECIPE_PATTERN_NOT_VALID = "The recipe name should contain only letters and the following characters: ',.- and space.";

    public static final String INVALID_RECIPE_TYPE = "Recipe Type can be either Vegetarian or other";
    public static final String INVALID_NUMBER_OF_SERVING = "Please provide valid numbers of serving";
    public static final String INSTRUCTION_SIZE_NOT_VALID = "The description can be 255 characters long at maximum.";
    public static final String INSTRUCTION_PATTERN_NOT_VALID = "Please provide a valid instruction. Avoid using special characters, except: \\\\ / - + . , ? ! * ( ) ; \".";

    public static final String INSTRUCTION_IS_NOT_VALID = "The description can be {max} characters long at maximum.";
    public static final String DATA_OPTION_NOT_VALID = "the data option is invalid";
    public static final String SEARCH_OPTION_IS_NOT_VALID = "Search option is not valid";
    public static final String FILTER_KEY_IS_INVALID = "filter key is invalid";
    public static final String RECIPE_IS_NOT_FOUND = "Recipe is not available";
    public static final String INGREDIENT_ALREADY_EXISTS = "Ingredient is already present.";
    public static final String INGREDIENT_IS_NOT_AVAILABLE = "Ingredient is not available for ingredient Id : ";
    public static final String RECIPE_ALREADY_EXISTS = "Recipe is already present with id : ";
}