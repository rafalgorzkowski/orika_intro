orika_intro
===========

Orika introduction

-- How to run spring module:
mvn tomcat7:run

--orika-spring:

--- JSON request
http://localhost:8080/orika-intro/createPlan

POST

Accept: application/json
Content-Type: application/json; charset=UTF-8

{
    "productId": "1"
}