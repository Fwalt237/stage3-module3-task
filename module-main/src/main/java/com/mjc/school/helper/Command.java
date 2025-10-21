package com.mjc.school.helper;

import java.util.Map;

import static com.mjc.school.helper.Operations.GET_ALL_NEWS;
import static com.mjc.school.helper.Operations.GET_ALL_TAGS;

public record Command(
    int operation,
    Map<String,String> params,
    String body) {

    public static Command NOT_FOUND = new Command(-1, null, null);
    public static Command GET_NEWS = new Command(GET_ALL_NEWS.getOperationNumber(), null, null);
    public static Command GET_AUTHORS = new Command(GET_ALL_NEWS.getOperationNumber(), null, null);
    public static Command GET_TAGS = new Command(GET_ALL_TAGS.getOperationNumber(), null, null);
}
