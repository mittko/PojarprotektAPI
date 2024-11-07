Change your code page to UTF-8 with chcp 65001 in your batch file or CMD session.
chcp 65001

REM make mvn to use speciffic java version
set JAVA_HOME=D:\Pojarprotekt_JavaHome_corretto-17.0.8.1

REM create jar file
mvn package