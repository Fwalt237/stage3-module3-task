package com.mjc.school;



import com.mjc.school.helper.Command;
import com.mjc.school.helper.CommandSender;
import com.mjc.school.helper.MenuHelper;
import com.mjc.school.helper.Operations;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Scanner;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableJpaAuditing
@EntityScan(basePackages ="com.mjc.school.repository.model")
@EnableCaching
@EnableTransactionManagement
public class ModuleMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(ModuleMainApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            Scanner keyboard = new Scanner(System.in);

            MenuHelper helper = ctx.getBean(MenuHelper.class);
            CommandSender commandSender = ctx.getBean(CommandSender.class);

            while(true) {
                try{
                    helper.printMainMenu();
                    String key = keyboard.nextLine().trim();
                    if(Integer.toString(Operations.EXIT.getOperationNumber()).equals(key)) {
                        System.exit(0);
                    }

                    Command command = helper.getCommand(keyboard,key);
                    Object result = commandSender.send(command);
                    if(result instanceof Iterable it) {
                        it.forEach(System.out::println);
                    }else{
                        System.out.println(result);
                    }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        };
    }
}
