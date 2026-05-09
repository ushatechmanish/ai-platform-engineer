package in.ushatech.springaidocumentation.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String question(String userInput) { // spring automatically maps request parameters to userInput So @RequestParam is not mandatory but good to have
        return this.chatClient.prompt(userInput)
                .call()
                .content();
    }

}
