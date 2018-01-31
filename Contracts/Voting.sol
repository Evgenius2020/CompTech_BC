pragma solidity ^0.4.16;

contract Voting {
    struct Votation {
        uint id;
        string title;
        string description;
        string options;
        address wallet_autor;
        bool active;
        bool end;
    }

    uint curr_id;    
    mapping(uint => Votation) public votations;
    mapping(uint => mapping(address => uint)) public votes;

    function Voting()
    public {
        curr_id = 1;
    }
    
    function CreateVotation(string title, string description, string options) 
    public
    returns (uint) {
        uint id = curr_id++;
        Votation storage votation = votations[id];
        votation.id = id;
        votation.title = title;
        votation.description = description;
        votation.wallet_autor = msg.sender;
        votation.active = true;
        votation.end = false;
        votation.options = options;

        return id;
    }
}