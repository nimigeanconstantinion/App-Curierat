package com.model;
import lombok.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Conexiune {
    private String jdbcURL = "jdbc:mysql://localhost:3307/curierat";
    private String userName = "root";
    private String password = "root";
    private Connection connection;
    private Statement statement;

    public Statement getStatement(){
        try{
            connection= DriverManager.getConnection(jdbcURL,userName,password);
            statement=connection.createStatement();
            return statement;
        }catch (Exception e){
            System.out.println("Nu pot returna un Statement!!");
        }
        return null;
    }
}
