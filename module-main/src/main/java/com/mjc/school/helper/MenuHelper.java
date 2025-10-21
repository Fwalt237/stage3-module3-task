package com.mjc.school.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mjc.school.helper.Constant.*;
import static com.mjc.school.helper.Operations.*;


@Component
public class MenuHelper {

    private final ObjectMapper mapper;

    private final Map<String, Function<Scanner, Command>> operations;

    private final PrintStream printStream ;

    public MenuHelper() {
        mapper = new ObjectMapper();
        operations = new HashMap<>();

        operations.put(String.valueOf(GET_ALL_NEWS.getOperationNumber()), this::getNews);
        operations.put(String.valueOf(GET_NEWS_BY_ID.getOperationNumber()), this::getNewsById);
        operations.put(String.valueOf(CREATE_NEWS.getOperationNumber()), this::createNews);
        operations.put(String.valueOf(UPDATE_NEWS.getOperationNumber()), this::updateNews);
        operations.put(String.valueOf(REMOVE_NEWS_BY_ID.getOperationNumber()), this::deleteNews);
        operations.put(String.valueOf(GET_NEWS_BY_PARAMS.getOperationNumber()), this::getNewsByParams);

        operations.put(String.valueOf(GET_ALL_AUTHORS.getOperationNumber()), this::getAuthors);
        operations.put(String.valueOf(GET_AUTHOR_BY_ID.getOperationNumber()), this::getAuthorById);
        operations.put(String.valueOf(CREATE_AUTHOR.getOperationNumber()), this::createAuthor);
        operations.put(String.valueOf(UPDATE_AUTHOR.getOperationNumber()), this::updateAuthor);
        operations.put(String.valueOf(REMOVE_AUTHOR_BY_ID.getOperationNumber()), this::deleteAuthor);
        operations.put(String.valueOf(GET_AUTHOR_BY_NEWS_ID.getOperationNumber()), this::getAuthorByNewsId);

        operations.put(String.valueOf(GET_ALL_TAGS.getOperationNumber()), this::getTags);
        operations.put(String.valueOf(GET_TAG_BY_ID.getOperationNumber()), this::getTagById);
        operations.put(String.valueOf(CREATE_TAG.getOperationNumber()), this::createTag);
        operations.put(String.valueOf(UPDATE_TAG.getOperationNumber()), this::updateTag);
        operations.put(String.valueOf(REMOVE_TAG_BY_ID.getOperationNumber()), this::deleteTag);
        operations.put(String.valueOf(GET_TAG_BY_NEWS_ID.getOperationNumber()), this::getTagByNewsId);

        this.printStream = System.out;
    }

    public void printMainMenu(){
        printStream.println(NUMBER_OPERATION_ENTER);
        for(Operations operation : Operations.values()){
            printStream.println(operation.getOperationWithNumber());
        }
    }

    public Command getCommand(Scanner keyboard, String key){
        return operations.getOrDefault(key, this::getCommandNotFound).apply(keyboard);
    }

    private Command getCommandNotFound(Scanner keyboard){
        return Command.NOT_FOUND;
    }

    private Command getNews(Scanner keyboard){
        printStream.println(GET_ALL_NEWS.getOperation());
        return Command.GET_NEWS;

    }

    private Command getNewsById(Scanner keyboard){
        printStream.println(GET_NEWS_BY_ID.getOperation());
        printStream.println(NEWS_ID_ENTER);
        return new Command(
                GET_NEWS_BY_ID.getOperationNumber(),
                Map.of("id", String.valueOf(getKeyboardNumber(NEWS_ID, keyboard))),
                null);
    }

    private Command createNews(Scanner keyboard) {
        Command command = null;
        boolean isValid = false;
        while (!isValid) {
            try {
                printStream.println(CREATE_NEWS.getOperation());
                printStream.println(NEWS_TITLE_ENTER);
                String title = keyboard.nextLine();
                printStream.println(NEWS_CONTENT_ENTER);
                String content = keyboard.nextLine();
                printStream.println(AUTHOR_ID_ENTER);
                long authorId = getKeyboardNumber(AUTHOR_ID, keyboard);
                printStream.println("Enter tag IDs (comma-separated, or press Enter to skip):");
                String tagIdsInput = keyboard.nextLine().trim();
                Set<Long> tagIds = null;
                if (!tagIdsInput.isEmpty()) {
                    try {
                        tagIds = Arrays.stream(tagIdsInput.split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .map(Long::valueOf)
                                .collect(Collectors.toSet());
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid tag IDs format. Please enter comma-separated numbers.");
                    }
                }
                Map<String, Object> body = new HashMap<>();
                body.put("title", title);
                body.put("content", content);
                body.put("authorId", Long.toString(authorId));
                if (tagIds != null && !tagIds.isEmpty()) {
                    body.put("tagIds", tagIds);
                }

                command = new Command(CREATE_NEWS.getOperationNumber(), null, mapper.writeValueAsString(body));
                isValid = true;
            } catch (Exception ex) {
                printStream.println(ex.getMessage());
            }
        }
        return command;
    }

    private Command updateNews(Scanner keyboard) {
        Command command = null;
        boolean isValid = false;
        while (!isValid) {
            try {
                printStream.println(UPDATE_NEWS.getOperation());
                printStream.println(NEWS_ID_ENTER);
                long newsId = getKeyboardNumber(NEWS_ID, keyboard);
                printStream.println(NEWS_TITLE_ENTER);
                String title = keyboard.nextLine();
                printStream.println(NEWS_CONTENT_ENTER);
                String content = keyboard.nextLine();
                printStream.println(AUTHOR_ID_ENTER);
                long authorId = getKeyboardNumber(AUTHOR_ID, keyboard);
                printStream.println("Enter tag Ids (comma-separated, or press Enter to skip):");
                String tagIdsInput = keyboard.nextLine().trim();
                Set<Long> tagIds = null;
                if (!tagIdsInput.isEmpty()) {
                    try {
                        tagIds = Arrays.stream(tagIdsInput.split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .map(Long::valueOf)
                                .collect(Collectors.toSet());
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid tag IDs format. Please enter comma-separated numbers.");
                    }
                }

                Map<String, Object> body = new HashMap<>();
                body.put("newsId", Long.toString(newsId));
                body.put("title", title);
                body.put("content", content);
                body.put("authorId", Long.toString(authorId));
                if (tagIds != null && !tagIds.isEmpty()) {
                    body.put("tagIds", tagIds);
                }

                command = new Command(UPDATE_NEWS.getOperationNumber(), null, mapper.writeValueAsString(body));
                isValid = true;
            } catch (Exception ex) {
                printStream.println(ex.getMessage());
            }
        }
        return command;
    }

    private Command deleteNews(Scanner keyboard) {
        printStream.println(REMOVE_NEWS_BY_ID.getOperation());
        printStream.println(NEWS_ID_ENTER);
        return new Command(
                REMOVE_NEWS_BY_ID.getOperationNumber(),
                Map.of("id", Long.toString(getKeyboardNumber(NEWS_ID, keyboard))),
                null);
    }

    private Command getNewsByParams(Scanner keyboard) {
        printStream.println(GET_NEWS_BY_PARAMS.getOperation());
        Map<String,String> params = new HashMap<>();

        printStream.println("Enter tag names(comma-separated, or press Enter to skip):");
        String tagNames = keyboard.nextLine().trim();
        if(!tagNames.isEmpty()){
            params.put("tagNames", tagNames);
        }

        printStream.println("Enter tag Ids(comma-separated, or press Enter to skip):");
        String tagIds = keyboard.nextLine().trim();
        if(!tagIds.isEmpty()){
            try{
                Arrays.stream(tagIds.split(",")).map(String::trim).forEach(Long::valueOf);
                params.put("tagIds", tagIds);
            }catch(NumberFormatException nfe){
                printStream.println("Invalid tag Ids format. Please Enter commas separated numbers.");
                return getCommandNotFound(keyboard);
            }
        }

        printStream.println("Enter author name (or press Enter to skip):");
        String authorName = keyboard.nextLine().trim();
        if(!authorName.isEmpty()){
            params.put("authorName", authorName);
        }

        printStream.println("Enter news title (or press Enter to skip):");
        String title = keyboard.nextLine().trim();
        if(!title.isEmpty()){
            params.put("title", title);
        }

        printStream.println("Enter news content (or press Enter to skip):");
        String content = keyboard.nextLine().trim();
        if(!content.isEmpty()){
            params.put("content", content);
        }

        if(params.isEmpty()) {
            printStream.println("At least one parameter is required for search.");
            return getCommandNotFound(keyboard);
        }
        return new Command(GET_NEWS_BY_PARAMS.getOperationNumber(), params, null);
    }



    private Command getAuthors(Scanner keyboard) {
        printStream.println(GET_ALL_AUTHORS.getOperation());
        return Command.GET_AUTHORS;
    }

    private Command getAuthorById(Scanner keyboard) {
        printStream.println(GET_AUTHOR_BY_ID.getOperation());
        printStream.println(AUTHOR_ID_ENTER);
        return new Command(
                GET_AUTHOR_BY_ID.getOperationNumber(),
                Map.of("id", String.valueOf(getKeyboardNumber(AUTHOR_ID, keyboard))),
                null);
    }

    private Command createAuthor(Scanner keyboard) {
        Command command = null;
        boolean isValid = false;
        while (!isValid) {
            try {
                printStream.println(CREATE_AUTHOR.getOperation());
                printStream.println(AUTHOR_NAME_ENTER);
                String name = keyboard.nextLine();

                Map<String, String> body = Map.of("name", name);
                command =
                        new Command(CREATE_AUTHOR.getOperationNumber(), null, mapper.writeValueAsString(body));
                isValid = true;
            } catch (Exception ex) {
                printStream.println(ex.getMessage());
            }
        }

        return command;
    }

    private Command updateAuthor(Scanner keyboard) {
        Command command = null;
        boolean isValid = false;
        while (!isValid) {
            try {
                printStream.println(UPDATE_AUTHOR.getOperation());
                printStream.println(AUTHOR_ID_ENTER);
                long authorId = getKeyboardNumber(AUTHOR_ID, keyboard);
                printStream.println(AUTHOR_NAME_ENTER);
                String name = keyboard.nextLine();

                Map<String, String> body = Map.of("authorId", Long.toString(authorId), "name", name);
                command =
                        new Command(UPDATE_AUTHOR.getOperationNumber(), null, mapper.writeValueAsString(body));
                isValid = true;
            } catch (Exception ex) {
                printStream.println(ex.getMessage());
            }
        }

        return command;
    }

    private Command deleteAuthor(Scanner keyboard) {
        printStream.println(REMOVE_AUTHOR_BY_ID.getOperation());
        printStream.println(AUTHOR_ID_ENTER);
        return new Command(
                REMOVE_AUTHOR_BY_ID.getOperationNumber(),
                Map.of("id", Long.toString(getKeyboardNumber(AUTHOR_ID, keyboard))),
                null);
    }

    private Command getAuthorByNewsId(Scanner keyboard) {
        printStream.println(GET_AUTHOR_BY_NEWS_ID.getOperation());
        printStream.println(NEWS_ID_ENTER);
        return new Command(GET_AUTHOR_BY_NEWS_ID.getOperationNumber(),
                Map.of("id", String.valueOf(getKeyboardNumber(NEWS_ID, keyboard))),
                null

        );
    }


    private Command getTags(Scanner keyboard) {
        printStream.println(GET_ALL_TAGS.getOperation());
        return Command.GET_TAGS;
    }

    private Command getTagById(Scanner keyboard) {
        printStream.println(GET_TAG_BY_ID.getOperation());
        printStream.println(TAG_ID_ENTER);
        return new Command(
                GET_TAG_BY_ID.getOperationNumber(),
                Map.of("id", String.valueOf(getKeyboardNumber(TAG_ID, keyboard))),
                null);
    }

    private Command createTag(Scanner keyboard) {
        Command command = null;
        boolean isValid = false;
        while (!isValid) {
            try {
                printStream.println(CREATE_TAG.getOperation());
                printStream.println(TAG_NAME_ENTER);
                String name = keyboard.nextLine();

                Map<String, String> body = Map.of("name", name);
                command =
                        new Command(CREATE_TAG.getOperationNumber(), null, mapper.writeValueAsString(body));
                isValid = true;
            } catch (Exception ex) {
                printStream.println(ex.getMessage());
            }
        }

        return command;
    }

    private Command updateTag(Scanner keyboard) {
        Command command = null;
        boolean isValid = false;
        while (!isValid) {
            try {
                printStream.println(UPDATE_TAG.getOperation());
                printStream.println(TAG_ID_ENTER);
                long tagId = getKeyboardNumber(TAG_ID, keyboard);
                printStream.println(TAG_NAME_ENTER);
                String name = keyboard.nextLine();

                Map<String, String> body = Map.of("tagId", Long.toString(tagId), "name", name);
                command =
                        new Command(UPDATE_TAG.getOperationNumber(), null, mapper.writeValueAsString(body));
                isValid = true;
            } catch (Exception ex) {
                printStream.println(ex.getMessage());
            }
        }

        return command;
    }

    private Command deleteTag(Scanner keyboard) {
        printStream.println(REMOVE_TAG_BY_ID.getOperation());
        printStream.println(TAG_ID_ENTER);
        return new Command(
                REMOVE_TAG_BY_ID.getOperationNumber(),
                Map.of("id", Long.toString(getKeyboardNumber(TAG_ID, keyboard))),
                null);
    }

    private Command getTagByNewsId(Scanner keyboard) {
        printStream.println(GET_TAG_BY_NEWS_ID.getOperation());
        printStream.println(TAG_ID_ENTER);
        return new Command(GET_TAG_BY_NEWS_ID.getOperationNumber(),
                Map.of("id", String.valueOf(getKeyboardNumber(NEWS_ID, keyboard))),
                null

        );
    }

    private long getKeyboardNumber(String params, Scanner keyboard){
        long enter;
        try{
            enter = keyboard.nextLong();
            keyboard.nextLine();
        }catch(Exception e){
            keyboard.nextLine();
            throw new RuntimeException(String.format("%s should be a number.", params));
        }
        return enter;
    }

}

