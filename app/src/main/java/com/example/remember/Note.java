package com.example.remember;

public class Note {
    String text;
    String tittle;
    public Note(String title, String text) {
        this.text = text;
        this.tittle = title;
    }

    public String getText() {
        return text;
    }
    public String getTittle() {
        return tittle;
    }

    public void setText(String text) {
        this.text = text;
    }
    public void setTittle(String tittle) {
        this.tittle = tittle;
    }
}
