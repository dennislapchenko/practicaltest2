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

#### Running in IDE
Project uses [Lombok](https://projectlombok.org/) annotations to reduce boiler plate code.   
To run app via IDE, Lombok plugin has to be installed and Annotation processing enabled in settings.  
(`IntelliJ IDEA`: Build,Exe.. > Compiler > Annotation Processors > enable annotation processing)


#### Code test/quality reports
Build the code to generate test, pmd, findbugs reports
```json
./gradlew clean build
```
Access the reports at:
- `./build/reports/junit-html/index.html`
- `./build/reports/findbugs/main.html`
- `./build/reports/pmd/main.html`
