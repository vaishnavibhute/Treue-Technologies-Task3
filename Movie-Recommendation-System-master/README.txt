Rec-a-Movie is a Java-based web application developed to recommend movies to the users based on the ratings provided by them for the movies watched by them already. Recommendation is done by using collaborative filtering, an approach by which similarity between entities can be computed.

Java is used as back-end for processing the data, whereas Servlets are used to render the information to the user in form of html pages. MySQL database by means of Xampp is used to store information about movies and users, which is accessed by the Java code.

Instructions to bring up the project:

Eclipse IDE is used for the development of the movie recommendation on Windows and the steps stated further in this README are based on the assumption the same IDE will be used for testing it.

Firstly, Apache Tomcat Server 7.0 and Eclipse Web Tools Platform are to be installed. From the Eclipse IDE, when creating a new web project named "Movie", choose Apache Tomcat 7.0 as the server. Transfer the files in the attached folder "Web Content" into the folder "Web Content" created within the project directory. Transfer the .java files in the attached folder "src" into the folder "src" created within the project directory.

Also, XAMPP server needs to be installed along with Apache, and MySQL needs to be turned on by clicking on "start" on the XAMPP Control Panel, in order to have database connectivity.The id and password for the database control user are "root" and "" (empty string) respectively. These credentials are also hardcoded in the java application every time while acquiring a connection.

Additionally, phpMyAdmin needs to be accessed via web browser for data access, after starting MySQL and Apache on the XAMPP server. The files comprising of the dataset, namely movie.csv and rating.csv are placed in the attached folder "dataset". These files need to be imported to the database using phpMyAdmin by first creating a structure for the database and inside it, creating structures for the tables "movie" and "raitng" and importing the data files "movie.csv" and "rating.csv" providing path to these files on the local machine. 

In order to allow JDBC connection for the java application to have database access, jconnector needs to be downloaded from dev.mysql.com which is an open source java connector. jconnector needs to be addded to the build path of the java web application by performing the following steps:
Right on the Java Project "Movie" in Eclipse -> Build Path -> Select "Add External JAR" from the dialog box -> Provide path to the downloaded jconnector.