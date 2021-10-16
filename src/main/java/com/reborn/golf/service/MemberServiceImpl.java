package com.reborn.golf.service;

import com.reborn.golf.dto.MemberDto;
import com.reborn.golf.entity.Account;
import com.reborn.golf.entity.Member;
import com.reborn.golf.entity.MemberRole;
import com.reborn.golf.repository.AccountRepository;
import com.reborn.golf.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    //비밀번호 암호화
    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    private final AccountRepository accountRepository;


    @Override
    public boolean register(MemberDto memberDto) {
        Optional<Member> result = memberRepository.getMemberByEmailAndRemovedFalse(memberDto.getEmail());

        //추가 1. 삭제된 정보의 경우 언제부터 다시 회원가입 가능한지 조건 필요

        if (result.isEmpty()) {

            // BlockChain key 생성 및 할당
            Account account = new Account();
            Caver caver = new Caver();

            List<String> generatedKeys = caver.wallet.generate(1);

            SingleKeyring keyring = (SingleKeyring) caver.wallet.getKeyring(generatedKeys.get(0));

            String address = generatedKeys.get(0);
            String pubKey = keyring.getPublicKey();
            String pvKey = keyring.getKlaytnWalletKey();

            account.setAddress(address);
            account.setPubKey(pubKey);
            account.setPvKey(pvKey);

            accountRepository.save(account);

            memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
            Member newMember = dtoToEntity(memberDto);
            newMember.addMemberAuthority(MemberRole.ROLE_USER);
            log.info(newMember);
            memberRepository.save(newMember);
            return true;
        }
        return false;
    }

    @Override
    public MemberDto read(Integer idx) {

        Optional<Member> result = memberRepository.getMemberByIdxAndRemovedFalse(idx);

        return result.map(this::entityToDto).orElse(null);

    }

    //이메일, 소셜 로그인 정보를 제외하고 모두 수정할 수 있다.
    @Override
    public void modify(Integer idx, MemberDto memberDto) {

        Optional<Member> result = memberRepository.getMemberByIdxAndRemovedFalse(idx);

        if (result.isPresent()) {

            Member member = result.get();

            if (member.getEmail().equals(memberDto.getEmail())) {

                member.changePassword(passwordEncoder.encode(memberDto.getPassword()));
                member.changeName(memberDto.getName());
                member.changeAddress(memberDto.getAddress());
                member.changePhone(memberDto.getPhone());

                log.info(member);
                memberRepository.save(member);

            }
        }
    }

    @Override
    @Transactional
    public void remove(Integer idx) {

        Optional<Member> result = memberRepository.getMemberByIdxAndRemovedFalse(idx);

        if (result.isPresent()) {

            Member member = result.get();
            member.changeIsRemoved(true);

            log.info(member);
            memberRepository.save(member);
        }
    }
}
