Question Answers:


Q1 answer:
    By default, JAX-RS resource classes are request-scoped. This means a new instance of the class is created for every incoming HTTP request and destroyed once the response is sent. Because the resource instances are short-lived, we cannot store data within standard instance variables. To prevent data loss, I implemented a centralized DataStore using static members. Furthermore, since multiple requests can occur simultaneously (concurrency), I used thread-safe structures like ConcurrentHashMap to prevent race conditions and ensure data consistency.