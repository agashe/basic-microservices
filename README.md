# Basic Kafka Setup

This project is a simple example for microservices , we have 2 main services , the first one is the `db-app` written in Java and it's connected to MySQL database , it provides basic CRUD operations and also an APIs interface using Spark framework. On the other side we have the `client-app` service. It's also provides an APIs interface using Fastify , but instead of having it's own database , it depends heavily on Kafka to fetch the data.

This example is very basic and have a lot of issues , so more work will be done in the future. Including : <br>

* Adding validation for the APIs
* Add the database to the Docker Compose file
* Make each service has it's own database  
* Replace Fastify with Fastify-Kafka , since the current <br>
  implementation is very slow
* Add more services 
* Create APIs Gateway
* Implement basic load balancer

## Documentation

In the following sections , you will find all the necessary instructions in order to run this project.

### Starting Kafka

The first piece of software we need to run in this project is Kafka , you could have Kafka installed already so make sure it's running on port `9092` or feel free to update the `db-app` and the `client-app` to listen to your current Kafka's port.

In case you don't have Kafka installed on your machine , we have a docker compose file in this project , that will run Kafka , in your terminal run the following command (in the root of the project directory !):

```
docker-compose -f compose.yaml up -d
```

### Creating Kafka Topics

Once the docker-compose is done , now we create 2 Kafka topics `students_commands` and `students_data` , this architecture might not be the best and of course in future might be replaced with something bestter.

Fo now the producer will send a message with the operation to perform using the `students_commands` topic , then the consumer will send the response on `students_data`.

To create a new topic using kafka , we need first to access the Kafka container using bash , so we run the following command , to get the `CONTAINER ID` :

```
docker ps
```

Once you get the `CONTAINER ID` , we can now access the container shell using the following command :

```
docker exec -it PUT_CONTAINER_ID_HERE bash
```

After we access the container's shell we run 2 more commands to create the topics :

```
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic students_commands
```

```
kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic students_data
```

And now we should have our topics ready for use !

### The Database

In this project we use s single MySQL database for the `db-app` , and unfortunately it's not included in the docker-compose file (will be added in the future). So make sure you have MySQL installed on your machine.

In the `db-app` directory , you will find a `.env` which includes the database connection's credentials , so make sure to update those with your database ones.

Finally in your database import / run the migration and seeder SQL files in order to create the table and seed the it.

We have 2 files in the `database` directory , we can run them as following in your terminal :

```
cd database/

mysql -u YOUR_USER -p DATABASE_NAME < migrations.sql

mysql -u YOUR_USER -p DATABASE_NAME < seeder.sql
```

### Running the `db-app`

Make sure That Java(+17) and Maven are installed on your machine , then the run the following command in your terminal to install the dependencies:

```
cd db-app/

mvn package
```

Then run the service using :

```
mvn exec:java -Dexec.mainClass="com.mycompany.app.App"
```

### Running the `client-app`

The `client-app` is Node.js application , we start it as following :

```
cd client-app/

npm install

npm start
```

### Using the APIs

Both services provide simple APIs with with no authentication , in your API-Client of choice (Postman / Insomnia/ Thunder-Client / CURL) send any of the following requests :  

`db-app` port : 4567 <br>
`client-app` port : 3000 

1- Get all students

```
GET http://localhost:PORT/api/v1/students
```

2- Get single student by id

```
GET http://localhost:PORT/api/v1/students/20
```

3- Create new student

```
POST http://localhost:PORT/api/v1/students

JSON Body :

{
  "name": "Omar",
  "age": 15
}
```

4- Update student by id

```
PUT http://localhost:PORT/api/v1/students/20

JSON Body :

{
  "name": "Ali",
  "age": 17
}
```

5- Delete student by id

```
DELETE http://localhost:PORT/api/v1/students/20
```
