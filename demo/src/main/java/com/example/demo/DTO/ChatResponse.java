package com.example.demo.DTO;

public  class ChatResponse {
    private String answer;
    public ChatResponse() {}
    public ChatResponse(String answer) { this.answer = answer; }
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}