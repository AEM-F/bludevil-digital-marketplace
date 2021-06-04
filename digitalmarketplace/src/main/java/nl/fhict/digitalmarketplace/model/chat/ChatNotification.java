package nl.fhict.digitalmarketplace.model.chat;

public class ChatNotification {
    private Integer messageId;
    private Integer senderId;
    private String senderName;

    public ChatNotification() {
    }

    public ChatNotification(Integer messageId, Integer senderId, String senderName) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.senderName = senderName;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
