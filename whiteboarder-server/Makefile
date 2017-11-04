.PHONY: build
build:
	mvn package -DskipTests

.PHONY: clean
clean:
	mvn clean
	rm -v -f whiteboarder-server.log*

.PHONY: test
test:
	mvn test

.PHONY: run
run:
	mvn exec:java
