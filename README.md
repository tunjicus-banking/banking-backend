# Banking Backend

## How To Run

### Starting the Database

Run `docker-compose up` in the `docker` folder. This will create the database and some sample data

### Starting the Application

This project uses Java 16, with the newest feature used being Records. Records were added, in preview, 
in Java 14 so Java 14 might work, but the best bet is to use 16 or newer. 

The build tool for this project is Maven. There is a Maven wrapper in the repo, so you don't need to have
it installed to start. `mvnw.cmd` is for Windows `mvnw` is for everyone else. You can build
the project by running `./mvnw package` and then run the application by running `./mvnw spring-boot:run`

By default, the application runs on port `8080`. You change it by changing the `server.port`
property [here](/src/main/resources/application.properties).

## A Note On Simulated Time

The application uses a simulated timescale to accelerate events. The
degree of scaling is subject to change, but the implementation will likely
remain the same. 

Any timestamps returned from the api are simulated dates.
These dates grow backwards, away from the current time. That means
that a timestamp might show that an event happened in 2020 even though
it was created in the current year. This makes timestamps returned from
the api unusable for any purpose other than displaying how "long ago" an
event occurred. These simulated timestamps should not be displayed directly
as it will just lead to confusion.