pragma solidity ^0.4.16;

contract Voting {
    struct Votation {
        uint id;
        string title;
        string description;
        address wallet_autor;
        Option[] options;
        bool active;
        bool end;
        string begin;
        string timeout;
    }

    struct Option {
        string name;
        address[] voters;
    }

    mapping(uint => Votation) votations;
    uint curr_id;

    function Voting() public {
        curr_id = 1;
    }

    function GetVotation(uint id)
    public
    returns (Votation)
    {    
        require(votations[id].id != 0);
        return votations[id];
    }

    function CreateVotation(string title, string description, string[] options) 
    public 
    returns (uint) {

        require(bytes(title).length != 0);
        require(options.length != 0);

        uint id = curr_id++;
        Votation storage votation = votations[id];
        votation.id = id;
        votation.title = title;
        votation.description = description;
        votation.wallet_autor = msg.sender;
        votation.active = true;
        votation.end = false;
        for(uint i = 0; i < options.length; i++) {
            votation.options[i].name = options[i];
        }

        return id;
    }

    function Vote(uint votation_id, uint option_id) 
    public {
        require(votations[votation_id].id != 0);
        Votation storage votation = votations[votation_id];
        require(votation.options.length > option_id);
        require(votation.active);
        Option[] storage options = votation.options;

        for (uint i = 0; i < options.length; i++) {
            Option storage option  = options[i];
            for (uint j = 0; j < option.voters.length; j++) {
                require(option.voters[j] != msg.sender);
            }
        }

        votation.options[option_id].voters.push(msg.sender);
    }
}