package in.ushatech.springaidocumentation.config;

import in.ushatech.springaidocumentation.prompts.SystemPrompts;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatClientConfig {

    @Bean
    @Primary
    public ChatClient openAiChatClient(OpenAiChatModel chatModel) {
        return chatClientWithDefaultSystemPrompt(chatModel);
    }

    @Bean
    @Qualifier("ollamaChatClient")
    public ChatClient ollamaChatClient(OllamaChatModel chatModel) {
        return chatClientWithDefaultSystemPrompt(chatModel);
    }

    private ChatClient chatClientWithDefaultSystemPrompt(ChatModel chatModel) {
        return ChatClient.builder(chatModel).defaultSystem(SystemPrompts.QUESTION_GENERATOR.getPromptText()).build();
    }


}