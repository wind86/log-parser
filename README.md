# log-parser
Simple spring-batch based application to parse log file and find IP addresses accessed site with overflowing expected threshold to ban it

# Technologies
Spring Boot, Spring Batch, Spring JDBC, Apache CLI, Lombok, Maven, MySQL, HSQL

# Requirements for local run
1. create database schema using script from src/main/resources before running application
2. configure database connection settings in src/main/resources/application.properties
3. build jar file with 'mvn clean package'
4. access.log file should have the same format as in src/test/resources/test-access.log
5. run jar using: 
	java -jar "parser.jar" --accesslog=<full_path>/access.log --startDate=2017-01-01.00:00:00 --duration=daily  --threshold=500
	java -jar "parser.jar" --accesslog=<full_path>/access.log --startDate=2017-01-01.13:30:00 --duration=hourly --threshold=100