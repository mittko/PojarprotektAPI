Change your code page to UTF-8 with chcp 65001 in your batch file or CMD session.
chcp 65001

REM switch to java version that mus run jar file
D:
cd D:\Pojarprotekt_JavaHome_corretto-17.0.8.1\bin

REM run jar
java -jar D:\PojarprotektAPI\target\Pojarprotekt.jar