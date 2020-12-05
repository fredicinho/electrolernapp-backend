package ch.hslu.springbootbackend.springbootbackend.DTO;

public class CategorySetOverviewDTO {

    private int categorySetId;

    private int numberOfQuestions;

    private double maximalNumberOfPoints;

    public CategorySetOverviewDTO(){}

    public CategorySetOverviewDTO(int categorySetId) {
        this.categorySetId = categorySetId;
        this.numberOfQuestions = numberOfQuestions;
        this.maximalNumberOfPoints = maximalNumberOfPoints;
    }

    public int getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public double getMaximalNumberOfPoints() {
        return maximalNumberOfPoints;
    }

    public void setMaximalNumberOfPoints(double maximalNumberOfPoints) {
        this.maximalNumberOfPoints = maximalNumberOfPoints;
    }

    public int getCategorySetId() {
        return categorySetId;
    }

    public void setCategorySetId(int categorySetId) {
        this.categorySetId = categorySetId;
    }


}
