package com.example.GraduationThesis.Service.LazySingleton.ListRoles;

import java.util.ArrayList;
import java.util.List;

public class ListRolesManager {
    private static ListRolesManager instance;
    private List<String> roles;

    private ListRolesManager() {
        roles = new ArrayList<>();
    }

    public static synchronized ListRolesManager getInstance() {
        if (instance == null) {
            instance = new ListRolesManager();
        }
        return instance;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void delete() {
        this.roles.clear();
    }
}