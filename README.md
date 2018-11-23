# TimeUtopia
Its a web based recommendation system which gives details of feasible events when the user provides location and time.

# Motivation
The project was created during a 24 hour hackathon at ASU, SunHacks.

# Technologies
We have used spring boot framework for creating REST API's and used react with spring to render client interfaces.

# External API's
Discovery API from ticketmaster, Google API

# Authentication & Authorization
JWT based Authenthication build upon Spring Security

# Recommendation basis
There are two ideas based on which we are recommending the events. 
1. Whether the event is feasible by checking the time slot user entered with the event time and travel time.
2. Using cosine similarity to predict weigted ratings for events.

# How to Run ?
PreRequistes - Java and an IDE like IntellIj.
Setup mongodb.
Run MLTrainer.java to create the predictions based on history data.
Run Application.java and go to localhost:8080 on the browser to test the app.
