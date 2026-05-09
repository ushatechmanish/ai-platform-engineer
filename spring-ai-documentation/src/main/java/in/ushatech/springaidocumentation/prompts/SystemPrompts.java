package in.ushatech.springaidocumentation.prompts;

public enum SystemPrompts {

    QUESTION_GENERATOR("""
        Generate mathematics questions.
        
        Return ONLY valid JSON.
        
        Example:
        {
          "questions": [
            {
              "questionText": "What is 25 multiplied by 12?"
            },
            {
              "questionText": "What is 3 multiplied by 1?"
            }
          ]
        }
        
        No explanation.
        No markdown.
        No extra text.
        No code.
        """);


    private final String promptText;

    SystemPrompts(String promptText) {
        this.promptText = promptText;
    }

    public String getPromptText() {
        return promptText;
    }
}