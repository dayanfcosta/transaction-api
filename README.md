# Transaction API

## Task

#### Explicity requirements:

Design and implement a RESTful API (including data model and the backing implementation) for
money transfers between accounts.
Explicit requirements:
1. You can use Java or Kotlin.
2. Keep it simple and to the point (e.g. no need to implement any authentication).
3. Assume the API is invoked by multiple systems and services on behalf of end users.
4. You can use frameworks/libraries if you like (**except Spring**), but don't forget about
requirement #2 and keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test.
6. The final result should be executable as a standalone program (should not require a
pre-installed container/server).
7. Demonstrate with tests that the API works as expected.

#### Implicit requirements:
1. The code produced by you is expected to be of high quality.
2. There are no detailed requirements, use common sense.

# Tech Stack
- Java 11
- Micronaut (lightway DI AOT)
- Maven
- JUnit 5
- AssertJ
- A in-memory DB hand-writter for better control of concurrency

## Runing the application

#### Requirements
- JDK 11 or newer

#### Running
- Download the source code
- In root folder, execute "**java -jar api.jar**"
- The app is hosted on port 8888
- To see all endpoints and the API documentation go to: **http://localhost:8888/api**
