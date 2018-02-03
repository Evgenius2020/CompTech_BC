package ru.comptech.bc.server.contracts;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.Callable;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple10;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import ru.comptech.bc.server.model.Votation;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 * <p>
 * <p>Generated with web3j version 3.2.0.
 */
public class Voting extends Contract {

    protected Voting(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Voting(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<CreationEventResponse> getCreationEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Creation",
                Arrays.asList(),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<CreationEventResponse> responses = new ArrayList<CreationEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            CreationEventResponse typedResponse = new CreationEventResponse();
            typedResponse.votationId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<CreationEventResponse> creationEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Creation",
                Arrays.asList(),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, CreationEventResponse>() {
            @Override
            public CreationEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                CreationEventResponse typedResponse = new CreationEventResponse();
                typedResponse.votationId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<BigInteger> currId() {
        Function function = new Function("currId",
                Arrays.asList(),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> createVotation(String title, String description, String options,
                                                         BigInteger optionsNumber,
                                                         BigInteger secondsToVote) {
        Function function = new Function(
                "createVotation",
                Arrays.asList(new org.web3j.abi.datatypes.Utf8String(title),
                        new org.web3j.abi.datatypes.Utf8String(description),
                        new org.web3j.abi.datatypes.Utf8String(options),
                        new org.web3j.abi.datatypes.generated.Uint256(optionsNumber),
                        new org.web3j.abi.datatypes.generated.Uint256(secondsToVote)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> closeVotation(BigInteger votationId) {
        Function function = new Function(
                "closeVotation",
                Arrays.asList(new org.web3j.abi.datatypes.generated.Uint256(votationId)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Votation> votations(BigInteger votationId) {
        final Function function = new Function("votations",
                Arrays.asList(new org.web3j.abi.datatypes.generated.Uint256(votationId)),
                Arrays.asList(new TypeReference<Uint256>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Bool>() {
                }));
        return new RemoteCall<>(() -> {
            List<Type> results = executeCallMultipleValueReturn(function);

            final int id = ((BigInteger) results.get(0).getValue()).intValue();
            final String title = (String) results.get(1).getValue();
            final String description = (String) results.get(2).getValue();
            final String rawOptions = (String) results.get(3).getValue();
            final int optionsCount = ((BigInteger) results.get(4).getValue()).intValue();
            final int votesCount = ((BigInteger) results.get(5).getValue()).intValue();
            final String author = (String) results.get(6).getValue();
            final long startSeconds = ((BigInteger) results.get(7).getValue()).longValue();
            final long endSeconds = ((BigInteger) results.get(8).getValue()).longValue();
            final boolean active = (Boolean) results.get(9).getValue();

            final Calendar start = Calendar.getInstance();
            start.setTimeInMillis(startSeconds * 1000);
            final Calendar end = Calendar.getInstance();
            end.setTimeInMillis(endSeconds * 1000);

            return new Votation(id,
                    title, description,
                    rawOptions, optionsCount, votesCount,
                    author,
                    start.getTime(), end.getTime(),
                    active);
        });
    }

    public RemoteCall<String> addresses(BigInteger votationId, BigInteger voteNumber) {
        Function function = new Function("addresses",
                Arrays.asList(new org.web3j.abi.datatypes.generated.Uint256(votationId),
                        new org.web3j.abi.datatypes.generated.Uint256(voteNumber)),
                Arrays.asList(new TypeReference<Address>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> vote(BigInteger votationId, BigInteger optionId) {
        Function function = new Function(
                "vote",
                Arrays.asList(new org.web3j.abi.datatypes.generated.Uint256(votationId),
                        new org.web3j.abi.datatypes.generated.Uint256(optionId)),
                Collections.emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> votes(BigInteger votationId, String address) {
        Function function = new Function("votes",
                Arrays.asList(new org.web3j.abi.datatypes.generated.Uint256(votationId),
                        new org.web3j.abi.datatypes.Address(address)),
                Arrays.asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static RemoteCall<Voting> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Voting.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Voting> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Voting.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Voting load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Voting(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Voting load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Voting(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class CreationEventResponse {
        public BigInteger votationId;
    }

    private static final String BINARY = "60606040526001600055341561001457600080fd5b6107e4806100236000396000f3006060604052600436106100825763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166315efc9c881146100875780631e1ce7c1146100ac57806355d3913f1461018a5780636523cfc9146101a0578063670815a914610378578063b384abef146103ad578063d23254b4146103c6575b600080fd5b341561009257600080fd5b61009a6103e8565b60405190815260200160405180910390f35b34156100b757600080fd5b61018860046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f016020809104026020016040519081016040528181529291906020840183838082843750949650508435946020013593506103ee92505050565b005b341561019557600080fd5b6101886004356104e7565b34156101ab57600080fd5b6101b6600435610566565b6040518a81526080810187905260a08101869052600160a060020a03851660c082015260e08101849052610100808201849052821515610120830152610140602083018181528c5460026001821615909402600019011692909204908301819052604083019060608401906101608501908e9080156102765780601f1061024b57610100808354040283529160200191610276565b820191906000526020600020905b81548152906001019060200180831161025957829003601f168201915b505084810383528c54600260001961010060018416150201909116048082526020909101908d9080156102ea5780601f106102bf576101008083540402835291602001916102ea565b820191906000526020600020905b8154815290600101906020018083116102cd57829003601f168201915b505084810382528b54600260001961010060018416150201909116048082526020909101908c90801561035e5780601f106103335761010080835404028352916020019161035e565b820191906000526020600020905b81548152906001019060200180831161034157829003601f168201915b50509d505050505050505050505050505060405180910390f35b341561038357600080fd5b6103916004356024356105bb565b604051600160a060020a03909116815260200160405180910390f35b34156103b857600080fd5b6101886004356024356105e1565b34156103d157600080fd5b61009a600435600160a060020a0360243516610700565b60005481565b6000808215156103fd57600080fd5b50506000805460018082018355818352602081905260409092208181559091810187805161042f92916020019061071d565b506002810186805161044592916020019061071d565b5060068101805473ffffffffffffffffffffffffffffffffffffffff191633600160a060020a031617905542600782018190558301600882015560098101805460ff19166001179055600381018580516104a392916020019061071d565b50600481018490557f7ff51c37f7a5d721e388c564567e90ae40bfaa77adce61f755d530a6dfff63c18260405190815260200160405180910390a150505050505050565b600081815260016020526040902054151561050157600080fd5b60008181526001602052604090206006015433600160a060020a0390811691161461052b57600080fd5b60008181526001602052604090206009015460ff16151561054b57600080fd5b6000908152600160205260409020600901805460ff19169055565b60016020819052600091825260409091208054600482015460058301546006840154600785015460088601546009870154959787019660028101966003909101959493600160a060020a031692919060ff168a565b6002602090815260009283526040808420909152908252902054600160a060020a031681565b60008281526001602052604081205415156105fb57600080fd5b60008381526001602052604090206009015460ff16151561061b57600080fd5b6000838152600160205260409020600801544290101561063a57600080fd5b811580159061065a57506000838152600160205260409020600401548211155b151561066557600080fd5b6000838152600360209081526040808320600160a060020a03331684529091529020541561069257600080fd5b506000828152600360209081526040808320600160a060020a0333168085529083528184209490945593825260018082528483206005018054918201905560028252848320908352905291909120805473ffffffffffffffffffffffffffffffffffffffff19169091179055565b600360209081526000928352604080842090915290825290205481565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061075e57805160ff191683800117855561078b565b8280016001018555821561078b579182015b8281111561078b578251825591602001919060010190610770565b5061079792915061079b565b5090565b6107b591905b8082111561079757600081556001016107a1565b905600a165627a7a723058206ce65a2871fa9c7f1066bd4293edf379247661e2527172e0881d490403400c310029";
}
