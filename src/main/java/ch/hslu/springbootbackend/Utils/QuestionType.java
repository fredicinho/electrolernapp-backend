package ch.hslu.springbootbackend.Utils;

public enum QuestionType {
    MC("Multiple Choice");

    private String type;

    QuestionType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static QuestionType fromString(String type) {
        for (QuestionType questionType : QuestionType.values()) {
            if (questionType.type.equalsIgnoreCase(type)) {
                return questionType;
            }
        }
        throw new IllegalArgumentException("No QuestionType called " + type + " found!");
    }
}
