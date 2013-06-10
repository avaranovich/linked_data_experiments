run:
	cd src 
	mvn compile package
	@java -classpath target/demos-1.0-SNAPSHOT-jar-with-dependencies.jar Main
