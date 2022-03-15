package com.reborn.golf.member.service;

import com.klaytn.caver.Caver;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import com.reborn.golf.common.api.ContractService;
import com.reborn.golf.common.exception.*;
import com.reborn.golf.member.dto.MemberDto;
import com.reborn.golf.member.dto.UserType;
import com.reborn.golf.member.entity.Member;
import com.reborn.golf.member.entity.Role;
import com.reborn.golf.member.entity.Wallet;
import com.reborn.golf.member.repository.MemberRepository;
import com.reborn.golf.member.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final PasswordEncoder passwordEncoder;  //비밀번호 암호화
    private final MemberRepository memberRepository;    //유저 리포지토리
    private final WalletRepository walletRepository;    //클레이튼 지갑 리포지토리
    private final ContractService contractService;  //클레이튼 서비스

    @Override
    @Transactional
    public void register(MemberDto memberDto, UserType userType) {
        try {
            /*지갑*/
            Wallet wallet = makeWallet();
            walletRepository.save(wallet);
            /*유저*/
            Member newMember = dtoToEntity(memberDto);
            newMember.registerWallet(wallet);
            newMember.changePassword(passwordEncoder.encode(memberDto.getPassword()));//유저 비밀번호 암호화
            newMember.addMemberAuthority(Role.ROLE_USER);   //유저 권한 부여
            if (userType.equals(UserType.WORKER)) {   //직원 권한 부여
                newMember.addMemberAuthority(Role.ROLE_MANAGER);
                newMember.addMemberAuthority(Role.ROLE_VIP);
            }
            memberRepository.save(newMember);
            /*회원가입 보너스 토큰*/
            contractService.transfer(wallet.getAddress(), 100000L);
        } catch (DataAccessException e) {
            log.debug(e.getMessage());
            throw new AlreadyExistEntityException("이미 있는 유저 : " + e.getMessage());
        } catch (IOException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                | InstantiationException | IllegalAccessException | TransactionException e) {
            log.debug("무료포인트 지급 실패 : " + e.getMessage());
            throw new TokenTransactionException("지갑 생성후 토큰 지급 실패");
        }

    }

    private Wallet makeWallet() {
        Caver caver = new Caver();  // BlockChain key 생성 및 할당
        List<String> generatedKeys = caver.wallet.generate(1);  //계정 1개 생성
        SingleKeyring keyring = (SingleKeyring) caver.wallet.getKeyring(generatedKeys.get(0));  //public private키 생성
        String walletAddress = generatedKeys.get(0);    //지갑주소
        String pubKey = keyring.getPublicKey(); //공개키
        String pvKey = keyring.getKlaytnWalletKey();    //비밀키

        return Wallet.builder()
                .address(walletAddress)
                .pubKey(pubKey)
                .pvKey(pvKey)
                .build();
    }

    @Override
    public MemberDto read(Integer idx) {
        Member member = memberRepository.getMemberWithWallet(idx)
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 고객정보가 DB에 없습니다"));
        try {
            Wallet wallet = member.getWallet();
            Long tokenAmount = Long.parseLong(contractService.balanceOf(wallet.getAddress())) / 1000L;
            return entityToDto(member, wallet.getAddress(), tokenAmount);
        } catch (NullPointerException | IOException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException
                | InstantiationException | IllegalAccessException e) {
            throw new NotExistsTokenInfoException("토큰 수량 정보 가져오기 실패");
        }
    }

    @Override
    @Transactional
    public Integer modify(Integer idx, MemberDto memberDto) {
        Member member = memberRepository.getByIdxAndRemovedFalse(idx)
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 고객정보가 DB에 없습니다"));

        if (member.getEmail().equals(memberDto.getEmail())) {
            member.changeName(memberDto.getName());
            member.changeAddress(memberDto.getAddress());
            member.changePhone(memberDto.getPhone());
            return member.getIdx();
        }
        throw new DifferentEmailException("이메일이 다릅니다");
    }

    @Override
    @Transactional
    public void remove(Integer idx) {
        Member member = memberRepository.getByIdxAndRemovedFalse(idx)
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 고객정보가 DB에 없습니다"));
        member.remove();
    }

    @Override
    public String searchEmail(MemberDto memberDto) {
        Member member = memberRepository.getMemberByPhoneAndRemovedFalse(memberDto.getPhone())
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 고객정보가 DB에 없습니다"));
        return member.getEmail();
    }

    @Override
    @Transactional
    public Integer searchPassword(MemberDto memberDto) {
        Member member = memberRepository.getMemberByEmailAndPhoneAndRemovedFalse(memberDto.getEmail(), memberDto.getPhone())
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 고객정보가 DB에 없습니다"));
        member.changePassword(member.getPassword());
        return member.getIdx();
    }

}
