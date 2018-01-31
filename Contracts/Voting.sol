pragma solidity ^0.4.16;

contract Voting {
    struct Votation {
        uint id;
        string title;
        string description;
        string options;
        uint optionsNumber;
        address walletAutor;
        uint begin;
        uint end;
        bool isActive;
    }

    uint currId = 1;    
    mapping(uint => Votation) public votations;
    mapping(uint => mapping(address => uint)) public votes;

    function createVotation(string title, string description, string options, uint optionsNumber, uint begin, uint end) 
    public
    returns (uint) {
        uint id = currId++;
        Votation storage votation = votations[id];
        votation.id = id;
        votation.title = title;
        votation.description = description;
        votation.walletAutor = msg.sender;
        votation.begin = begin;        
        votation.end = end;
        votation.isActive = true;        
        votation.options = options;
        votation.optionsNumber = optionsNumber;

        return id;
    }

    function closeVotation(uint votationId) 
    public {
        require(votations[votationId].id != 0);
        require(votations[votationId].walletAutor == msg.sender);
        require(votations[votationId].isActive);
        
        votations[votationId].isActive = false;
    }

    function vote(uint votationId, uint optionId, uint currTime) 
    public {
        require(votations[votationId].id != 0);
        require(votations[votationId].isActive);
        require(votations[votationId].end >= currTime);
        require(optionId != 0 && optionId <= votations[votationId].optionsNumber);
        require(votes[votationId][msg.sender] == 0);

        votes[votationId][msg.sender] = optionId;
    }
}