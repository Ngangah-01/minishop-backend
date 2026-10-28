package io.userauthentication.service;


import io.userauthentication.entity.UsernameType;

public interface OnUsernameVerificationListener {
    void onSuccessfulVerification(UsernameType usernameType, String authUserId);
}
