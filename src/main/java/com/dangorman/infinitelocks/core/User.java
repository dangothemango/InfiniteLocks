package com.dangorman.infinitelocks.core;

import com.dangorman.infinitelocks.db.DatabaseModule;
import groovy.sql.GroovyRowResult;

import javax.xml.ws.http.HTTPException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User {


    public List<String> getAvailableLocks(){
        List<GroovyRowResult> puzzles;
        try {
            puzzles = DatabaseModule.getDbConnection().rows("Select distinct name from locks");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            //TODO make all 404s better --idea Secret locks
            throw new HTTPException(404);
        }
        List<String> validPuzzles = new ArrayList<>();
        for (GroovyRowResult puzzle: puzzles) {
            validPuzzles.add((String)puzzle.get("name"));
        }

        return validPuzzles;
    }

}
