package com.example.tiptop.Objects;

import java.io.Serializable;

public class Message implements Serializable {

    //Fields
    public String sender;
    public String senderUid;
    public String message;
    public long timestamp;

    //Default constructor
    public Message(){

    }

    //Parameter constructor
    public Message(String sender, String senderUid, String message, long timestamp) {
        this.sender = sender;
        this.senderUid = senderUid;
        this.message = message;
        this.timestamp = timestamp;
    }
}