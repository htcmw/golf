package com.reborn.golf.contracts;

import com.klaytn.caver.Caver;
import com.klaytn.caver.contract.Contract;
import com.klaytn.caver.contract.SendOptions;
import com.klaytn.caver.methods.response.Bytes32;
import com.klaytn.caver.methods.response.TransactionReceipt;
import com.klaytn.caver.transaction.TxPropertyBuilder;
import com.klaytn.caver.transaction.response.PollingTransactionReceiptProcessor;
import com.klaytn.caver.transaction.response.TransactionReceiptProcessor;
import com.klaytn.caver.transaction.type.FeeDelegatedValueTransfer;
import com.klaytn.caver.transaction.type.ValueTransfer;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import com.reborn.golf.entity.Account;
import com.reborn.golf.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.protocol.exceptions.TransactionException;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ContractTests {

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void createWallet() {
        Account account = new Account();
        Caver caver = new Caver();

        List<String> generatedKeys = caver.wallet.generate(100);
        IntStream.rangeClosed(0, 9).forEach(i -> {
            SingleKeyring keyring = (SingleKeyring) caver.wallet.getKeyring(generatedKeys.get(i));

            String address = generatedKeys.get(i);
            String pubKey = keyring.getPublicKey();
            String pvKey = keyring.getKlaytnWalletKey();

            account.setAddress(address);
            account.setPubKey(pubKey);
            account.setPvKey(pvKey);

            accountRepository.save(account);
        });
    }

    @Test
    public void Transfer() throws IOException {

        Caver caver = new Caver("https://api.baobab.klaytn.net:8651");

        // caver.wallet에 키링 추가
        SingleKeyring keyring = caver.wallet.keyring.createFromPrivateKey("0x3695438005ab4a7ca25f4b2161e03540c409ee84b07e1b501f04879a05f67f760x000x50018c6a8c904adbc7de1d2c83021bba06d31eb7");
        caver.wallet.add(keyring);

        // 자산 이동 트랜잭션 생성
        ValueTransfer valueTransfer = caver.transaction.valueTransfer.create(
                TxPropertyBuilder.valueTransfer()
                        .setFrom(keyring.getAddress())
                        .setTo("0xf5c0dd927483e43177cd7f7d8fe40dcb2112e316")
                        .setValue(BigInteger.valueOf(1))
                        .setGas(BigInteger.valueOf(30000)));

        valueTransfer.sign(keyring);
        String rlpEncoded = valueTransfer.getRLPEncoding();

        try {
            // `caver.rpc.klay.sendRawTransaction`으로 트랜잭션 서명
            Bytes32 sendResult = caver.rpc.klay.sendRawTransaction(rlpEncoded).send();
            if (sendResult.hasError()) {
                // 에러 처리
            }
            String txHash = sendResult.getResult();
            System.out.println("Transaction Hash : " + txHash);
        } catch (IOException e) {    // 예외 처리
        }
    }

    @Test
    public void 영수증확인() {
        Caver caver = new Caver("https://api.baobab.klaytn.net:8651");
        String txHash = "0x275353568ca577c17b0ea63381d2fcf13210e92626575c5f8f06b3f679ca499e";
        try {
            TransactionReceipt receipt = caver.rpc.klay.getTransactionReceipt(txHash).send();
            if (receipt.hasError()) {
                // 에러 처리
            }

            TransactionReceipt.TransactionReceiptData receiptData = receipt.getResult();
        } catch (IOException e) {
            // 예외 처리
        }
    }

    @Test
    public void 수수료대납() throws IOException, TransactionException {
        Caver caver = new Caver("https://api.baobab.klaytn.net:8651");

        // 보내는 계정 키링 추가
        SingleKeyring senderKeyring = caver.wallet.keyring.createFromPrivateKey("0x3695438005ab4a7ca25f4b2161e03540c409ee84b07e1b501f04879a05f67f760x000x50018c6a8c904adbc7de1d2c83021bba06d31eb7");
        caver.wallet.add(senderKeyring);

        // 대납 계정 키링 추가
        SingleKeyring feePayerKeyring = caver.wallet.keyring.createFromPrivateKey("0xaf373a005b889e77f4fba1f6f8bc0df85694fde61ad8e02269026f5ee1aa20ae0x000x7aab9e29ab4ae73baa4693daf9a974d2245f8078");
        caver.wallet.add(feePayerKeyring);

        // 보내는 계정 서명
        FeeDelegatedValueTransfer feeDelegatedValueTransfer = caver.transaction.feeDelegatedValueTransfer.create(
                TxPropertyBuilder.feeDelegatedValueTransfer()
                        .setFrom(senderKeyring.getAddress())
                        .setTo("0xf5c0dd927483e43177cd7f7d8fe40dcb2112e316")
                        .setValue(BigInteger.valueOf(1))
                        .setGas(BigInteger.valueOf(30000)));
        caver.wallet.sign(senderKeyring.getAddress(), feeDelegatedValueTransfer);

        String sendderRlpEncoded = feeDelegatedValueTransfer.getRLPEncoding();
        System.out.println("SenderRlp = " + sendderRlpEncoded);

        // 대납 게정 서명
//        String freeDeleRlpEncoded = sendderRlpEncoded;
        FeeDelegatedValueTransfer feeDelegatedValueTransfer2 = caver.transaction.feeDelegatedValueTransfer.decode(sendderRlpEncoded);
        feeDelegatedValueTransfer2.setFeePayer(feePayerKeyring.getAddress());
        feeDelegatedValueTransfer2.setKlaytnCall(caver.rpc.klay);

        caver.wallet.signAsFeePayer(feePayerKeyring.getAddress(), feeDelegatedValueTransfer2);

        TransactionReceiptProcessor receiptProcessor = new PollingTransactionReceiptProcessor(caver, 1000, 15);

        String resultRlpEncoded = feeDelegatedValueTransfer2.getRLPEncoding();

        try {
            // `caver.rpc.klay.sendRawTransaction`로 트랜잭션 전송
            System.out.println("ResultRlp = " + resultRlpEncoded);
            Bytes32 sendResult = caver.rpc.klay.sendRawTransaction(feeDelegatedValueTransfer2).send();
            if(sendResult.hasError()) {
                // 에러 처리
            }
            System.out.println("getResult = " + sendResult.getResult());
            String txHash = sendResult.getResult();
            System.out.println(txHash);
            TransactionReceipt.TransactionReceiptData receiptData = receiptProcessor.waitForTransactionReceipt(txHash);
        } catch (IOException | TransactionException e) {
            // 예외 처리}
        }
    }

    private static final String byteCode = "6080604052670de0b6b3a764000060065534801561001c57600080fd5b506040518060400160405280600381526020017f525454000000000000000000000000000000000000000000000000000000000081525060049080519060200190610068929190610157565b506040518060400160405280600f81526020017f5265626f726e54657374546f6b656e0000000000000000000000000000000000815250600390805190602001906100b4929190610157565b5060126002819055506006546305f5e10002600581905550336000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600554600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506101fc565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061019857805160ff19168380011785556101c6565b828001600101855582156101c6579182015b828111156101c55782518255916020019190600101906101aa565b5b5090506101d391906101d7565b5090565b6101f991905b808211156101f55760008160009055506001016101dd565b5090565b90565b6109458061020b6000396000f3fe608060405234801561001057600080fd5b50600436106100935760003560e01c80635fb0c25b116100665780635fb0c25b146101a557806370a08231146101f35780638da5cb5b1461024b57806395d89b4114610295578063bf4de3441461031857610093565b806306fdde03146100985780631624c2ce1461011b57806318160ddd14610169578063313ce56714610187575b600080fd5b6100a0610366565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156100e05780820151818401526020810190506100c5565b50505050905090810190601f16801561010d5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6101676004803603604081101561013157600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919080359060200190929190505050610404565b005b610171610594565b6040518082815260200191505060405180910390f35b61018f61059a565b6040518082815260200191505060405180910390f35b6101f1600480360360408110156101bb57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506105a0565b005b6102356004803603602081101561020957600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506106ef565b6040518082815260200191505060405180910390f35b610253610707565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b61029d61072c565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156102dd5780820151818401526020810190506102c2565b50505050905090810190601f16801561030a5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b6103646004803603604081101561032e57600080fd5b81019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190803590602001909291905050506107ca565b005b60038054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103fc5780601f106103d1576101008083540402835291602001916103fc565b820191906000526020600020905b8154815290600101906020018083116103df57829003601f168201915b505050505081565b600760008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205481600760008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205401101561049157600080fd5b80600760008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254039250508190555080600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055503373ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a35050565b60055481565b60025481565b80600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205410156105ec57600080fd5b80600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254039250508190555080600760008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055508173ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a35050565b60076020528060005260406000206000915090505481565b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60048054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107c25780601f10610797576101008083540402835291602001916107c2565b820191906000526020600020905b8154815290600101906020018083116107a557829003601f168201915b505050505081565b80600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054101561081657600080fd5b80600760003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254039250508190555080600760008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055508173ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a3505056fea165627a7a72305820f20235a9510a6afebc69a66d3007cc632d4fd257c7576ddef9408dfaada0d7e00029";
    private static final String ABIJson = "[{\"constant\":true,\"inputs\":[],\"name\":\"name\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_from\",\"type\":\"address\"},{\"name\":\"_value\",\"type\":\"uint256\"}],\"name\":\"sellTransfer\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"totalSupply\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"decimals\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_to\",\"type\":\"address\"},{\"name\":\"_value\",\"type\":\"uint256\"}],\"name\":\"buyTransfer\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"\",\"type\":\"address\"}],\"name\":\"balanceOf\",\"outputs\":[{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"owner\",\"outputs\":[{\"name\":\"\",\"type\":\"address\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"symbol\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_to\",\"type\":\"address\"},{\"name\":\"_value\",\"type\":\"uint256\"}],\"name\":\"confrimProduct\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"from\",\"type\":\"address\"},{\"indexed\":true,\"name\":\"to\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"value\",\"type\":\"uint256\"}],\"name\":\"Transfer\",\"type\":\"event\"}]";

    @Test
    public void 컨트렉트불러오기() {

        Caver caver = new Caver("https://api.baobab.klaytn.net:8651");

        SingleKeyring deployer = caver.wallet.keyring.createFromPrivateKey("0x99c28573f061ee09d2f6a57338c8144d5a3025ce5d3bccd9084fb77c3b1570d10x000xf5c0dd927483e43177cd7f7d8fe40dcb2112e316");
        caver.wallet.add(deployer);

        SingleKeyring feePayer = caver.wallet.keyring.createFromPrivateKey("0x3695438005ab4a7ca25f4b2161e03540c409ee84b07e1b501f04879a05f67f760x000x50018c6a8c904adbc7de1d2c83021bba06d31eb7");
        caver.wallet.add(feePayer);
        try {
            Contract contract = caver.contract.create(ABIJson);
            SendOptions sendOptionsForDeployment = new SendOptions();
            sendOptionsForDeployment.setFrom(deployer.getAddress());
            sendOptionsForDeployment.setGas(BigInteger.valueOf(1000000));
            sendOptionsForDeployment.setFeeDelegation(true);
            sendOptionsForDeployment.setFeePayer(feePayer.getAddress());
            contract.deploy(sendOptionsForDeployment, byteCode);
            System.out.println("The address of deployed smart contract:" + contract.getContractAddress());
        } catch (IOException | TransactionException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            // 예외 처리
        }
    }

    @Test
    public void 컨트렉트실행() {
        Caver caver = new Caver("https://api.baobab.klaytn.net:8651");
        Transfe;
        SingleKeyring executor = caver.wallet.keyring.createFromPrivateKey("0x99c28573f061ee09d2f6a57338c8144d5a3025ce5d3bccd9084fb77c3b1570d10x000xf5c0dd927483e43177cd7f7d8fe40dcb2112e316");
        caver.wallet.add(executor);

        SingleKeyring feePayer = caver.wallet.keyring.createFromPrivateKey("0x3695438005ab4a7ca25f4b2161e03540c409ee84b07e1b501f04879a05f67f760x000x50018c6a8c904adbc7de1d2c83021bba06d31eb7");
        caver.wallet.add(feePayer);

        try {
            Contract contract = caver.contract.create(ABIJson, "0x7c2cc53952059733e5480e3ccbd9c6f3b3516b46");

            contract.send(Transfer();)
            SendOptions sendOptionsForExecution = new SendOptions();
            sendOptionsForExecution.setFrom(executor.getAddress());
            sendOptionsForExecution.setGas(BigInteger.valueOf(4000000));
            sendOptionsForExecuted.setFeeDelegation(true);
            sendOptionsForExecuted.setFeePayer(feePayer.getAddress());

            TransactionReceipt.TransactionReceiptData receipt = contract.send(Transfer();, "set", "test", "testValue");
        } catch (Exception e) {
            // 예외 처리
        }

    }

    // 트랜젝션 없이 조회만할 때 사용
    @Test
    public void callContractFunction() {
        Caver caver = new Caver("https://api.baobab.klaytn.net:8651");

        try {
            Contract contract = caver.contract.create(ABIJson, "0x7c2cc53952059733e5480e3ccbd9c6f3b3516b46");
            List<Type> result = contract.call("get", "test");
            System.out.println((String)result.get(0).getValue());
        } catch (IOException | TransactionException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            // 예외 처리
        }
    }
}


