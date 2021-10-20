package com.reborn.golf.contract;

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
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.protocol.exceptions.TransactionException;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.List;


@SpringBootTest
public class WalletTests {
//
//    @Autowired
//    AccountRepository accountRepository;
//
//    @Test
//    public void createWallet() {
//        Account account = new Account();
//        Caver caver = new Caver();
//
//        List<String> generatedKeys = caver.wallet.generate(100);
//        IntStream.rangeClosed(0, 9).forEach(i -> {
//            SingleKeyring keyring = (SingleKeyring) caver.wallet.getKeyring(generatedKeys.get(i));
//
//            String address = generatedKeys.get(i);
//            String pubKey = keyring.getPublicKey();
//            String pvKey = keyring.getKlaytnWalletKey();
//
//            account.setAddress(address);
//            account.setPubKey(pubKey);
//            account.setPvKey(pvKey);
//
//            accountRepository.save(account);
//        });
//    }

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

}


