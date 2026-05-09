package in.ushatech.springaidocumentation.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;
@JsonIgnoreProperties(ignoreUnknown = true)
public record Question(
        String questionText){}

/** To be used with llm
public record Question(

        String questionText,
        DifficultyLevel difficultyLevel,
        String id,

        Subject subject,

        Integer standard,


        List<ExamType> examTypes,

        List<String> topics,

        QuestionType questionType,

        List<Option> options,

        String correctAnswer,

        String explanation,

        Integer marks,

        Integer expectedTimeInSeconds,

        BloomLevel bloomLevel,

        String source,

        String language,

        Map<String, Object> metadata

) {
}*/