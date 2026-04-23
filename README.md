**API Design Overview**

This RESTful Web API manages a Smart Campus ecosystem. It uses Java EE and Jersey (JAX-RS) to handle resources including campus rooms, environmental sensors, and historical data logs.

Service Discovery: A versioned entry point at /api/v1.

Resource Modeling: Logic for creating, retrieving, and deleting Rooms and Sensors.

Deep Nesting: Implementation of the Sub-Resource Locator Pattern to manage historical sensor readings.

Advanced Error Handling: Custom Exception Mappers to ensure the API is "leak-proof"


**Build & Launch Instructions**

Follow these steps to build and deploy the project locally:

Clone the Repository:

git clone [Your-Repo-URL]

Navigate to the project root and run:

mvn clean install

Deployment:

Copy the resulting .war file from the /target directory.

Deploy the file to your Apache Tomcat 9.0+ webapps folder.

Access the API:
The API will be available at http://localhost:8080/Coursework_w2086242/api/v1

**Sample curl Commands**

These commands demonstrate successful interactions with different parts of the API:

Service Discovery:
curl -X GET http://localhost:8080/Coursework_w2086242/api/v1

Create a Room:
curl -X POST -H "Content-Type: application/json" -d '{"id":"LIB-301","name":"Library","capacity":50}' http://localhost:8080/Coursework_w2086242/api/v1/rooms

Register a Sensor:
curl -X POST -H "Content-Type: application/json" -d '{"id":"T1","type":"Temperature","roomId":"LIB-301"}' http://localhost:8080/Coursework_w2086242/api/v1/sensors

Add a Sensor Reading:
curl -X POST -H "Content-Type: application/json" -d '{"id":"R101","value":22.5}' http://localhost:8080/Coursework_w2086242/api/v1/sensors/T1/read

Retrieve Sensor History:
curl -X GET http://localhost:8080/Coursework_w2086242/api/v1/sensors/T1/read

**Question Answers:**


Q1 answer:

By default, JAX-RS resource classes are request-scoped. This means a new instance of the class is created for every incoming HTTP request and destroyed once the response is sent. Because the resource instances are short-lived, we cannot store data within standard instance variables. To prevent data loss, I implemented a centralized DataStore using static members. Furthermore, since multiple requests can occur simultaneously (concurrency), I used thread-safe structures like ConcurrentHashMap to prevent race conditions and ensure data consistency.

Q2 answer:

Hypermedia, or HATEOAS (Hypermedia As The Engine Of Application State), is considered a hallmark of advanced REST design because it makes the API self-discoverable. Instead of a client having to hardcode every possible URL, the server provides "navigational clues" (links) within the JSON response that tell the client what they can do next based on the current state of the resource.

Benefits to Client Developers:

Reduced Coupling: Developers don't need to hardcode complex URL patterns (like /api/v1/sensors/123/readings/456). They can simply follow the link provided in the previous response.

Real-time Guidance: Unlike static documentation, which can become outdated, hypermedia links are dynamic. If a resource's state changes (e.g., a sensor is deleted), the server simply stops providing the "delete" or "update" link, informing the client of available actions in real-time.

Easier Versioning: The server can change URL structures behind the scenes without breaking the client, as long as the "relation" names (the keys for the links) remain the same.


