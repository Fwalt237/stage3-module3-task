package com.mjc.school.helper;

import com.mjc.school.controller.annotation.CommandHandler;
import org.springframework.stereotype.Controller;

import static com.mjc.school.helper.Constant.COMMAND_NOT_FOUND;

@Controller
public class CommandNotFoundController {

    @CommandHandler(operation = -1)
    public String commandNotFound() {return COMMAND_NOT_FOUND;}
}
