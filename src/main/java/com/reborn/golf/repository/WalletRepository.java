package com.reborn.golf.repository;

import com.reborn.golf.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository <Wallet, Long> {
}
