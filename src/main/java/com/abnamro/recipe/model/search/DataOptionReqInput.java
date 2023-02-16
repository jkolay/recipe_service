package com.abnamro.recipe.model.search;

import java.util.Optional;

public enum DataOptionReqInput {
    ANY, ALL;

    public static Optional<DataOptionReqInput> getDataOption(final String dataOption) {
        String lowerDataOption = dataOption.toLowerCase();
        switch (lowerDataOption) {
            case "all":
                return Optional.of(ALL);
            case "any":
                return Optional.of(ANY);
        }
        return Optional.empty();
    }
}
