package com.example.tiptop;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime; // import the LocalDateTime class
import com.google.firebase.storage.StorageReference;

public class Task implements Serializable {

    //Default constructor
    public Task() {
    }

    enum STATUS {
        NotAssociated,
        Associated,
        WaitingForApproval,
        Confirmed,
        NotConfirmed
    }
    private String nameTask;
    private Long bonusScore;
    private String belongsToUID; //Here we enter the UID from the function firebase.auth().Currentuser.uid
    private LocalDate startDate;
    private  LocalDate endDate;
    private STATUS status;
    private String comment;
    private StorageReference image;

    //Getter
    public String getNameTask() {
        return nameTask;
    }

    public Long getBonusScore() {
        return bonusScore;
    }

    public String getBelongsToUID() {
        return belongsToUID;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
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

    public void setBonusScore(Long bonusScore) {
        this.bonusScore = bonusScore;
    }

    public void setBelongsToUID(String belongsToUID) {
        this.belongsToUID = belongsToUID;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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
        return "Task{"+"Name: "+nameTask+'\'' +
                "Bonus Score: "+bonusScore+'\'' +
                "Start date and hour: "+startDate+'\'' +
                "End date and hour: "+endDate+'\'' +
                "Status"+status+'\'' +
                "Comment"+comment+'\''+
                "}";
    }
}
