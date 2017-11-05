.PHONY: build
build:
	mvn package -DskipTests

.PHONY: run
run: build
	mvn exec:java

.PHONY: clean
clean:
	mvn clean

.PHONY: test
test:
	mvn test
