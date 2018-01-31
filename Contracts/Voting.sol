pragma solidity ^0.4.16;

contract Voting {
    struct Votation {
        uint id;
        string title;
        string description;
        string options;
        uint options_number;
        address wallet_autor;
        uint begin;
        uint end;
        bool is_active;
    }

    uint curr_id = 1;    
    mapping(uint => Votation) public votations;
    mapping(uint => mapping(address => uint)) public votes;

    function CreateVotation(string title, string description, string options, uint options_number, uint begin, uint end) 
    public
    returns (uint) {
        uint id = curr_id++;
        Votation storage votation = votations[id];
        votation.id = id;
        votation.title = title;
        votation.description = description;
        votation.wallet_autor = msg.sender;
        votation.begin = begin;        
        votation.end = end;
        votation.is_active = true;        
        votation.options = options;
        votation.options_number = options_number;

        return id;
    }

    function CloseVotation(uint votation_id) 
    public {
        require(votations[votation_id].id != 0);
        require(votations[votation_id].wallet_autor == msg.sender);
        require(votations[votation_id].is_active);
        
        votations[votation_id].is_active = false;
    }

    function Vote(uint votation_id, uint option_id, uint curr_time) 
    public {
        require(votations[votation_id].id != 0);
        require(votations[votation_id].is_active);
        require(votations[votation_id].end >= curr_time);
        require(option_id != 0 && option_id <= votations[votation_id].options_number);
        require(votes[votation_id][msg.sender] == 0);

        votes[votation_id][msg.sender] = option_id;
    }
}