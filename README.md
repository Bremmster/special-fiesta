# special-fiesta
Första inlämningsuppgiften i API och webservices. Ett restAPI med några säkrade endpoints.
Projektet är till för att köra på localhost för test av Springboot SecurityFilterChain, UserDetails och JWT tokens. 

Kräver Java 21 och en mySQL version 8 eller senare.

Få igång applikation
1. packa upp projektet till valfri mapp
   2. öppna projektet i valfritt IDE
      3. Kontrollera att det finns ssl nycklar i mappen src/main/resources/certs det ska vara en private.pem och en certificate.pem
         4. Saknas SSL certifikat ladda ner https://www.openssl.org/source/ och installera.
         5. placera sen en terminal i mappen src/main/resources/certs och kör kommandot  
           ~~~
            openssl req -x509 -newkey rsa:2048 -sha256 -days 3650 \
             -nodes -keyout private.pem -out certificate.pem -subj "/CN=localhost.com" \
             -addext "subjectAltName=DNS:example.com,DNS:*.example.com,IP:127.0.0.1"
         ~~~
         Kolla att namnen på certifikaten stämmer mot det som konfigurerats i de två application.properties filerna.
         ###         Dela aldrig dina nycklar med obehöriga!
4. Kör alla test i src/test/java/com/karlson/crudapi.
5. Konfigurera databas så uppgifterna stämmer med src/main/resources/application.properties
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/crud_api
   spring.datasource.username=user
   spring.datasource.password=password
   ```
6. Starta applikationen.

### Användning
Vid uppstart generas användaren "usr" pass: "password" om den inte redan finns i user table.  
Är book table tom skapas tre böcker.  

#### Endpoints
GET http://localhost:8080/
visa information om api

POST http://localhost:8080/register
Skicka en Json body för att lägga till användare.
```
{
"name": "namn",
"password": "lösenord"
}
```
Returnerar din användare om det lyckats 

POST http://localhost:8080/auth  
Skicka Basic Auth med användarnamn och lösenord  
Returnerar en Token  


GET http://localhost:8080/protected  
Skicka med din Bearer Token.  
Returnerar en sträng att du är inloggad och ditt användarnamn.  

GET http://localhost:8080/api/v1/books/  
Alla kan nå endpoint.  
Returnerar en lista över alla böcker.  

GET http://localhost:8080/api/v1/books/{id}  
Alla kan nå endpoint.  
Returnerar en specifik bok om den finns i databasen.


POST http://localhost:8080/api/v1/books/  
Alla kan nå endpoint.  
Skicka en book som Json body  
```
{
    "author": "Lars Imby",
    "title": "Nya Svenska Fågelboken"
}
```
Om boken sparas returner den.  

PUT http://localhost:8080/api/v1/books/{id}  
Kräver Token.  
Skicka en bok med id för att uppdatera en befintlig bok  
```
{
    "id": {id}
    "author": "Lars Imby",
    "title": "Nya Svenska Fågelboken"
}
```
Lyckas uppdateringen returneras den uppdaterade boken  

DELETE http://localhost:8080/api/v1/books/{id}  
Kräver Token.  
Returnerar http 202 när det lyckats 

### Swagger Api dokumentation
GET http://localhost:8080/swagger-ui/index.html
Finns automatiskt generad documentation, dock stämmer inte utfallet men det är inte korrekt konfigurerad och far med osanningar.






Project bord https://github.com/users/Bremmster/projects/8/views/1
