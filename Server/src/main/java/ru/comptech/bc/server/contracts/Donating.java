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
import org.web3j.tuples.generated.Tuple11;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import ru.comptech.bc.server.model.Donation;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.2.0.
 */
public class Donating extends Contract {
    private static final String BINARY = "60606040526001600055341561001457600080fd5b610728806100236000396000f3006060604052600436106100775763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166315efc9c8811461007c57806318ac924a146100a157806343c9a46a146100c3578063f14faf6f1461016e578063f8626af814610179578063fac02859146102e8575b600080fd5b341561008757600080fd5b61008f61031d565b60405190815260200160405180910390f35b34156100ac57600080fd5b61008f600435600160a060020a0360243516610323565b34156100ce57600080fd5b61016c60046024813581810190830135806020601f8201819004810201604051908101604052818152929190602084018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052818152929190602084018383808284375094965050843594600160a060020a036020820135169450604001359250610340915050565b005b61016c600435610447565b341561018457600080fd5b61018f6004356105dc565b6040518b8152602081018b90526080810188905260a08101879052600160a060020a0386811660c0830152851660e08201526101008082018590526101208201849052821515610140830152610160604083018181528c546002600182161590940260001901169290920490830181905260608301906101808401908d90801561025a5780601f1061022f5761010080835404028352916020019161025a565b820191906000526020600020905b81548152906001019060200180831161023d57829003601f168201915b505083810382528b54600260001961010060018416150201909116048082526020909101908c9080156102ce5780601f106102a3576101008083540402835291602001916102ce565b820191906000526020600020905b8154815290600101906020018083116102b157829003601f168201915b50509d505050505050505050505050505060405180910390f35b34156102f357600080fd5b61030160043560243561063b565b604051600160a060020a03909116815260200160405180910390f35b60005481565b600260209081526000928352604080842090915290825290205481565b60008082151561034f57600080fd5b5050600080546001808201835581835260205260409091208181556002810187805161037f929160200190610661565b5060038101868051610395929160200190610661565b5060006004820181905560058201869055600682018054600160a060020a0333811673ffffffffffffffffffffffffffffffffffffffff199283161790925560078401805492881692909116919091179055426008830181905584016009830155600a8201805460ff191660019081179091558201557f7ff51c37f7a5d721e388c564567e90ae40bfaa77adce61f755d530a6dfff63c18260405190815260200160405180910390a150505050505050565b600081815260016020526040902054151561046157600080fd5b6000818152600160205260409020600a015460ff16151561048157600080fd5b600081815260016020526040902060090154429010156104a057600080fd5b34600160a060020a0333163110156104b757600080fd5b6000818152600260209081526040808320600160a060020a033316845290915290205415156105325760008181526001602081815260408084208301805490930192839055600382528084209284529190529020805473ffffffffffffffffffffffffffffffffffffffff191633600160a060020a03161790555b6000818152600260209081526040808320600160a060020a03338116855290835281842080543490810190915585855260019093529220600481018054909201909155600501543090911631106105d95760008181526001602052604090819020600a8101805460ff1916905560070154600160a060020a0390811691309091163180156108fc029151600060405180830381858888f1935050505015156105d957600080fd5b50565b6001602081905260009182526040909120805491810154600482015460058301546006840154600785015460088601546009870154600a880154969760028101976003909101969594600160a060020a03908116941692919060ff168b565b6003602090815260009283526040808420909152908252902054600160a060020a031681565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106106a257805160ff19168380011785556106cf565b828001600101855582156106cf579182015b828111156106cf5782518255916020019190600101906106b4565b506106db9291506106df565b5090565b6106f991905b808211156106db57600081556001016106e5565b905600a165627a7a72305820a3c90931a550dc4b6abd556a523ae39658b60323245d6acef8ace4defdc2e9450029";

    protected Donating(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Donating(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<CreationEventResponse> getCreationEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Creation", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<CreationEventResponse> responses = new ArrayList<CreationEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            CreationEventResponse typedResponse = new CreationEventResponse();
            typedResponse.donationId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<CreationEventResponse> creationEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Creation", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, CreationEventResponse>() {
            @Override
            public CreationEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                CreationEventResponse typedResponse = new CreationEventResponse();
                typedResponse.donationId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<BigInteger> currId() {
        Function function = new Function("currId", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> donates(BigInteger param0, String param1) {
        Function function = new Function("donates", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0), 
                new org.web3j.abi.datatypes.Address(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> createDonation(String title, String description, BigInteger needToCollect, String walletReceiver, BigInteger secondsToVote) {
        Function function = new Function(
                "createDonation", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(title), 
                new org.web3j.abi.datatypes.Utf8String(description), 
                new org.web3j.abi.datatypes.generated.Uint256(needToCollect), 
                new org.web3j.abi.datatypes.Address(walletReceiver), 
                new org.web3j.abi.datatypes.generated.Uint256(secondsToVote)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> donate(BigInteger donationId, BigInteger weiValue) {
        Function function = new Function(
                "donate", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(donationId)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<Donation> donations(BigInteger donationId) {
        final Function function = new Function("donations", 
                Arrays.asList(new org.web3j.abi.datatypes.generated.Uint256(donationId)),
                Arrays.asList(new TypeReference<Uint256>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Utf8String>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Uint256>() {
                }, new TypeReference<Bool>() {
                }));
        return new RemoteCall<>(() -> {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        final int id = ((BigInteger) results.get(0).getValue()).intValue();
                        if (id == 0)
                            throw  new IllegalArgumentException();  //FIXME : IS IT OKAY?
                        final int amountOfPayers = ((BigInteger) results.get(1).getValue()).intValue();
                        final String title = (String) results.get(2).getValue();
                        final String description = (String) results.get(3).getValue();
                        final long currentBalance = ((BigInteger) results.get(4).getValue()).longValue();
                        final long needToCollect = ((BigInteger) results.get(5).getValue()).longValue();
                        final String walletAuthor = (String) results.get(6).getValue();
                        final String walletReceiver = (String) results.get(7).getValue();
                        final long startingTime = ((BigInteger) results.get(8).getValue()).longValue();
                        final long endingTime = ((BigInteger) results.get(9).getValue()).longValue();
                        final boolean isActive = (Boolean) results.get(10).getValue();

                        final Calendar start = Calendar.getInstance();
                        start.setTimeInMillis(startingTime * 1000);
                        final Calendar end = Calendar.getInstance();
                        end.setTimeInMillis(endingTime * 1000);

                        return new Donation(id,amountOfPayers,title,description,
                                currentBalance,needToCollect,walletAuthor,walletReceiver,
                                start.getTime(),end.getTime(),isActive);
                });
    }

    public RemoteCall<String> moneysenders(BigInteger param0, BigInteger param1) {
        Function function = new Function("moneysenders", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0), 
                new org.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static RemoteCall<Donating> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Donating.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Donating> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Donating.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static Donating load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Donating(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static Donating load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Donating(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class CreationEventResponse {
        public BigInteger donationId;
    }
}
