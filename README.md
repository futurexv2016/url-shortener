# Bondle shorten url
## Requirements:
The user wants a URL shortener application. They should be able to provide a url and get a
shortened url for it. When a shortened url is provided, the user should be redirected to the
original url.
1. Create a simple web application that  
    a. Uses Springboot framework  
    b. Presents a web interface to user
2. User should be able to  
a. Submit a URL via form  
b. Response should be shortened URL
3. User should be able to  
a. Submit a shortened URL via form  
b. Get redirected to original URL
4. Ther User should be able to see a list of previous shortened URLs
5. BONUS: Front end communicates to backend via REST API
6. Search box to search for shortened URL and display result in the table

Note:
The requirements does not require search feature (6). However, given very long URL summary table, user cannot search for shortened URL.
Hence, I added search feature allowing users to enter shortURL key (the part after domain) and get the requested record in the table.

## Implementation
This app consists of frontend and backend which are in different code bases and highly recommend to deploy them in different servers/environments for security, scalability.
### Backend:
#### Features:
- Restful API is implemented in API first approach [https://swagger.io/resources/articles/adopting-an-api-first-approach/](https://swagger.io/resources/articles/adopting-an-api-first-approach/)
- For each request comes, interceptors are implemented to log request and response for debugging, tracing purposes
- Spring Sleuth is used to assign every step of the request an ID so that we can trace all steps of a request in different layers via ```grep id``` command in log. 
- Caffeine cache is implemented for URL read-only operations to improve performance. It is now memory cache. However, if necessary can be replaced by different cache such as AWS elastic cache later
- API is described via Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Unit test is written and Jacoco is used to measure test coverage report

#### Deployment environment
- Docker-Compose: 1.23.1
- Docker CE: 19.03.2
- Maven Wrapper is used so no need to install Maven.
- Java is embedded in Docker image so no need to install java.  

Link Backend after deployment: [http://localhost:8080](http://localhost:8080)  
Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
##### 1. Start app:
    cd deploy && start.sh
##### 2. Stop app:
    cd deploy && stop.sh

### Frontend:
#### Features:
- Search feature is implemented to search for short URL  
- For simplicity, Frontend is implemented by React Hook. In case of more sophisticated app, Redux Saga can also be used.
- For time constraint, unit test in Frontend is ignored 
#### Deployment environment
- Npm: 7.5.4
- Node: v14.15.1
- Docker: 20.10.5
- Docker-Compose: 1.23.1
##### 1. Start app:
    cd deploy && ./start.sh
##### 2. Stop app:
    cd deploy && ./stop.sh

Link Frontend after deployment: [http://localhost](http://localhost)
### Deploy both Frontend and Backend
    cd deploy && ./startAll.sh

Link app after deployment: [http://localhost](http://localhost)
