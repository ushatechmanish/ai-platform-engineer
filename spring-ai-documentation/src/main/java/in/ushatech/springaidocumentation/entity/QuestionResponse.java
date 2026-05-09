package in.ushatech.springaidocumentation.entity;

import java.util.List;

public record QuestionResponse(
        List<Question> questions
) {
}