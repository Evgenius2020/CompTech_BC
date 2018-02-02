pragma solidity ^0.4.18;

contract Donating {
    struct Donation {
        uint id;
        uint amountOfPayers;
        string title;
        string description;
        uint currentBalance;
        uint needToCollect;
        address walletAuthor;
        address walletReceiver;
        uint startingTime;
        uint endingTime;
        bool isActive;
    }

    event Creation(uint donationId);

    uint public currId = 1;    
    mapping(uint => Donation) public donations;
    mapping(uint => mapping(address => uint)) public donates;
    mapping(uint => mapping(uint => address)) public moneysenders;
    function createDonation(string title, string description, uint needToCollect, 
        address walletReceiver, uint secondsToVote) 
    public
    {
        require(secondsToVote != 0);
        uint id = currId++;
        Donation storage donation = donations[id];
        donation.id = id;
        donation.title = title;
        donation.description = description;
        donation.currentBalance = 0;
        donation.needToCollect = needToCollect;
        donation.walletAuthor = msg.sender;
        donation.walletReceiver = walletReceiver;
        donation.startingTime = now;        
        donation.endingTime = now + secondsToVote;
        donation.isActive = true;
        donation.amountOfPayers = 0;
        Creation(id);
    }

    function donate(uint donationId)
    payable
    public 
    {
        require(donations[donationId].id != 0);
        require(donations[donationId].isActive);
        require(donations[donationId].endingTime >= now);        
        require(msg.sender.balance >= msg.value);
        if (donates[donationId][msg.sender] == 0) {
            donations[donationId].amountOfPayers++;
            moneysenders[donationId][donations[donationId].amountOfPayers] = msg.sender;
        }
        donates[donationId][msg.sender] += msg.value;
        donations[donationId].currentBalance += msg.value;
        if (this.balance >= donations[donationId].needToCollect) {
            donations[donationId].isActive = false;
            donations[donationId].walletReceiver.transfer(this.balance);
        }
    }
}
