Whiteboarder Server
===================

## How To Run
First, download and install the latest version of [Apache Maven](https://maven.apache.org/download.cgi).

To build:

```
mvn package
```

To run:

```
mvn exec:java
```

Press Enter to tear down the server.

### Try The Routes

A good tool for testing the server is
[Advanced REST Client](https://www.advancedrestclient.com/).
Try out these routes:

1. A POST request to `http://localhost:8080/whiteboarder/wb/session` creates
   a Whiteboarder session and returns the session ID.
2. A GET request to `http://localhost:8080/whiteboarder/wb/image/{sessionID}`
   returns the current background image for the specified Whiteboarder session.
3. A POST request to `http://localhost:8080/whiteboarder/wb/image/{sessionID}`
   updates the background image of the specified session with the image in the
   payload.

To run the tests in `/whiteboarder-server/src/test/`, cd to
`/whiteboarder-server` and run:

```
mvn test
```

To clean out the previous build and rebuild, run:

```
mvn clean package
```

Note: this will also run unit tests.

## Notes


The `/whiteboarder-server` directory uses the Maven Project Standard Directory Layout.

`/whiteboarder-server` experimentally makes use of the Jersey RESTful Web
Services framework and the Grizzly NIO framework.
