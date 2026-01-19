# The Game Buffer
This sample proyect is a personal management desktop app based on the popular website [Backloggd](https://backloggd.com/), designed for the user to be able to track their videogame playlists, rate and review games, explore different platforms, track developers and interact with other users.

Users can manage and organize the titles they want to play, the ones they are playing and the ones already finished, being able to rate and review them, also showing the hours spent playing at the time of rating.

Technologies used: OpenSSL, JPA/Hibernate, JavaFX, DotEnv, MapStruct, jBcrypt, MySQL, WAMP, Git

# Requirements

* JDK 23 or openjdk 23
* MySQL 8.3.0 (You can use XAMP for a local server)
* Intellij IDE
* Git Latest version


# Installation process

## Cloning the repository
- Create a folder where you want to set up the project
- Clone the repository by following [this guide](https://docs.github.com/en/repositories/creating-and-managing-repositories/cloning-a-repository)

## Setting up the database
- Navigate to "resources" in the project files
- Go to "DB-Scripts", open the file "DBStructureAndData.sql" and copy the whole content
- Open a terminal
  - Windows: Enter Win+R and search up "cmd"
  - macOS: Open the "Terminal" app
- Use the command "mysql -u (username_here) -p < (paste the .sql file name here)", press Enter and then enter your password, it will ask for it, press Enter again if you have none
- Now the database should be created and populated, you can check it by using any SELECT query to retrieve the data (e.g. "SELECT * FROM usuarios")<br><br>

- Next up, the project uses Dotenv to manage Hibernate config data, so you have to create a file at the resources folder named ".env"
- Copy the contents of the file ".envExample" to ".env"
- Replace the text in brackets (brackets included) with your own data. (IMPORTANT: The hostname in DB_URL must be "localhost" due to the certificates being generated using it as CN)
- At the "TRUSTSTORE_PASSWORD" field, paste "811JxT7n8Mv"
  
## Ensuring SSL conectivity
- Create a folder at an accesible location for your local server (XAMP)
- Navigate to the "resources" folder in the project, go to "SSLCerts" and copy the files "ca.pem","server-key.pem" and "server-cert.pem" onto the folder you have created
- Open my.ini (should be located somewhere like "C:\Program Files\MySQL\MySQL 8.3.0\my.ini") and at the bottom enter these lines:<br><br>
    [mysqld]<br>
    ssl_ca=(route to your ca.pem file)<br>
    ssl_cert=(route to your server-cert.pem file)<br>
    ssl_key=(route to your server-key.pem file)<br><br>
- Restart MySQL
- Now the server and project should be ready to run


## Finally, open the project in Intellij IDEA
- Open Intellij
- Clic on "Projects" -> "Open"
- Select the folder where you cloned the repo
- Done! the project should be ready to run
