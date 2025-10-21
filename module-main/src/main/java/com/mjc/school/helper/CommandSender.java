package com.mjc.school.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjc.school.controller.annotation.CommandBody;
import com.mjc.school.controller.annotation.CommandHandler;
import com.mjc.school.controller.annotation.CommandParam;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.function.Function;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CommandSender {

    private final Map<String, Object> controllers;
    private final ObjectMapper mapper;
    private final Map<Class<?>, Function<String, Object>> stringToObjectMappers;

    @Autowired
    public CommandSender(ListableBeanFactory listableBeanFactory) {
        this.controllers = listableBeanFactory.getBeansWithAnnotation(Controller.class);
        this.mapper = new ObjectMapper();

        this.stringToObjectMappers = new HashMap<>();
        this.stringToObjectMappers.put(Long.class, Long::valueOf);
        this.stringToObjectMappers.put(List.class, str->{
            if(str == null || str.isEmpty()){
                return Collections.emptyList();
            }
            return Arrays.stream(str.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        });
        this.stringToObjectMappers.put(Set.class, str -> {
            if (str == null || str.isEmpty()) {
                return Collections.emptySet();
            }
            try {
                return Arrays.stream(str.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Long::valueOf)
                        .collect(Collectors.toSet());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid format for Set<Long>: expected comma-separated numbers");
            }
        });
    }

    public Object send(Command command) throws Exception {
        if(command == null) {
            throw new NullPointerException("command is null");
        }

        CommandHandlerWithController commandHandlerWithController =
            getCommandHandlerWithController(command.operation());

        Method commandHandler = commandHandlerWithController.method;
        Object controller = commandHandlerWithController.controller;

        try{
            return commandHandler.invoke(controller, getMethodArgs(command, commandHandler));
        }catch(InvocationTargetException ite){
            if(ite.getTargetException() != null) {
                throw (Exception) ite.getTargetException();
            } else {
                throw ite;
            }
        }
    }

    private CommandHandlerWithController getCommandHandlerWithController(int operation) {
        List<CommandHandlerWithController> commandHandlerWithControllers = new ArrayList<>();

        for(Object controller : controllers.values()) {
            List<CommandHandlerWithController> controllerCommandHandlers =
                Stream.of(controller.getClass().getDeclaredMethods())
                    .filter(m ->!m.isBridge())
                    .filter(m-> m.isAnnotationPresent(CommandHandler.class))
                    .filter(m-> m.getAnnotation(CommandHandler.class).operation()==operation)
                    .map(m->new CommandHandlerWithController(controller,m)).toList();

            commandHandlerWithControllers.addAll(controllerCommandHandlers);
        }
        if(commandHandlerWithControllers.size()!=1) {
            throw new RuntimeException(String.format("0 or more than 1 command handlers found for operation %d", operation));
        }
        return commandHandlerWithControllers.get(0);
    }

    private Object[] getMethodArgs(Command command, Method method) throws JsonProcessingException {
        List<Object> args = new ArrayList<>();

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Parameter[] parameters = method.getParameters();

        for(int i=0; i<parameterAnnotations.length; i++) {
            for(Annotation parameterAnnotation : parameterAnnotations[i]) {
                if(parameterAnnotation instanceof CommandBody) {
                    args.add(mapper.readValue(command.body(), parameters[i].getType()));
                }else if(parameterAnnotation instanceof CommandParam an && command.params()!=null) {
                    String value = command.params().get(an.name());
                    if(value != null){
                        Class<?> paramType = parameters[i].getType();
                        if(List.class.isAssignableFrom(paramType)) {
                            Type genericType = parameters[i].getParameterizedType();
                            if(genericType instanceof ParameterizedType pt) {
                                Class<?> genericClass = (Class<?>) pt.getActualTypeArguments()[0];
                                if(genericClass == Long.class){
                                    try{
                                        args.add(Arrays.stream(value.split(","))
                                                .map(String::trim)
                                                .filter(s->!s.isEmpty())
                                                .map(Long::valueOf)
                                                .collect(Collectors.toList()));

                                    }catch(NumberFormatException nfe){
                                        throw new IllegalArgumentException("Invalid format for " + an.name() + ":" +
                                                " expected comma-separated numbers");
                                    }
                                }else{
                                    args.add(Arrays.stream(value.split(","))
                                            .map(String::trim)
                                            .filter(s -> !s.isEmpty())
                                            .collect(Collectors.toList()));
                                }
                            }else{
                                args.add(Arrays.stream(value.split(","))
                                        .map(String::trim)
                                        .filter(s->!s.isEmpty())
                                        .collect(Collectors.toList())
                                        );
                            }
                        }else{
                            Function<String, Object> mapper = stringToObjectMappers
                                    .getOrDefault(paramType,str->str);
                            args.add(mapper.apply(value));
                        }
                    }else{
                        args.add(null);
                    }
                }
            }
        }
        return args.toArray();
    }



    private record CommandHandlerWithController(Object controller, Method method) {}
}
