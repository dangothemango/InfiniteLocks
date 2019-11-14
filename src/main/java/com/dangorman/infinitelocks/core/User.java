package com.dangorman.infinitelocks.core;

import com.dangorman.infinitelocks.db.DatabaseModule;
import groovy.sql.GroovyRowResult;

import javax.xml.crypto.Data;
import javax.xml.ws.http.HTTPException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User {

    private static final String GET_USERS_SQL = "Select * from users where username = ? limit 1";
    private static final String GET_LOCKS_SQL = "Select distinct name from locks";
    public String username;
    public String firstName;
    public String lastName;
    public int numPuzzlesSolved;
    public boolean completedTutorial;


    public User(String username){
        try {
            GroovyRowResult userRow;
            userRow = DatabaseModule.rows(GET_USERS_SQL, username).get(0);
            this.fromRow(userRow);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new HTTPException(500);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void fromRow(GroovyRowResult userRow){
        this.username = (String)userRow.get("username");
        this.firstName = (String)userRow.get("first_name");
        this.lastName = (String)userRow.get("last_name");
        this.numPuzzlesSolved = (int)userRow.get("puzzles_solved");
        this.completedTutorial = (Boolean)userRow.get("hascompletedtutorial");
    }

    public List<String> getAvailableLocks(){
        List<GroovyRowResult> puzzles;
        try {
            puzzles = DatabaseModule.rows(GET_LOCKS_SQL);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new HTTPException(500);
        }
        List<String> validPuzzles = new ArrayList<>();
        for (GroovyRowResult puzzle: puzzles) {
            validPuzzles.add((String)puzzle.get("name"));
        }

        return validPuzzles;
    }

    public Boolean hasCompletedTutorial() {
        return this.completedTutorial;
    }

}
