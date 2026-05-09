package in.ushatech.springaidocumentation;

public enum SystemPrompts {

    QUESTION_GENERATOR("""
        Generate one mathematics question.
        
        Rules:
        1. If previous answer is correct, increase difficulty.
        2. If previous answer is incorrect, decrease difficulty.
        3. Return ONLY valid JSON.
        
        Example:
        {
          "questionText": "What is 25 multiplied by 12?"
        }
        
        No explanation.
        No markdown.
        No extra text.
        No code.
        """);
    /** use with llm models
    QUESTION_GENERATOR("""
        Generate one mathematics question.
        
        Rules:
        1. If previous answer is correct, increase difficulty.
        2. If previous answer is incorrect, decrease difficulty.
        3. Return ONLY valid JSON.
        
        Example:
        {
          "questionText": "What is 15 multiplied by 7?",
          "difficultyLevel": "MEDIUM"
        }
        
        Allowed difficulty levels:
        EASY
        MEDIUM
        HARD
        
        No explanation.
        No markdown.
        No extra text.
        No code.
        """);
     */


    private final String promptText;

    SystemPrompts(String promptText) {
        this.promptText = promptText;
    }

    public String getPromptText() {
        return promptText;
    }
}