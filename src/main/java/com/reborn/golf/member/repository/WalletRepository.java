package com.reborn.golf.member.repository;

import com.reborn.golf.member.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository <Wallet, Long> {
}
