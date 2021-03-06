package com.example.tiptop.Objects;

import java.io.Serializable;
import com.google.firebase.storage.StorageReference;

public class Task implements Serializable {

    //Fields
    private String nameTask;
    private Long bonusScore;
    private String belongsToUID; //Here we enter the UID from the function firebase.auth().Currentuser.uid
    private String startDate;
    private String endDate;
    private String confirmedDate;
    private STATUS status;
    private String comment;
    private String description;
    private StorageReference image;

    //Default constructor
    public Task() {
    }

    public enum STATUS {
        NotAssociated,
        Associated,
        WaitingForApproval,
        Confirmed
    }

    //Getter
    public String getNameTask() {
        return nameTask;
    }

    public String getDescription() {
        return description;
    }

    public Long getBonusScore() {
        return bonusScore;
    }

    public String getBelongsToUID() {
        return belongsToUID;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getConfirmedDate() {
        return confirmedDate;
    }

    public STATUS getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public StorageReference getImage() {
        return image;
    }

    //Setter
    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBonusScore(Long bonusScore) {
        this.bonusScore = bonusScore;
    }

    public void setBelongsToUID(String belongsToUID) {
        this.belongsToUID = belongsToUID;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setConfirmedDate(String confirmedDate) {
        this.confirmedDate = confirmedDate;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setImage(StorageReference image) {
        this.image = image;
    }


    //methods
    @Override
    public String toString() {
        return "Task{" +
                "nameTask='" + nameTask + '\'' +
                ", bonusScore=" + bonusScore +
                ", belongsToUID='" + belongsToUID + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", confirmedDate='" + confirmedDate + '\'' +
                ", status=" + status +
                ", comment='" + comment + '\'' +
                ", description='" + description + '\'' +
                ", image=" + image +
                '}';
    }
}
