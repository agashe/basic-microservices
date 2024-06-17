package com.mycompany.app.Controllers;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.app.Entities.Student;
import com.mycompany.app.Repositories.StudentsRepository;
import spark.*;

public class StudentsController 
{
    private StudentsRepository studentsRepository;
    private ObjectMapper objectMapper;

    public StudentsController() {
        this.studentsRepository = new StudentsRepository();
        this.objectMapper = new ObjectMapper();
    }

    public Route get = (Request request, Response response) -> {                    
        Student student = null;
        String jsonArray = "";
        
        try {
            student = this.studentsRepository.find(
                Integer.parseInt(request.params("id"))
            );

            jsonArray = objectMapper.writeValueAsString(student); 
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
        }

        return jsonArray;
    };

    public Route getAll = (Request request, Response response) -> {                    
        ArrayList<Student> students = null;
        String jsonArray = "";
        
        try {
            students = this.studentsRepository.all();
            jsonArray = this.objectMapper.writeValueAsString(students); 
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
        }

        return jsonArray;
    };

    public Route create = (Request request, Response response) -> {                    
        Student student = null;
        
        try {
            student = this.objectMapper.readValue(
                request.body(), 
                Student.class
            );    
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
        }
        
        int studentId = 0;

        try {
            studentId = this.studentsRepository.create(student);
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
        }
        
        return "{\"id\" : \"" + studentId + "\"}";
    };

    public Route update = (Request request, Response response) -> {                    
        Student student = null;
        
        try {
            student = this.objectMapper.readValue(
                request.body(), 
                Student.class
            );    
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
        }
        
        if (request.params("id") == "") {
            response.status(400);
            return "{\"error\" : \"Invalid Id !\"}";
        }

        try {
            this.studentsRepository.update(
                Integer.parseInt(request.params("id")),
                student
            );
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
        }
        
        return response;
    };

    public Route delete = (Request request, Response response) -> {                    
        if (request.params("id") == "") {
            response.status(400);
            return "{\"error\" : \"Invalid Id !\"}";
        }

        try {
            this.studentsRepository.delete(
                Integer.parseInt(request.params("id"))
            );
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
        }
        
        return response;
    };
}
