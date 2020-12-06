package ch.hslu.springbootbackend.springbootbackend.DTO;

public class CategorySetOverviewDTO {

    private int categorySetId;

    private int numberOfQuestions;

    private double maximalNumberOfPoints;

    private String title;

    private int categoryId;

    public CategorySetOverviewDTO(){}

    public CategorySetOverviewDTO(int categorySetId) {
        this.categorySetId = categorySetId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
