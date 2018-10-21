. The application reads a input file whose name is passed while calling the main program, and produces an output under
 Recources folder which is under the root. An example of input log file is already existing under this folder including some
 corrupt data. The output is aslo printed out to tge console.

. To compile and run the application, go to the root folder and run the following commands;

to compile;

javac -d bin -sourcepath src/  src/com/turan/streams/demo/*.java

to run with a logs file;

java -cp bin com.turan.streams.demo.MainAccessLogsReport Resources/logs.log

. The application also includes tests of the application. The tests may be run by executing the build.sh from the root folder.
(The test also generated an output under Resources folder with the name test_report.txt) The output is also printed out
to the console. Go to the

sh build.sh


