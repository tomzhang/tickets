/**
  * Copyright 2018 bejson.com 
  */
package com.tickets.tickets.domain.price;
import java.util.List;

/**
 * Auto-generated: 2018-09-30 8:55:45
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class TicketPriceVo {

    private String validateMessagesShowId;
    private boolean status;
    private int httpstatus;
    private Data data;
    private List<String> messages;
    private ValidateMessages validateMessages;
    public void setValidateMessagesShowId(String validateMessagesShowId) {
         this.validateMessagesShowId = validateMessagesShowId;
     }
     public String getValidateMessagesShowId() {
         return validateMessagesShowId;
     }

    public void setStatus(boolean status) {
         this.status = status;
     }
     public boolean getStatus() {
         return status;
     }

    public void setHttpstatus(int httpstatus) {
         this.httpstatus = httpstatus;
     }
     public int getHttpstatus() {
         return httpstatus;
     }

    public void setData(Data data) {
         this.data = data;
     }
     public Data getData() {
         return data;
     }

    public void setMessages(List<String> messages) {
         this.messages = messages;
     }
     public List<String> getMessages() {
         return messages;
     }

    public void setValidateMessages(ValidateMessages validateMessages) {
         this.validateMessages = validateMessages;
     }
     public ValidateMessages getValidateMessages() {
         return validateMessages;
     }

}
