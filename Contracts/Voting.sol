pragma solidity ^0.4.18;

contract Voting {
    struct Votation {
        uint id;
        string title;
        string description;
        string options;
        uint optionsNumber;
        address walletAuthor;
        uint startingTime;
        uint endingTime;
        bool isActive;
    }

    event Creation(uint votationId);

    uint public currId = 1;    
    mapping(uint => Votation) public votations;
    mapping(uint => mapping(address => uint)) public votes;

    function createVotation(string title, string description, string options,
        uint optionsNumber, uint startingTime, uint endingTime) 
    public
    {
        uint id = currId++;
        Votation storage votation = votations[id];
        votation.id = id;
        votation.title = title;
        votation.description = description;
        votation.walletAuthor = msg.sender;
        votation.startingTime = startingTime;        
        votation.endingTime = endingTime;
        votation.isActive = true;        
        votation.options = options;
        votation.optionsNumber = optionsNumber;

        Creation(id);
    }

    function closeVotation(uint votationId) 
    public 
    {
        require(votations[votationId].id != 0);
        require(votations[votationId].walletAuthor == msg.sender);
        require(votations[votationId].isActive);
        
        votations[votationId].isActive = false;
    }

    function vote(uint votationId, uint optionId, uint currTime) 
    public 
    {
        require(votations[votationId].id != 0);
        require(votations[votationId].isActive);
        require(votations[votationId].startingTime <= currTime);
        require(votations[votationId].endingTime >= currTime);
        require(optionId != 0 && optionId <= votations[votationId].optionsNumber);
        require(votes[votationId][msg.sender] == 0);

        votes[votationId][msg.sender] = optionId;
    }
}