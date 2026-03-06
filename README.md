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

## Desining The site:
The site seems pretty straightforward to make
I will be using a Java backend because we talked about the job needing Java
I will also be using React.js for the frontend framework, this is just what I'm most comfortable with
For the database, the data seems very straightforward and structured so I will be using SQLite
I will be hosting it on a lightsail instance and using Cognito for user auth. I primarily did this to show I can use AWS proficiently

## The Database:
The database will have a table for:

The Book:
    BookId
    Title
    Author
    Notes

Unread Books:
    UserId
    BookId
    Status

AWS Cognito will handle user authentication