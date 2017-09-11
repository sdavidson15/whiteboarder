Whiteboarder Server
===================

## How To Run
First, download and install the latest version of [Apache Maven](https://maven.apache.org/download.cgi).

1. Navigate to the `/whiteboarder-server` directory and run:

    ```
    mvn exec:java
    ```

2. Use any of the following endpoints to play around:

    1. A GET request to `http://localhost:8080/whiteboarder/myresource` returns the plain text "Got it!"
    2. A GET request to `http://localhost:8080/whiteboarder/wb/hello-world` returns "Hello, world!".
    3. A POST request with some JSON formatted data sent to `http://localhost:8080/whiteboarder/wb/hello-world` returns your data.
    4. A GET request to `http://localhost:8080/whiteboarder/wb/{text}` returns whatever text you entered into the text parameter.

3. Simply press Enter to tear down the locally running server.

4. To run the tests located in `/whiteboarder-server/src/test/`, from the `/whiteboarder-server` directory, run:

    ```
    mvn clean package
    ```

## Notes


The `/whiteboarder-server` directory uses the Maven Project Standard Directory Layout.

`/whiteboarder-server` experimentally makes use of the Jersey RESTful Web Services framework and the Grizzly NIO framework.