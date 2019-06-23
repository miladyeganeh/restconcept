package com.milad.ws.restconcept.exception;

public enum ErrorMessages {
    MISSING_REQUIRED_FIELD("Missing required fields, please check documentation"),
    RECORD_ALREADY_EXIST("Record already exist"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    NO_RECORD_FOUND("Record with provided id is not found"),
    AUTHENTICATION_FAILED("Authentication failed"),
    COULD_NOT_UPDATE_RECORD("Could not update record"),
    COULD_NOT_DELETE_RECORD("Could not delete record"),
    EMAIL_ADDRESS_NOT_VERYFIED("Email address could not veryfied");

    String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    public void serErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }
}