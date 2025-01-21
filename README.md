# Instructions to run the application locally
 ## Git Cloning
   Command: git clone https://github.com/CoderVimalMathew/Hatio_SOE.git
            cd Hatio_SOE
 ## Run spring boot application
   Command: mvn spring-boot:run
 ## REST Endpoints
   ### In terminal
      - GET Command: curl localhost:8080/api/rates?AFN
       + This would display the fetch rates for the base currency AFN.
       + If no base currency given, USD will be the default base.
      - POST Command: curl -X POST http://localhost:8080/api/convert \
                      -H "Content-Type: application/json" \
                      -d '{"from": "USD", "to": "EUR", "amount": 100}'
          + Output: {"from":"USD","to":"EUR","amount":100.0,"convertedAmount":96.55}
  ### In Postman
      Postman is useful website suitable for API Collaborations.
      Install the Postman desktop agent to run local ports: https://www.postman.com/downloads/
      Launch the application and create a new request:
         GET: localhost:8080/api/rates?RWF
         POST: localhost:8080/api/convert
               (For the post, you have to add the raw data as JSON to get results.
