.PHONY: build
build:
	mvn package -DskipTests

.PHONY: clean
clean:
	mvn clean
	rm -v -f *.log
	rm -v -f *.log.lck

.PHONY: test
test:
	mvn test

.PHONY: run
run:
	mvn exec:java

.PHONY: run-prod
run-prod:
	mvn exec:java -Dexec.args="prod-config.txt"
