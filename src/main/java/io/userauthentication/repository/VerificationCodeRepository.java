package io.userauthentication.repository;


import io.lib.repository.BaseJpaRepository;
import io.userauthentication.entity.UsernameType;
import io.userauthentication.entity.VerificationCode;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends BaseJpaRepository<VerificationCode> {
    Optional<VerificationCode> findByUsernameTypeAndUsername(UsernameType usernameType, String username);
}
