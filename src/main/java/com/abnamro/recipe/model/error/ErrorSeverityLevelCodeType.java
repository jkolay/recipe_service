package com.abnamro.recipe.model.error;

public enum ErrorSeverityLevelCodeType {
    WARNING,
    ERROR,
    INFORMATION;

    public static ErrorSeverityLevelCodeType getValue(String name){
        ErrorSeverityLevelCodeType value=null;
        for(ErrorSeverityLevelCodeType errorSeverityLevelCodeType: ErrorSeverityLevelCodeType.values()){
            if(errorSeverityLevelCodeType.name().equalsIgnoreCase(name)){
                value=errorSeverityLevelCodeType;
            }
        }
        return value;
    }
}
