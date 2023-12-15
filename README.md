
## Requirements
Implement an application for reading Read.me files from every user's (for example Spotify) public GitHub repository and count the 3 most popular words that are longer than 4 letters. 
You can use any framework or trick that you like to make your code shorter and faster, Java is the one limitation.
The application can be CLI or a simple web service, wrapping it to the docker container would be a nice bonus.

## Docker build

```
./mvnw clean package -Dpackaging=docker
```

## Preparation
Specify an own GitHub token in application.properties file (property **github.token**)

## How to use
Make HTTP GET request to **http://localhost:8080/api/v1/statistics/<user_name>**
where <user_name> - GitHub user for searching.

Example:
http://localhost:8080/api/v1/statistics/spotify

