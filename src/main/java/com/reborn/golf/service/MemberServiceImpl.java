package com.reborn.golf.service;

import com.klaytn.caver.Caver;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import com.reborn.golf.api.ContractService;
import com.reborn.golf.dto.exception.*;
import com.reborn.golf.dto.user.MemberDto;
import com.reborn.golf.entity.Wallet;
import com.reborn.golf.entity.Enum.Role;
import com.reborn.golf.entity.Member;
import com.reborn.golf.repository.WalletRepository;
import com.reborn.golf.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.exceptions.TransactionException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    //비밀번호 암호화
    private final PasswordEncoder passwordEncoder;
    //유저 리포지토리
    private final MemberRepository memberRepository;
    //클레이튼 지갑 리포지토리
    private final WalletRepository walletRepository;
    //클레이튼 서비스
    private final ContractService contractService;

    @Override
    public void register(MemberDto memberDto) {

        Optional<Member> result = memberRepository.getMemberByEmailAndRemovedFalse(memberDto.getEmail());
        //추가 1. 삭제된 정보의 경우 언제부터 다시 회원가입 가능한지 조건 필요
        if (result.isEmpty()) {

            //비밀번호 해쉬값
            memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

            Member newMember = dtoToEntity(memberDto);
            //User 권한 부여
            newMember.addMemberAuthority(Role.ROLE_USER);
            memberRepository.save(newMember);
            log.info(newMember);

            // BlockChain key 생성 및 할당
            Caver caver = new Caver();
            //계정 1개 생성
            List<String> generatedKeys = caver.wallet.generate(1);
            //public private키 생성
            SingleKeyring keyring = (SingleKeyring) caver.wallet.getKeyring(generatedKeys.get(0));
            //지갑주소
            String walletAddress = generatedKeys.get(0);
            //공개키
            String pubKey = keyring.getPublicKey();
            //비밀키
            String pvKey = keyring.getKlaytnWalletKey();
            Wallet wallet = Wallet.builder()
                    .address(walletAddress)
                    .pubKey(pubKey)
                    .pvKey(pvKey)
                    .member(newMember)
                    .build();
            walletRepository.save(wallet);

            try {
                contractService.transfer(wallet.getAddress(), 100000L);
            } catch (IOException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException | TransactionException e) {
                log.debug("무료포인트 지급 실패 : " + e.getMessage());
                throw new TokenTransactionException("지갑 생성후 토큰 지급 실패");
            }


        } else {
            throw new AlreadyExistEntityException("같은 이메일이 이미 있습니다.");
        }
    }

    @Override
    public MemberDto read(Integer idx) {
        Member member = memberRepository.getMemberByIdxAndRemovedFalse(idx)
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 고객정보가 DB에 없습니다"));

        String walletAddress = member.getWallet().getAddress();
        try {
            Long tokenAmount = Long.parseLong(contractService.balanceOf(walletAddress)) / 1000L;
            return entityToDto(member, walletAddress, tokenAmount);
        } catch (IOException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new NotExistsTokenInfoException("토큰 수량 정보 가져오기 실패");
        }
    }

    //이메일, 소셜 로그인 정보를 제외하고 모두 수정할 수 있다.
    @Override
    public Integer modify(Integer idx, MemberDto memberDto) {

        Member member = memberRepository.getMemberByIdxAndRemovedFalse(idx)
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 고객정보가 DB에 없습니다"));

        if (member.getEmail().equals(memberDto.getEmail())) {
            member.changeName(memberDto.getName());
            member.changeAddress(memberDto.getAddress());
            member.changePhone(memberDto.getPhone());
            memberRepository.save(member);
            log.info(member);
            return member.getIdx();
        }
        throw new DifferentEmailException("이메일이 다릅니다");
    }

    @Override
    @Transactional
    public void remove(Integer idx) {

        Member member = memberRepository.getMemberByIdxAndRemovedFalse(idx)
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 고객정보가 DB에 없습니다"));

        member.changeIsRemoved(true);
        memberRepository.save(member);
        log.info(member + " : 삭제");
    }

    @Override
    public String searchEmail(MemberDto memberDto) {
        Member member = memberRepository.getMemberByPhoneAndRemovedFalse(memberDto.getPhone())
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 고객정보가 DB에 없습니다"));
        return member.getEmail();
    }

    //변경 필요
    @Override
    public Integer searchPassword(MemberDto memberDto) {
        Member member = memberRepository.getMemberByEmailAndPhoneAndRemovedFalse(memberDto.getEmail(), memberDto.getPhone())
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 고객정보가 DB에 없습니다"));
        member.changePassword(member.getPassword());
        return member.getIdx();
    }

}
