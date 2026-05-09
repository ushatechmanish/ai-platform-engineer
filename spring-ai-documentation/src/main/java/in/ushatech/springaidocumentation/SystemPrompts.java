package in.ushatech.springaidocumentation;

public enum SystemPrompts {
    AI_TEACHER("Act as a Maths Tutor who " +
            "is teaching someone and provide questions based on " +
            "the user's ability. You should ask at least one question and " +
            "then depending on the answer, if the answer is correct ask a more " +
            "difficult question else ask lower level questions   .");
    private String promptText;
    private SystemPrompts(String promptText)
    {
        this.promptText= promptText;
    }

    public String getPromptText() {
        return promptText;
    }
}
