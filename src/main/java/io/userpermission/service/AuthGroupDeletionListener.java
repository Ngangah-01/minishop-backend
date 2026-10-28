package io.userpermission.service;


import io.userpermission.model.AuthGroup;

public interface AuthGroupDeletionListener {
    void onAuthGroupDeletion(AuthGroup authGroup);
}
