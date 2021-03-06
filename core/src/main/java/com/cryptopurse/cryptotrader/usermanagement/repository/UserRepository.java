package com.cryptopurse.cryptotrader.usermanagement.repository;

import com.cryptopurse.cryptotrader.usermanagement.domain.CryptotraderUser;
import com.cryptopurse.cryptotrader.infrastructure.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CryptotraderUser, Long> {
    Optional<CryptotraderUser> findByUsername(@Param("username") String username);

    Optional<CryptotraderUser> findByActivationCode(@Param("activationCode") String invitationCode);

    Optional<CryptotraderUser> findByEmail(@Param("email") String email);

    Optional<CryptotraderUser> findByPasswordResetCode(@Param("passwordResetCode") String passwordResetCode);
}