package in.ushatech.springaidocumentation.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static in.ushatech.springaidocumentation.SystemPrompts.AI_TEACHER;


@RestController
public class MathsController {

    ChatClient chatClient;


    public MathsController(ChatClient chatClient) {
       this.chatClient = chatClient;

    }

    /**
     * Usage : curl -X GET --location "http://localhost:8080/question?userInput=%22what+is+1+1%22"
     * */
    @GetMapping("/question")
    public String question(@RequestParam String userInput) { // spring automatically maps request parameters to userInput So @RequestParam is not mandatory but good to have
        return this.chatClient.prompt(userInput)// this is not fluent api there is another variation of this prompt(Prompt prompt)
                .call()
                .content();
    }

    @GetMapping("/questionFluent")
    public String questionFluentApi(@RequestParam String userInput) { // spring automatically maps request parameters to userInput So @RequestParam is not mandatory but good to have
        return this.chatClient.prompt()
                .user(userInput)// this is  fluent api
                .call()
                .content();
    }

    @GetMapping("/questionChatResponse")
    public ChatResponse questionFluentApiWithChatResponse(@RequestParam String userInput) { // spring automatically maps request parameters to userInput So @RequestParam is not mandatory but good to have
        return this.chatClient.prompt()
                .user(userInput)// this is  fluent api
                .call()
                .chatResponse();
    }


}
