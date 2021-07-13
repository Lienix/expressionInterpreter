# expressionInterpreter
<p><b>parse and process arithmetical and logical expressions</b></p>
<p><b>first steps:</b>
<br>git clone project, needs Java jdk 8</p>
<p><b>build:</b>
<br>$ mvn clean install</p>
<p><b>run:</b>
<br>goto ~/target: execute created ~.jar with argument(s) e.g.:
<br>$ java -jar expressionInterpreter-0.0.1-SNAPSHOT.jar "2*(3+7)"
<br>20</p>
<p>The application just prints the result of the given arithmetic or logical expression(s)</p>
<p>It is possible to add more than one expression and to use variables too. e.g.:
<br>$ java -jar expressionInterpreter-0.0.1-SNAPSHOT.jar "A=4" "B=3" "4+A-21/B"
<br>4
<br>3
<br>1</p>
<p>Alternatively:
<br>$ java -jar expressionInterpreter-0.0.1-SNAPSHOT.jar "A=4; B=3; 4+A-21/B"
<br>1
<br>in this case only the last result is printed.</p>
<p>Syntax and other errors are recognized:
<br>$ java -jar expressionInterpreter-0.0.1-SNAPSHOT.jar "A=4" "B=0" "4+A-21/B"
<br>4
<br>0
<br>Runtime Error:
  <br>line 0: '4 + A - 21 / B'<- OPERATION_NOT_DEFINED division by 0!: Integer/Integer[/ B]</p>
<br>
<br>
<p><b>Examples with IF:</b></p>
<p><br>$ java -jar expressionInterpreter-0.0.1-SNAPSHOT.jar "A=4" "B=0" "IF(A>B,\"A > B\",\"A <= B\")"
<br>4
<br>0
<br>A > B</p>
<p><br>$ java -jar expressionInterpreter-0.0.1-SNAPSHOT.jar "A=4" "B=5" "IF(A>B,\"A > B\",\"A <= B\")"
<br>4
<br>5
<br>A <= B</p>
<p><br>$ java -jar expressionInterpreter-0.0.1-SNAPSHOT.jar "A=4; B=17; C=IF(A>B, 12, 0); C * 4"
<br>0</p>
<p><br>$ java -jar expressionInterpreter-0.0.1-SNAPSHOT.jar "A=4; B=1; C=IF(A>B, 12, 0); C * 4"
<br>48</p>




