package com.reborn.golf.service;

import com.klaytn.caver.Caver;
import com.klaytn.caver.wallet.keyring.SingleKeyring;
import com.reborn.golf.dto.user.MemberDto;
import com.reborn.golf.entity.Account;
import com.reborn.golf.entity.Enum.Role;
import com.reborn.golf.entity.Member;
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
    public void register(MemberDto memberDto) {
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
            newMember.addMemberAuthority(Role.ROLE_USER);
            memberRepository.save(newMember);
            log.info(newMember);
        } else {
            throw new AlreadyExistEntityException("같은 이메일이 이미 있습니다.");
        }
    }

    @Override
    public MemberDto read(Integer idx) {

        Member member = memberRepository.getMemberByIdxAndRemovedFalse(idx)
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 고객정보가 DB에 없습니다"));

        return entityToDto(member);

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
        }
        return member.getIdx();
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
