package com.example.addressbook;

import java.util.List;

public interface IGoalDAO {
    void createGoalsTable();
    void addGoal(Goal goal, User user);
    void updateGoal(Goal goal);
    void DeleteGoal(Goal goal);
    Goal GetGoal(int id);
    List<Goal> fetchGoalsForUser(User user); // Add this method to the interface
}
