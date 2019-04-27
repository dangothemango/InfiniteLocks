package com.dangorman.infinitelocks.core;

import com.dangorman.infinitelocks.db.DatabaseModule;
import groovy.sql.GroovyRowResult;

import javax.xml.ws.http.HTTPException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User {

    public String username;
    public String firstName;
    public String lastName;
    public int numPuzzlesSolved;
    public boolean completedTutorial;


    public User(String username){
        this.username = username;
        GroovyRowResult userRow;
        try {
            userRow = DatabaseModule.getDbConnection().rows(
                    String.format("Select * from users where username = '%s' limit 1",username)
            ).get(0);
            this.fromRow(userRow);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new HTTPException(500);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void fromRow(GroovyRowResult userRow){
        this.firstName = (String)userRow.get("first_name");
        this.lastName = (String)userRow.get("last_name");
        this.numPuzzlesSolved = (int)userRow.get("puzzles_solved");
        this.completedTutorial = (Boolean)userRow.get("hascompletedtutorial");
    }

    public List<String> getAvailableLocks(){
        List<GroovyRowResult> puzzles;
        try {
            puzzles = DatabaseModule.getDbConnection().rows("Select distinct name from locks");
        } catch (SQLException e) {
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
