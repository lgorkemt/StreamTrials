. The application reads an input file whose name is passed while calling the main program, and produces an output named
reports.txt under Recources folder which is under the root. An example of input log file mamed logs.log is already
existing under this folder including some corrupt data. The output is printed out to the console as well.

. To compile and run the application, go to the root folder and run the following commands;

to compile;

javac -d bin -sourcepath src/  src/com/turan/streams/demo/*.java

to run with a logs file;

java -cp bin com.turan.streams.demo.MainAccessLogsReport Resources/logs.log

. The application also includes tests of the application. The tests may be run by executing the build.sh from the root
folder. (The test also generates an output named test_report.txt under Resources folder with the name test_report.txt)
The output is also printed out to the console.

From the terminal go to the root and run;

sh build.sh


