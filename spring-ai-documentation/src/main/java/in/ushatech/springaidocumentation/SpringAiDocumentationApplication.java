package in.ushatech.springaidocumentation;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringAiDocumentationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiDocumentationApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner commandLineRunner(ChatClient.Builder builder) {
//        return args -> {
//            ChatClient chatClient = builder.build();
//            String response = chatClient.prompt("Tell me an easy maths question").call().content();
//            System.out.println(response);
//        };
//    }
}
