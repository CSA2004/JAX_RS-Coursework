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


Q3 answer:

This represents a trade-off between bandwidth efficiency and API chattiness:

Returning only IDs: Minimizes initial payload size (saving bandwidth), which is ideal for mobile users. However, it forces the client to make "N" additional API calls to fetch details for each room. This increases network latency and server overhead due to multiple HTTP handshakes.

Returning full objects: Increases the initial response size but allows the client to render all data in a single round-trip. This simplifies client-side processing as the developer doesn't need to manage complex data aggregation from multiple endpoints.

I chose to return full objects to prioritize a "chunky" design over a "chatty" one, ensuring a faster, more responsive user interface.


Q4 answer:

Yes, the DELETE operation is idempotent.

In this implementation, idempotency is achieved because the end state of the server remains the same regardless of how many times the request is sent.

First Request: The server finds the room, removes it, and returns a 204 No Content (or 200 OK) status.

Subsequent Requests: If the client sends the exact same request again, the server looks for the room, finds it no longer exists, and returns a 404 Not Found.

Although the status code changes from 204 to 404, the state of the resource on the server does not change after the first successful deletion; the room remains gone. Since the side effects are the same for one or many requests, the operation is technically idempotent.


Q5 answer:

The @Consumes annotation acts as a strict filter for the Content-Type header of incoming requests. If a client sends data in an unsupported format like text/plain or application/xml:

Technical Consequence: The JAX-RS runtime will refuse to process the request because it cannot find a matching resource method that is capable of de-serializing that specific media type into a Java object.

Handling the Mismatch: JAX-RS handles this automatically by halting the request execution before it reaches your method logic. It will return an HTTP 415 Unsupported Media Type error response to the client.

This mechanism ensures that the API only accepts data it is programmed to understand, protecting the application from processing incompatible or malformed data structures.


Q6 answer:

Using Query Parameters (?type=CO2) is generally superior to using Path Parameters for filtering collections because it adheres to the core principles of RESTful resource modeling:

Resource vs. Attribute: In REST, the URL path should represent a specific resource (the collection of sensors), while query parameters represent attributes used to sort, filter, or search that collection. Using a path for a filter (e.g., /type/CO2) creates a "false hierarchy" that suggests the filter is a resource itself.

Flexibility and Scalability: Query parameters allow for combining multiple filters easily (e.g., ?type=CO2&status=ACTIVE&min=20). Implementing this with path parameters would result in an explosion of complex, rigid URL combinations that are difficult for the server to route and for the client to construct.

Optionality: Query parameters are inherently optional. A client can hit /sensors to get everything, or add a parameter to narrow the results. Path parameters are typically mandatory, making it harder to provide a clean, single entry point for a collection.

By using @QueryParam, the API remains flexible, allowing users to build complex searches without breaking the logical structure of the URL.



