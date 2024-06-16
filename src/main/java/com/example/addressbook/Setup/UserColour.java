package com.example.addressbook.schedule.Setup;

import com.example.addressbook.User;
import javafx.scene.paint.Color;

public class UserColour {
    private User user;
    private String purpose;
    private Color color;
    public UserColour(User user, String purpose, Color color){
        this.user = user;
        this.purpose = purpose;
        this.color = color;
    }
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
