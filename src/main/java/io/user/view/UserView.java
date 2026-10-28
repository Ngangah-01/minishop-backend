package io.user.view;

import io.lib.view.BaseView;
import io.user.entity.User;
import io.user.entity.UserStatus;

public class UserView<T extends User> extends BaseView<T> {
    public UserView(T entity) {
        super(entity);
    }

    public String getPhoneNumber(){
        return entity.getPhoneNumber();
    }

    public boolean isPhoneNumberVerified(){
        return entity.isPhoneNumberVerified();
    }

    public String getPhoneCode(){
        return entity.getPhoneCode();
    }

    public String getCountryCode(){
        return entity.getCountryCode();
    }

    public String getName(){
        return entity.getName();
    }


    public String getEmail(){
        return entity.getEmail();
    }

    public boolean isEmailVerified(){
        return entity.isEmailVerified();
    }

    public UserStatus getUserStatus(){
        return entity.getUserStatus();
    }

    public  String getNationalIdNumber(){
        return entity.getNationalIdNumber();
    }
}
