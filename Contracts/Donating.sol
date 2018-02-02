pragma solidity ^0.4.18;

contract Donating {
    struct Donation {
        uint id;
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

    function createDonation(string title, string description, uint needToCollect, 
        address walletReceiver, uint startingTime, uint endingTime) 
    public
    {
        uint id = currId++;
        Donation storage donation = donations[id];
        donation.id = id;
        donation.title = title;
        donation.description = description;
        donation.currentBalance = 0;
        donation.needToCollect = needToCollect;
        donation.walletAuthor = msg.sender;
        donation.walletReceiver = walletReceiver;
        donation.startingTime = startingTime;        
        donation.endingTime = endingTime;
        donation.isActive = true;

        Creation(id);
    }

    function donate(uint donationId, uint currTime)
    payable
    public 
    {
        require(donations[donationId].id != 0);
        require(donations[donationId].isActive);
        require(donations[donationId].startingTime <= currTime);
        require(donations[donationId].endingTime >= currTime);        
        require(msg.sender.balance >= msg.value);
        donates[donationId][msg.sender] += msg.value;
        donations[donationId].currentBalance += msg.value;
        if (this.balance >= donations[donationId].needToCollect) {
            donations[donationId].isActive = false;
            donations[donationId].walletReceiver.transfer(this.balance);
        }
    }
}