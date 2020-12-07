package ch.hslu.springbootbackend.springbootbackend.DTO;

import java.util.Objects;

public class ExamResultOverviewDTO {

    private int examSetId;

    private String username;

    private double maximalNumberOfPoints;

    private double achievedPoints;

    private double grade;

    public ExamResultOverviewDTO(){}
    public ExamResultOverviewDTO(int examSetId, String userName) {
        this.examSetId = examSetId;
        this.username = userName;
    }


    public int getExamSetId() {
        return examSetId;
    }

    public void setExamSetId(int examSetId) {
        this.examSetId = examSetId;
    }


    public double getMaximalNumberOfPoints() {
        return maximalNumberOfPoints;
    }

    public void setMaximalNumberOfPoints(double maximalNumberOfPoints) {
        this.maximalNumberOfPoints = maximalNumberOfPoints;
    }

    public double getAchievedPoints() {
        return achievedPoints;
    }

    public void setAchievedPoints(double achievedPoints) {
        this.achievedPoints = achievedPoints;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExamResultOverviewDTO that = (ExamResultOverviewDTO) o;
        return examSetId == that.examSetId &&
                Double.compare(that.maximalNumberOfPoints, maximalNumberOfPoints) == 0 &&
                Double.compare(that.achievedPoints, achievedPoints) == 0 &&
                Double.compare(that.grade, grade) == 0 &&
                Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(examSetId, username, maximalNumberOfPoints, achievedPoints, grade);
    }
}
