# Country Calling Code Locator

Scrapes wiki page with calling codes and lets the user query by the code.

### Endpoint
`GET /api/callingcode/:code`  

##### Example Request:
```
http://localhost:8080/api/callingcode/371
```
##### Example Response:
```json
{"ok":true,"country":"Latvia","timezone":"UTC+02:00"}
```

##### Other Responses:
```json
{"ok":false,"error":"Calling code did not match any country."}
```
```json
{"ok":false,"error":"Invalid code format."}
```

### Running
Build jar (via spring boot plugin):
```json
./gradlew clean bootJar
```

Run from root via:
```json
java -jar build/libs/interviewtest-0.0.1-SNAPSHOT.jar
```