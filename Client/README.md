# Web Fronted for ethereum

# REST-API protocol to parse JSON from web-server

## session

### /rest/login POST
Log in authorization
#### HTTP Request parameters:
#### HTTP Request header: application/json
#### HTTP Request body:
{

  "walletFile": "string",
  
  "password": "string"
  
}

#### HTTP Response code:
200 OK

#### HTTP Response code
400 Wallet is incorrect

### /rest/login GET
Is logged in?
#### HTTP Request parameters:
#### HTTP Response code:
200 OK
#### HTTP Response header: application/json
#### HTTP Response body:
{

  "loggedIn": true
  
}

### /rest/logout POST
Log out
#### HTTP Request parameters:
#### HTTP Response code:
200 OK

## voting

### /rest/votings GET
Get maximum voting's ID or count of all votings
#### HTTP Request parameters:
#### HTTP Response code:
200 OK
#### HTTP Response header: application/json
#### HTTP Response body:
{

  "maxId": 0
  
}

### /rest/votings/{votingId} GET
Get voting by ID
#### HTTP Request parameters:
votingId = (int) 1..maxId     //ID of the voting to get
#### HTTP Response code:
200 OK
#### HTTP Responce header: application/json
#### HTTP Responce body:
{

  "id": 0,
  
  "title": "string",
  
  "description": "string",
  
  "options": [
  
    {
    
      "number": 0,
      
      "name": "string",
      
      "votes": 0,
      
      "wallets": [
      
        "string"
        
      ]
      
    }
    
  ],
  
  "authorWallet": "string",
  
  "begin": "2018-02-05T19:59:52.828Z",
  
  "end": "2018-02-05T19:59:52.828Z",
  
  "active": true
  
}

#### HTTP Response code:
404 Not found

### /rest/votings/{votingId} PUT
Vote for the selected option
#### HTTP Request parameters:
votingId = (int) 1..maxId        //ID of the voting to vote in
#### HTTP Request header: application/json
#### HTTP Request body:
{

  "optionId": 0

}

#### HTTP Responce code:
200 OK

#### HTTP Responce code:
400 Icorrect data

#### HTTP Responce code:
402 Not enough money

#### HTTP Responce code:
403 Not authorized

#### HTTP Responce code:
404 Not found

### /rest/votings POST
Create new voting
#### HTTP Request parameters:
#### HTTP Request header: application/json
#### HTTP Request body:
{

  "title": "string",
  
  "description": "string",
  
  "options": [
  
    "string"
    
  ],
  
  "begin": "2018-02-05T20:08:04.781Z",
  
  "end": "2018-02-05T20:08:04.781Z"
  
}

#### HTTP Response code:
201 OK Created
#### HTTP Responce header: application/json
#### HTTP Responce body:
{

  "id": 0
  
}

#### HTTP Response code:
400 Incorrect data

#### HTTP Response code:
402 Not enough money

#### HTTP Response code:\
403 Not authorized

## payments

### /rest/payments GET
Get maximum payment's ID or count of all payments
#### HTTP Request parameters:
#### HTTP Response code:
200 OK
#### HTTP Response header: application/json
#### HTTP Response body:
{

  "maxId": 0
  
}

### /rest/payments/{paymentId} GET
Get payment by ID
#### HTTP Request parameters:
paymentId = (int) 1..maxId  //ID of the payment to get
#### HTTP Response code:
200 OK
#### HTTP Response header: application/json
#### HTTP Response body:
{

  "id": 0,
  
  "title": "string",
  
  "description": "string",
  
  "amountNeed": 0,
  
  "amountGot": 0,
  
  "parts": [
  
    {
    
      "wallet": "string",
      
      "amount": 0
      
    }
    
  ],
  
  "authorWallet": "string",
  
  "receiverWallet": "string",
  
  "begin": "2018-02-05T20:19:12.123Z",
  
  "end": "2018-02-05T20:19:12.123Z",
  
  "active": true
  
}

#### HTTP Response code:
404 Not found

### /rest/payments/{paymentId} PUT
Take part in the selected payment
#### HTTP Request parameters:
paymentId = (int) 1..maxId  //ID of the payment to take part in
#### HTTP Request header: application/json
#### HTTP Request body:
{

  "amount": 0
  
}

#### HTTP Response code:
200 OK

#### HTTP Response code:
400 Incorrect data

#### HTTP Responce code:
402 Not enough money

#### HTTP Response code:
403 Not authorized

#### HTTP Response code:
404 Not found

### /rest/payments POST
Create new payment
#### HTTP Request parameters:
#### HTTP Request header: application/json
#### HTTP Request body:
{

  "title": "string",
  
  "description": "string",
  
  "amountNeed": 0,
  
  "receiverWallet": "string",
  
  "begin": "2018-02-05T20:28:27.470Z",
  
  "end": "2018-02-05T20:28:27.470Z"
  
}

#### HTTP Response code:
201 OK Created
#### HTTP Response header: application/json
#### HTTP Response body:
{

  "id": 0
  
}

#### HTTP Response code:
400 Incorrect data

#### HTTP Response code:
402 Not enough money

#### HTTP Response code:
403 Not authorized
