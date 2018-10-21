rm -rf bin
mkdir bin
mkdir bin/Resources
cp -R Resources/ bin/Resources/
javac -d bin -sourcepath src/  src/com/turan/streams/demo/*.java
javac -cp :lib/*:bin:. -sourcepath src/test/ -d bin src/com/turan/streams/demo/test/*.java
cd bin
java -cp ../lib/*:. com/turan/streams/demo/test/TestRunner