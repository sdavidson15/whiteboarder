.PHONY: build
build:
	mvn package -DskipTests

.PHONY: clean
clean:
	mvn clean

.PHONY: test
test:
	mvn test

.PHONY: run
run:
	mvn exec:java

.PHONY: run-prod
run-prod:
	mvn exec:java -Dexec.args="prod"
