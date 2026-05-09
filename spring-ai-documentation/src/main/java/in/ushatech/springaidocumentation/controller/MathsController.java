package in.ushatech.springaidocumentation.controller;

import in.ushatech.springaidocumentation.entity.Question;
import in.ushatech.springaidocumentation.entity.QuestionResponse;
import org.springframework.ai.chat.client.AdvisorParams;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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
    public QuestionResponse questionFluentApiWithChatResponse(@RequestParam("userInput") String userInput) { // spring automatically maps request parameters to userInput So @RequestParam is not mandatory but good to have

        // because of error  "message": "400: Invalid schema for response_format 'json_schema': schema must be a JSON Schema of 'type: \"object\"', got 'type: \"array\"'.",
        // We need to have single object a wrapper DTO , so we need to change List<Question> to QuestionResponses having List<Question>

        /**
         * {
         *   "questions": [
         *     {
         *       "questionText": "..."
         *     }
         *   ]
         * }
         * is expected Now using a dto wrapper works
         * */
        BeanOutputConverter<QuestionResponse> converter = new BeanOutputConverter<>(QuestionResponse.class);
        return this.chatClient.prompt()
                .user(userInput)// this is  fluent api , removed formatter as it will increase tokens and is not required as we are using advisor
                .advisors(AdvisorParams.ENABLE_NATIVE_STRUCTURED_OUTPUT)//asks provider/OpenAI for schema-safe JSON
                .call()
                .entity(converter); // use convertor instead of ParameterizedTypeReference . This is the cleanest approach to use with spring-ai
    }


}
