Whiteboarder Server
===================

## How To Run
First, download and install the latest version of [Apache Maven](https://maven.apache.org/download.cgi).

Then, you must build the program (this step will also fetch dependencies):

    ```
    mvn package
    ```

From now on to run, you can do:

1. Navigate to the `/whiteboarder-server` directory and run:

    ```
    mvn exec:java
    ```

2. Use any of the following endpoints to play around locally:

    1. A POST request to `http://localhost:8080/whiteboarder/wb/session` creates a Whiteboarder session and returns the session ID.
    2. A GET request to `http://localhost:8080/whiteboarder/wb/image/{sessionID}` returns the current background image for the specified Whiteboarder session.
    3. A POST request to `http://localhost:8080/whiteboarder/wb/image/{sessionID}` updates the background image of the specified session with the image in the payload.

3. Simply press Enter to tear down the locally running server.

4. To run the tests located in `/whiteboarder-server/src/test/`, from the `/whiteboarder-server` directory, run:

    ```
    mvn test
    ```

5. To clean out the previous build and rebuild, run:

    ```
    mvn clean package
    ```

    Note that this will also run unit tests.

## Notes


The `/whiteboarder-server` directory uses the Maven Project Standard Directory Layout.

`/whiteboarder-server` experimentally makes use of the Jersey RESTful Web Services framework and the Grizzly NIO framework.
