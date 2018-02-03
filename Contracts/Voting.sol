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
    }

    event Creation(uint votationId);

    uint public currId = 1;    
    mapping(uint => Votation) public votations;
    mapping(uint => mapping(address => uint)) public votes;

    function createVotation(string title, string description, string options,
        uint optionsNumber, uint secondsToVote) 
    public
    {
        require(secondsToVote != 0);
        uint id = currId++;
        Votation storage votation = votations[id];
        votation.id = id;
        votation.title = title;
        votation.description = description;
        votation.walletAuthor = msg.sender;
        votation.startingTime = now;        
        votation.endingTime = now + secondsToVote;
        votation.options = options;
        votation.optionsNumber = optionsNumber;

        Creation(id);
    }

    function vote(uint votationId, uint optionId) 
    public 
    {
        require(checkVoteException(votationId, optionId) == 0);
        votes[votationId][msg.sender] = optionId;
    }

    function checkVoteException(uint votationId, uint optionId)
    public
    view
    returns (uint)    
    {
        if (votations[votationId].id == 0)
            return 1;
        if (votations[votationId].endingTime < now)
            return 2;
        if ((optionId == 0 ) || 
            (optionId > votations[votationId].optionsNumber))
            return 3;
        if (votes[votationId][msg.sender] != 0)
            return 4;
        return 0;
    }
}