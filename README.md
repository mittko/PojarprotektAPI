
# Setup And Installation Guide
# Apache Derby. This articles explains how to install the Apache Derby database, how to start the Derby server, how to connect via Java to Derby and how to use # # the Derby command line tool to issue SQL statements. The installation of Apache Derby as Windows Service is also explained.
1. Apache Derby
1.1. Overview
Apache Derby is an open source database written in Java. It is free and performs well. Apache Derby is used in the JDK and is called Java DB. Apache Derby and Java DB are essentially the same. Apache Derby is the reference implementation for JDBC 4.0 and compatible to ANSI-SQL. JDBC is the Java interface to connect to databases.

1.2. Server vs. embedded mode
Derby can be used in a server mode and in a so-called embedded mode. If Derby runs in the server mode you start the Derby network server which will be responsible for handling the database requests. In embedded mode Derby runs within the JVM (Java Virtual Machine) of the application. In this mode only the application can access the database, e.g. another user / application will not be able to access the database.

2. Installation of Derby
Download the latest Derby version from the Apache website http://db.apache.org/derby/. Choose the bin distribution and extract this zip to a directory of your choice.

Also make the Derby tools available in your path:

Set the environment variable DERBY_HOME to the Derby installation directory

Add DERBY_HOME/bin to the "path" environment variable

3. Server mode
3.1. Starting Derby in server mode
Use the following command in the command line to start the Derby network server (located in the Derby installation directory/bin). On Microsoft Windows it is possible to use the .bat version.

startNetworkServer

This will start the network server which can serve an unlimited number of databases. By default the server will be listening on port 1527 but this can be changed via the -p option.

startNetworkServer -h localhost -p 3301

By default Derby will only accept connections from the localhost. To make the Derby server accept connections also from other hosts use the following start command. Replace "sampleserver.sampledomain.com" with the name or the IP of the server. The server will then accept connections only from other servers as the localhost.

startNetworkServer -h sampleserver.sampledomain.com
If connections should be allowed from localhost and any other server use the following.

startNetworkServer -h 0.0.0.0

3.2. Connect to the Derby Server via Java

To connect to the network server via Java code you need to have the derbyclient.jar in your classpath. The network connection string to this database is the IP address of the server:portnumber. For example for a server which is running on localhost you can create a new database via the following string.

jdbc:derby://localhost:1527/dbname;create=true
If you want to connect to an existing database you can use the following string.
jdbc:derby://localhost:1527/c:\temp\mydatabase


To connect to the embedded embedded database you need derby.jar, derbynet.jar, derbyrun.jar and derbytools.jar 
If you want to connect to an existing embedded database you can use the following string.
jdbc:derby:c:\temp\mydatabase

For example a small Java client might look like the following. This assumes that you have already created a schema called a table users with the columns "name" and "number".








import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.Statement;

public class DerbyTest {

    private Connection connect = null;
    private Statement statement = null;
    private ResultSet resultSet = null;
    public DerbyTest() throws Exception {
        try {

            Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
            connect = DriverManager
                    .getConnection("jdbc:derby://localhost/c:/temp/db/FAQ/db");
            PreparedStatement statement = connect
                    .prepareStatement("SELECT * from USERS");

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String user = resultSet.getString("name");
                String number = resultSet.getString("number");
                System.out.println("User: " + user);
                System.out.println("ID: " + number);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }

    }

    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }

    public static void main(String[] args) throws Exception {
        DerbyTest dao = new DerbyTest();
    }
    
}


4. Derby tools
4.1. Using Derby from the command line (ij)
ij is Derby’s interactive JDBC scripting tool. It is a simple utility for running scripts or interactive queries against a Derby database. To start the tool open a command shell and type in "ij". This will start a shell program which can connect to your database and execute SQL commands . Stop this tool with typing in "exit;" and pressing Enter. In ij every line needs to get terminated with ;.

If you want to connect to the Derby database in embedded mode you can use the following command. In this example the database is located at c:\temp\db\FAQ\db.

connect 'jdbc:derby:c:\temp\db\FAQ\db';
If you want to connect to a Derby database which is running in server mode then you can use the following command.

connect 'jdbc:derby://localhost:1527/c:\temp\db\FAQ\db;create=true';

To disconnect from the database.

disconnect;
To run a SQL script from ij use the following command.

run 'sqlscript.sql'

You can also use SQL directly, e.g.

select * from SCHEMA1.USERS where NUMBER='lars'
4.2. SQL dump for the database schema
Use the tool dblook. Type dblook on the console to see the options, the tool is really easy to use. For example to write the schema "myschema" to the file "lars.sql".

dblook -d 'jdbc:derby:c:\temp\db\FAQ\db' -z myschema -o lars.sql

5. Running Derby Server as Windows Service

The Derby Server is started via a batch program. In a server environment this batch program should be automatically started if the server is rebooted / started. The windows program "srvmgr" can be used for this purpose. For details on the tool please check the official documentation; the following will give a description on how this can be done for Apache Derby. Install "srvmgr" and remember the installation path.

We will call our service "ApacheDerby" and the batch file is located under "C:\db-derby\bin\startNetworkServer.bat". In the command line register a service via:

# This assumes the "srvmgr" tools are installed in c:\Windows\system32\
instsrv ApacheDerby c:\Windows\system32\srvany.exe
You should receive a success message.

If something goes wrong and you want to remove an installed service, you can use:

# This will remove a service installed with the name ApacheDerby
instsrv ApacheDerby REMOVE
Run Regedt32.exe and locate the following subkey HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\ApacheDerby.

From the Edit menu, select New  Key and add a key named Parameters.

Select the Parameters key, that you have just created. From the Edit menu, select New  String Value and maintain the following values.

Value Name: Application
Data Type : REG_SZ
String : C:\db-derby\bin\startNetworkServer.bat

Value Name: AppDirectory
Data Type : REG_SZ
String : C:\db-derby\bin\

Value Name: AppParameters
Data Type : REG_SZ
String : -h 0.0.0.0
Now start/adjust the service in the Windows services control panel.

# CREATE EMPTY DB FROM EXISTING DATABASE SCHEMA

1.open cmd type ij and press enter

2. establish connection and create database if not exist (create = true; must to be added):
connect to embedded db  'jdbc:derby:D:/NEWDB;create=true';
connect to network db 'jdbc:derby://127.0.0.1:1527/NEWDB:/RealDB';

3. run command to create install.sql file from every existing database (use dblook utility) for example -> dblook -d 'jdbc:derby:D:/RealDB'; -z APP -o install.sql;

4. type run 'install.sql';

# Connect to database from multiple clients
you cannot share Derby DB files among multiple JVMs. The first JVM to create or connect to existing DB will lock the files to itself so that no data corruption can occur.

You have two choices:

embed your database into just one app that resides in a single JVM end expose higher level API to work with it either via REST or via event bus (or both)
start Derby as a server, then the architecture looks like this:

![image](https://github.com/mittko/PojarprotektAPI/assets/6568414/fba090d8-a1c3-492c-b9b4-11dd3fb700c1)

The second approach might is easier to achieve (less coding), and the URL for JDBC or Hibernate will look like jdbc:derby://localhost:1527/derbyDB You need, of course, create the database first, for which you need to connect once with jdbc:derby://localhost:1527/derbyDB?create=true. You should not use `create=true in Hibernate pool. Just do this:



try (Connection c = DriverManager.getConnection("jdbc:derby:derbyDB;create=true")) {
    System.out.println("Database created: "+c);
} catch (SQLException e) {
    e.printStackTrace(); //adapt it to your logging/error processing
}



The code above prints something like:

Database created: org.apache.derby.impl.jdbc.EmbedConnection@105374791 (XID = 167), (SESSIONID = 1), (DATABASE = derbyDB), (DRDAID = null) 

The first approach, with single embedded app and external interface, allows you to control the resources and access much better, though. If you are going for efficiency/speed, the time spent might be well worth it.

All that said, if single embedded Derby is not enough, developers usually look for other options, e.g. PostgreSQL. Even "DB2 Express C" could be an option to ship with your app. It really depends on the use case.
