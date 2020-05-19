del /S /F /Q  prog_lab5-1.0-SNAPSHOT prog_lab5-1.0-SNAPSHOT.tar prog_lab5-1.0-SNAPSHOT.zip log4j\logfile.log
cd ..\..\
call gradlew build
cd build\distributions\
"C:\Program Files\7-Zip\7zG" x prog_lab5-1.0-SNAPSHOT.zip -y
copy "bin\*" "prog_lab5-1.0-SNAPSHOT\bin\"
"C:\Program Files\7-Zip\7zG" x prog_lab5-1.0-SNAPSHOT\lib\prog_lab5-1.0-SNAPSHOT.jar -o"prog_lab5-1.0-SNAPSHOT\lib\prog_lab5-1.0-SNAPSHOT" -y
del prog_lab5-1.0-SNAPSHOT\bin\prog_lab5
prog_lab5-1.0-SNAPSHOT\bin\prog_lab5.bat