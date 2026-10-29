package io.internaluser.service;

import io.internaluser.entity.InternalUser;
import io.internaluser.repository.InternalUserRepository;
import io.user.service.UserVerificationCodeListener;
import org.springframework.stereotype.Service;

@Service
public class InternalUserVerificationListener
        extends UserVerificationCodeListener<InternalUser, InternalUserRepository> {
}
