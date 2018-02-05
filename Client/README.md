# Web Fronted for ethereum

# REST-API protocol to parse JSON from web-server

## session

### /rest/login POST
### HTTP Request parameters:
### HTTP Request header: application/json
### HTTP Request body:
{

  "walletFile": "string",
  
  "password": "string"
  
}
### HTTP Response:
200 OK

400 Wallet is incorrect

### /rest/login GET
### HTTP Request parameters:
### HTTP Request header:
### HTTP Request body:
### HTTP Response:
200 OK
### HTTP Response header: application/json
### HTTP Response body:
{
  "loggedIn": true
}

### /rest/logout POST
### HTTP Request parameters:
### HTTP Request header:
### HTTP Request body:
### HTTP Response:
200 OK

