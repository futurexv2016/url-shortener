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

Note:
The requirements does not require search box for URL table. However, without it, user cannot search for shortened URL.
Hence, I added search feature allowing users to enter 

##Implementation
This app consists of frontend and backend which is in different code base and highly recommend to them in different environments for security, scalability.
### Backend:
#### Features:
- Restful API is implemented in API first approach [https://swagger.io/resources/articles/adopting-an-api-first-approach/](https://swagger.io/resources/articles/adopting-an-api-first-approach/)
- For each request comes, interceptors are implemented to log request and response for debugging, tracing purposes
- Spring Sleuth is used to assign each step within a request an ID so that we can trace all steps of a request via ```grep``` command in log. 
- Caffeine cache is implemented for URL read-only operations to improve performance. It is now memory cache. However, if necessary can be replaced by different cache such as AWS elastic cache later
- API is described via Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- App is using Jacoco to create test coverage report

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
- Search feature, outside the requirements, is implemented to search for short URL  
- For simplicity, FE is implemented by React Hook. In case of more sophisticated app, Redux Saga can be used.
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
