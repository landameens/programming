del /S /F /Q client-1.0-SNAPSHOT client-1.0-SNAPSHOT.tar client-1.0-SNAPSHOT.zip log4j\logfile.log
cd ..\..\..\
call gradlew build
cd client\build\distributions\
"C:\Program Files\7-Zip\7zG" x client-1.0-SNAPSHOT.zip -y
copy "bin\*" "client-1.0-SNAPSHOT\bin\"
"C:\Program Files\7-Zip\7zG" x client-1.0-SNAPSHOT\lib\client-1.0-SNAPSHOT.jar -o"client-1.0-SNAPSHOT\lib\client-1.0-SNAPSHOT" -y
del client-1.0-SNAPSHOT\bin\client
client-1.0-SNAPSHOT\bin\client_en.bat