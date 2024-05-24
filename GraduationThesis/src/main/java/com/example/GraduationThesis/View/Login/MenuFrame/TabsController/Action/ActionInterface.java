package com.example.GraduationThesis.View.Login.MenuFrame.TabsController.Action;

import com.example.GraduationThesis.View.Login.MenuFrame.TabsController.DecoratorButton.ActionType;

import javax.swing.*;

public interface ActionInterface {

    /**
     * Generics
     * numberphone (String) and ID (Long)
     * this action for tab general information - tab posistion - tab conduct - tab usersdata
     * it will delete record on table of java swing and record in mysql database
     */
    <T> void delete(T value, ActionType actionType);


    /**
     * this action for tab scores
     * it will edit the value columns 3 4 5 6 7 to "" and edit too in mysql database
     */
    <T> void deleteTabScores(T value, JTable table, int selectedRow);

    /**
     * this action for tab conduct
     * it will edit the value columns 3 4 5 to "" and edit too in mysql database
     */
    <T> void deleteTabConduct(T value, JTable table, int selectedRow);

    /**
     * admin authorization with number phone by user has role admin
     */
    void adminAuthorization(String numberPhone);


}
