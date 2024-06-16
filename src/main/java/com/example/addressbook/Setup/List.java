package com.example.addressbook.schedule.Setup;

import com.example.addressbook.DashboardController;
import com.example.addressbook.IUserDAO;
import com.example.addressbook.User;

public class List {
    String[] listOfDay = {"Sunday","Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private IUserDAO scheduleInterface;
    java.util.Date date = new java.util.Date();
    private final String currentDate = listOfDay[date.getDay()];
    private final int currentHour = date.getHours();
    private final User currentUser = DashboardController.currentUser;
    private final String currentPurpose = scheduleInterface.getPurpose(currentUser,currentDate,currentHour);

    Date CurrentTime = new Date(currentDate,currentHour,currentPurpose);
}
