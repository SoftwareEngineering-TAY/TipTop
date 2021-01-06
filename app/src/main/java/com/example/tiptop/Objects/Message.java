package com.example.tiptop.Objects;

import java.io.Serializable;

public class Message implements Serializable {
    public String sender;
//    public String receiver;
    public String senderUid;
//    public String receiverUid;
    public String message;
    public long timestamp;

    public Message(){

    }

    public Message(String sender, String senderUid, String message, long timestamp) {
        this.sender = sender;
//        this.receiver = receiver;
        this.senderUid = senderUid;
//        this.receiverUid = receiverUid;
        this.message = message;
        this.timestamp = timestamp;
    }
}