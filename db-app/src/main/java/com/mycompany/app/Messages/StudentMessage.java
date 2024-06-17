package com.mycompany.app.Messages;

import com.fasterxml.jackson.databind.JsonNode;

public class StudentMessage extends BaseMessage
{
    public String command;
    public JsonNode payload;
    
    public StudentMessage()
    {}
}
