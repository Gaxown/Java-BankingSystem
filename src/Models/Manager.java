package Models;

import enums.Role;

import java.util.ArrayList;
import java.util.List;

public class Manager extends User {
    private String departement;


    public Manager(String firstName, String lastName, String email, String password, String department) {
        super(firstName, lastName, email, password, enums.Role.MANAGER);
        this.departement = department;
    }

    @Override
    public enums.Role getRole() {
        return enums.Role.MANAGER;
    }



}
