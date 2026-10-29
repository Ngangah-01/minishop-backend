package io.user.service;


import io.lib.service.BaseJpaRepoEditService;
import io.user.entity.User;
import io.user.repository.UserRepository;
import io.userauthentication.entity.UsernameType;
import io.userauthentication.service.OnUsernameVerificationListener;

public class UserVerificationCodeListener<U extends User, R extends UserRepository<U>>
        extends BaseJpaRepoEditService<U, R>
        implements OnUsernameVerificationListener {
    @Override
    public void onSuccessfulVerification(UsernameType usernameType, String authUserId) {
        if(!repository.existsByEntityId(authUserId)){
            return;
        }
        U user = findByEntityId(authUserId);
        switch (usernameType){
            case EMAIL -> user.setEmailVerified(true);
            case PHONE_NUMBER -> user.setPhoneNumberVerified(true);
        }
        save(user);
    }
}
