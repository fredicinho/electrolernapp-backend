package ch.hslu.springbootbackend.springbootbackend.DTO;
import org.springframework.hateoas.RepresentationModel;
import java.util.Date;

public class StatisticEvaluationDTO extends RepresentationModel<StatisticEvaluationDTO> {
    private final int statisticId;
    private Date date;
    private int pointsAchieved;
    private boolean isMarked;
    private int categoryId;
    private int categorySetId;

    public StatisticEvaluationDTO(int statisticId, Date date, int pointsAchieved, boolean isMarked, int categoryId, int categorySetId) {
        this.statisticId = statisticId;
        this.date = date;
        this.pointsAchieved = pointsAchieved;
        this.isMarked = isMarked;
        this.categoryId = categoryId;
        this.categorySetId = categorySetId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategorySetId() {
        return categorySetId;
    }

    public void setCategorySetId(int categorySetId) {
        this.categorySetId = categorySetId;
    }

    public int getStatisticId() {
        return statisticId;
    }

    public void setStatisticId(int statisticId) {
        statisticId = statisticId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPointsAchieved() {
        return pointsAchieved;
    }

    public void setPointsAchieved(int pointsAchieved) {
        this.pointsAchieved = pointsAchieved;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    @Override
    public String toString() {
        return "StatisticEvaluationDTO{" +
                "statisticId=" + statisticId +
                ", date=" + date +
                ", pointsAchieved=" + pointsAchieved +
                ", isMarked=" + isMarked +
                ", categoryId=" + categoryId +
                ", categorySetId=" + categorySetId +
                '}';
    }
}
