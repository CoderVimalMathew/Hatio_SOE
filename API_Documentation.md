# API Goal
 * Convert international currencies from one to another.
# External API
 * External API was fetched from: https://api.exchangeratesapi.io/v1/latest
 * Access key: 271c1e75ddfa6ef5328806ada803f394
 * URL and Key are added to application.properties.
# REST Endpoints
 * GET Query: localhost:8080/api/rates?base=AZN
   - Will set AZN as base currency(1.0) and convert all other currencies accordingly.
   - If no bases specified, USD will be considered as base currency.
 * POST Query: localhost:8080/api/convert
   - Raw data also to be passed (as JSON): {
                                             "from": "USD",
                                            "to": "EUR",
                                             "amount": 100
                                            }
   - Will provide the converted currency in EUR as output: {
                                                             "from": "USD",
                                                             "to": "EUR",
                                                             "amount": 100,
                                                             "convertedAmount": 94.5
                                                            }
