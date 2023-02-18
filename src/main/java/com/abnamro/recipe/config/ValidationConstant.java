package com.abnamro.recipe.config;

/**
 * This class contains the Validation related length,patterns
 */
public class ValidationConstant {

    /**
     * name pattern of recipe
     */
    public static final String PATTERN_NAME = "^(?:\\p{L}\\p{M}*|[',. \\-]|\\s)*$";

    /**
     * Max length of recipe
     */
    public static final int MAX_LENGTH_NAME = 100;

    /**
     * Default max length
     */
    public static final int MAX_LENGTH_DEFAULT = 255;

    /**
     * Matches for free text fields for instructions
     */
    public static final String PATTERN_FREE_TEXT = "^(?:\\p{L}\\p{M}*|[0-9]*|[\\/\\-+.,?!*();\"]|\\s)*$";


}
