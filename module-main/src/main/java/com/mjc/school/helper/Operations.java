package com.mjc.school.helper;

import lombok.Getter;

import static com.mjc.school.helper.Constant.OPERATION;

public enum Operations {
    GET_ALL_NEWS(1, "Get all news."),
    GET_NEWS_BY_ID(2, "Get news by id."),
    CREATE_NEWS(3, "Create news."),
    UPDATE_NEWS(4, "Update news."),
    REMOVE_NEWS_BY_ID(5, "Remove news by id."),
    GET_NEWS_BY_PARAMS(6, "Get news by the following parameters:"),

    GET_ALL_AUTHORS(7, "Get all authors."),
    GET_AUTHOR_BY_ID(8, "Get author by id."),
    CREATE_AUTHOR(9, "Create author."),
    UPDATE_AUTHOR(10, "Update author."),
    REMOVE_AUTHOR_BY_ID(11, "Remove author by id."),
    GET_AUTHOR_BY_NEWS_ID(12, "Get author by news id."),

    GET_ALL_TAGS(13, "Get all tags."),
    GET_TAG_BY_ID(14, "Get tag by id."),
    CREATE_TAG(15, "Create tag."),
    UPDATE_TAG(16, "Update tag."),
    REMOVE_TAG_BY_ID(17, "Remove tag by id."),
    GET_TAG_BY_NEWS_ID(18, "Get tag by news id."),

    EXIT(0, "Exit."),
    ;

    @Getter
    private final int operationNumber;

    private final String operation;

    Operations(int operationNumber, String operation) {
        this.operationNumber = operationNumber;
        this.operation = operation;
    }

    public String getOperation() {return OPERATION + operation;}

    public String getOperationWithNumber(){return operationNumber + "-" +operation;}

}
