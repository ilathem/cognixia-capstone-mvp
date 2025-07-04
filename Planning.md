# Planning

This document is for recording my plans for this project.

- Tracker will be for philosophy books

## Architecture

- Will use MVC 
- Model: DAO, Model Classes, and SQL
- View: View class that displays text to the user and collects input, sends data to controller via JSON
- Controller: Controller class that takes data from the View, parses it, and calls methods from the DAO

## SQL Planning

- Need at least 2 tables (user and tracker)
- User table
  - PK: user_id 
  - Name
  - Password
  - Clearance: 
    - level 0: only access to their own tracker
    - level 1: can change book table (admin)
- Book table: stores all the books available to track
  - PK: book_id
  - Name
  - Author
  - Number of pages
- Tracker table: maps user to book with progress
  - FK user_id
  - FK book_id
  - Progress
  - Rating

## Custom Exceptions

- BookNotAvailable
- UserNotFound