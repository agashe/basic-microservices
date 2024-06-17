package com.mycompany.app.Repositories;

import java.sql.*;
import java.util.ArrayList;
import com.mycompany.app.Entities.*;

public class StudentsRepository extends BaseRepository
{
    public StudentsRepository()
    {
        super();
    }

    public Student find(int id) throws SQLException
    {   
        Student student = null;
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery(
            "SELECT * FROM students WHERE id = " + id
        );

        if (results.next()) {
            student = new Student(
                results.getInt("id"),
                results.getString("name"),
                results.getInt("age")
            );
        }

        return student;
    }

    public ArrayList<Student> all() throws SQLException
    {   
        Statement stmt = conn.createStatement();
        ResultSet results = stmt.executeQuery("SELECT * FROM students");
        ArrayList<Student> students = new ArrayList<Student>();

        while (results.next()) {
            students.add(
                new Student(
                    results.getInt("id"),
                    results.getString("name"),
                    results.getInt("age")
                )
            );
        }

        return students;
    }

    public int create(Student student) throws SQLException
    {   
        PreparedStatement stmt = conn.prepareStatement(
            "INSERT INTO students (name, age) VALUES (?, ?)",
            PreparedStatement.RETURN_GENERATED_KEYS
        );

        stmt.setString(1, student.name);
        stmt.setInt(2, student.age);

        stmt.executeUpdate();
        
        int studentId = 0;
        ResultSet result = stmt.getGeneratedKeys();

        if (result.next()) {
            studentId = result.getInt(1);
        }

        return studentId;
    }
    
    public boolean update(int id, Student student) throws SQLException
    {   
        String updateStatement = "UPDATE students SET ";

        if (student.name != "") {
            updateStatement += "name = \"" + student.name + "\"";
        }

        if (student.age != 0) {
            if (student.name != "") {
                updateStatement += " , ";
            }

            updateStatement += "age = " + student.age;
        }

        updateStatement += " WHERE id = " + id;

        PreparedStatement stmt = conn.prepareStatement(
            updateStatement,
            PreparedStatement.RETURN_GENERATED_KEYS
        );

        int result = stmt.executeUpdate();

        return result == 1;
    }
    
    public boolean delete(int id) throws SQLException
    {   
        PreparedStatement stmt = conn.prepareStatement(
            "DELETE FROM students WHERE id = ?",
            PreparedStatement.RETURN_GENERATED_KEYS
        );

        stmt.setInt(1, id);
        int result = stmt.executeUpdate();
        
        return result == 1;
    }
}
