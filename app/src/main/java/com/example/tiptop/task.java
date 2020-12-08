package com.example.tiptop;

import java.io.Serializable;
import java.time.LocalDateTime; // import the LocalDateTime class
import com.google.firebase.storage.StorageReference;

public class task implements Serializable {

    //Default constructor
    public task() {
    }

    enum STATUS {
        NotAssociated,
        Associated,
        WaitingForApproval,
        Confirmed,
        NotConfirmed
    }
    private String nameTask;
    private int bonusScore;
    private String belongsToUID; //Here we enter the UID from the function firebase.auth().Currentuser.uid
    private  LocalDateTime startDateAndHour;
    private  LocalDateTime endDateAndHour;
    private STATUS status;
    private String comment;
    private StorageReference image;

    //Getter
    public String getNameTask() {
        return nameTask;
    }

    public int getBonusScore() {
        return bonusScore;
    }

    public String getBelongsToUID() {
        return belongsToUID;
    }

    public LocalDateTime getStartDateAndHour() {
        return startDateAndHour;
    }

    public LocalDateTime getEndDateAndHour() {
        return endDateAndHour;
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

    public void setBonusScore(int bonusScore) {
        this.bonusScore = bonusScore;
    }

    public void setBelongsToUID(String belongsToUID) {
        this.belongsToUID = belongsToUID;
    }

    public void setStartDateAndHour(LocalDateTime startDateAndHour) {
        this.startDateAndHour = startDateAndHour;
    }

    public void setEndDateAndHour(LocalDateTime endDateAndHour) {
        this.endDateAndHour = endDateAndHour;
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
}
