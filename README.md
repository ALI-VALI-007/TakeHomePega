# TakeHomePega

## Requirements:
Build a small service (any language/framework you like) that manages “reading list” items:
    The service should allow you to create, list, update, and delete items.
    Each item should have a title, an author, optional notes, and a “read/unread” status.
    Persist data however you like but explain your choice.
    Add a short README describing:
        How to start and use the service
        If and how you used AI assistance (what it generated, what you changed)

I assume this is a website to keep track of books. The reason I say so is because it says keeping track of a "reading list"

## How To Run:
Running this is pretty simple, please have docker installed. The application will also be hosted and in the email
Go to the primary directory of this project, there will be a docker file here.
Simply run:
> docker compose up --build
This will build the docker image and start everything up.
The link is http://localhost:3000/
You can also access the backend REST API: http://localhost:8080/swagger-ui/index.html
You can run
> docker ps
to see if the instances are properly running (backend and frontend)
and
> docker kill {id}
to end a specific process 
or
> docker compose down
to bring all instances down

## Designing The site:
The site seems pretty straightforward to make
I will be using a Java backend because we talked about the job needing Java
I will also be using React.js for the frontend framework, this is just what I'm most comfortable with
For the database, the data seems very straightforward and structured so I will be using SQLite
I will be hosting it on a lightsail instance and using Cognito for user auth

## The Database:
The database will have a table for:

Books:
    BookId
    Title
    Author
    Notes

User Books:
    UserId
    BookId
    Status

AWS Cognito will handle user authentication so no need to store passwords or anything

The database was designed this way since in case I need to add features like browsing other books, seeing what other people are reading, etc.
If I wanted to add that functionality I can easily add a *User* table and have a field like visibility, or other things like that.

I chose Sqlite since it is very lightweight and easy to work with. I also have previous experience with it so it made it significantly easier for me.
The application scope is small and the user count will just be a few Pega employees. 

## The Backend
For the backend I used Java, specifically Spring Boot. The reason I chose Java was because I would be working full time in Java at this role so it was important I get used to it again. The reason I used Spring Boot was because I am used to making applications for school in ASP.NET with C#. The design paradigms are similar. However I primarily used a MVC style, Spring Boot has additional layers. The *Repository* and *Service* are the most notable. The Repository layer is primarily for interacting with the database. I am used to creating stored procedures but SQLite doesn't allow you to do that so I used the JPA to create standardized sql statements that way. The Service layer primarily interacts with the Controller and Repository layer, it takes away a lot of weight that the model class has to deal with in standard MVC style in C#. There is also a DTO which is the data structure for the controller to receive and send. The Service layer acts as a middle ground between Models and DTO's. 

This was my first time using Spring Boot and since this position did include coding in Java I coded it all by hand. However I did use AI for reading debugging logs from the 
> docker compose logs backend
to see what sql queries were being made by the JPA. The primary issue I had was the composite key created duplicate fields. book_id was the correct name for the field but the JPA kept adding BookId as a new field which altered the wanted result of the database. 

## The Frontend
The frontend was done in React. I used React for my web development classes and out of school projects as well. The code was primarily written by AI, but I did some bug fixes here and there however nothing major. I primarily directed the AI into what to do like make a router, make a login page using AWS Cognito, Make a sign up page, there needs to be a email verification, etc. I didn't see any front end work on the job posting nor remember hearing about it so I felt free to use AI for this segment.

I did modify the frontend so it would do async calls to the backend and handle the frontend by itself since otherwise it would refresh the page. Other than that I would say 75% of the code is AI generated.