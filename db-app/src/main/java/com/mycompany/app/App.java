package com.mycompany.app;

import com.mycompany.app.Controllers.StudentsController;
import com.mycompany.app.Entities.*;
import com.mycompany.app.Messages.StudentMessage;
import com.mycompany.app.Util.MessageQueue;
import com.mycompany.app.Repositories.StudentsRepository;

import java.time.Duration;
import static spark.Spark.*;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class App {
    public static void main(String[] args) {
        StudentsRepository studentsRepository = new StudentsRepository();
        StudentsController studentsController = new StudentsController();

        path("/api/v1", () -> {
            before("/*", (request, response) -> {
                response.status(200);
                response.type("application/json");
            });

            get("/students", studentsController.getAll);
            get("/students/:id", studentsController.get);
            post("/students/", studentsController.create);
            put("/students/:id", studentsController.update);
            delete("/students/:id", studentsController.delete);
        });

        System.out.println("Server is running on port : 4567");

        // Start kafka consumer
        KafkaConsumer<String, String> consumer = MessageQueue.createConsumer();
        KafkaProducer<String, String> producer = MessageQueue.createProducer();

        // poll for new data
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            ObjectMapper objectMapper = new ObjectMapper();

            for (ConsumerRecord<String, String> record : records) {
                StudentMessage studentMessage = null;
                
                try {
                    studentMessage = objectMapper.readValue(
                        record.value(),
                        StudentMessage.class
                    );
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (studentMessage.command.equals("get_student")) {
                    Student parameters = null;
                    Student student = null;
                    String jsonArray = "";

                    try {
                        parameters = objectMapper.treeToValue(
                            studentMessage.payload,
                            Student.class
                        );

                        student = studentsRepository.find(parameters.id);
                        jsonArray = objectMapper.writeValueAsString(student);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ProducerRecord<String, String> studentData = new ProducerRecord<>("students_data", jsonArray);

                    producer.send(studentData);
                } 
                else if (studentMessage.command.equals("get_students")) {
                    ArrayList<Student> students = null;
                    String jsonArray = "";

                    try {
                        students = studentsRepository.all();
                        jsonArray = objectMapper.writeValueAsString(students);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    ProducerRecord<String, String> studentsData = new ProducerRecord<>("students_data", jsonArray);

                    producer.send(studentsData);
                }
                else if (studentMessage.command.equals("create_student")) {                    
                    Student parameters = null;
                    int studentId = 0;

                    try {
                        parameters = objectMapper.treeToValue(
                            studentMessage.payload,
                            Student.class
                        );
                        
                        studentId = studentsRepository.create(parameters);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    ProducerRecord<String, String> studentsData = new ProducerRecord<>(
                        "students_data", 
                        "{\"id\" : \"" + studentId + "\"}"
                    );

                    producer.send(studentsData);
                }
                else if (studentMessage.command.equals("update_student")) {                    
                    Student parameters = null;

                    try {
                        parameters = objectMapper.treeToValue(
                            studentMessage.payload,
                            Student.class
                        );
                        
                        studentsRepository.update(
                            parameters.id,
                            parameters
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    ProducerRecord<String, String> studentsData = new ProducerRecord<>(
                        "students_data", 
                        ""
                    );

                    producer.send(studentsData);
                }
                else if (studentMessage.command.equals("delete_student")) {                    
                    Student parameters = null;

                    try {
                        parameters = objectMapper.treeToValue(
                            studentMessage.payload,
                            Student.class
                        );
                        
                        studentsRepository.delete(
                            parameters.id
                        );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    ProducerRecord<String, String> studentsData = new ProducerRecord<>(
                        "students_data", 
                        ""
                    );

                    producer.send(studentsData);
                }
            }
        }
    }
}
