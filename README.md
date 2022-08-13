# Goddard Ordering Management System

### Introduction

This is a food ordering platform application, and it contains two ends: web end for admin purposes (such as launching new dishes, managing employees, 
managing incoming orders, etc.) and mobile end for customers (allowing actions such as adding dish to shopping cart, placing orders, etc.). 
This application is developed based on Spring Boot, MyBatis Plus for the back end and VUE (element UI) for the front end.

### Project Purpose

I myself focused on the back end development with Spring and how it gets and sends data from and to the frontend.
**I demonstrate my experience on Spring Boot, Spring MVC and MyBatis Plus, and also my understanding of advanced designs of full stack application,
such as how ad why to incorporate DAOs, how to decouple components in the app, and the benefits of different persistence layer frameworks/technologies (Hibernate, MyBatis, MyBatis Plus, etc.).**
I'm also involved in developing a few parts of the front end.


### Technologies Used

- Spring {Boot, MVC, Cache}
- MyBatis Plus
- Redis
- MySQL
- VUE (Element UI)

### Benefits of MyBatis Plus

MyBatis Plus is an advanced version of MyBatis. Unlike MyBatis, MyBatis Plus comes with plenty of mappers that one can use out-of-box. All I need to do is to extend the `BaseMapper` interface,
and I can use all these methods which saves tons of time writing SQL statements. In addition, when there's no suitable built-in method satisfying my need, I can still handwrite the method in the service layer, and often times the built-in ones can still help me with that.

It also takes over the heavy-lifting when it comes to create paginated query lists. All I need to do is to create a simple method for pagination
in the controllers with the help of the built-in class `Page`. Furthermore, MyBatis Plus supports all common databases, including H2, MySQL, PostgreSQL, etc.


### Instruction for Use

Please see the demo running on AWS EC2 server
