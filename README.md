# expressionInterpreter
Parse and process arithmetical and logical expressions

First steps:
git clone project, needs Java jdk 8

build:
mvn clean install

run:
goto ~/target: execute created ~.jar with argument(s) e.g.:
java -jar expressionInterpreter-0.0.1-SNAPSHOT.jar "2*(3+7)"
20
The application just prints the result of the given arithmetic or logical expression(s)
It is possible to add more than one expression and to use variables too. e.g.:
java -jar expressionInterpreter-0.0.1-SNAPSHOT.jar "A=4" "B=3" "4+A-21/B"
4
3
1
Syntax and other errors are recognized:
java -jar expressionInterpreter-0.0.1-SNAPSHOT.jar "A=4" "B=0" "4+A-21/B"
4
0
Runtime Error:
line 0: '4 + A - 21 / B'<- OPERATION_NOT_DEFINED division by 0!: Integer/Integer[/ B]



