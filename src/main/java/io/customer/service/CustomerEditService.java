package io.customer.service;

import io.customer.entity.Customer;
import io.customer.form.CustomerRegistrationForm;
import io.customer.form.CustomerSelfRegistrationForm;
import io.customer.form.CustomerUpdateForm;
import io.customer.repository.CustomerRepository;
import io.user.entity.UserStatus;
import io.user.service.BaseUserEditService;
import io.userauthentication.entity.UsernameType;
import io.userauthentication.service.OnUsernameVerificationListener;
import io.userauthentication.service.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerEditService extends BaseUserEditService<Customer, CustomerRepository> {
    private VerificationCodeService verificationCodeService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private List<OnUsernameVerificationListener> usernameVerificationListeners;

    public Customer register(CustomerSelfRegistrationForm form){
        verificationCodeService.verifyOtp(
            form.getPhoneVerificationCode().getVerificationCodeId(),
            form.getPhoneVerificationCode().getOtp()
        );

        verificationCodeService.deleteVerificationCode(form.getPhoneVerificationCode().getVerificationCodeId());

        var customer = super.register(form);

        var hashedPassword = bCryptPasswordEncoder.encode(form.getPassword());
        customer.setPassword(hashedPassword);
        customer.setUserStatus(UserStatus.ACTIVE);

        customer = save(customer, form.getSessionUserId());

        for(OnUsernameVerificationListener listener: usernameVerificationListeners) {
            listener.onSuccessfulVerification(UsernameType.PHONE_NUMBER, customer.getEntityId());
        }

        return customer;
    }

    public Customer register(CustomerRegistrationForm form){
        return super.register(form);
    }

    public Customer edit(String customerId, CustomerUpdateForm form){
        var customer = super.edit(customerId, form);
        customer = save(customer, form.getSessionUserId());
        return customer;
    }

    @Override
    protected Customer getNewUser() {
        return new Customer();
    }

    @Autowired
    public void setVerificationCodeService(VerificationCodeService service) {
        this.verificationCodeService = service;
    }

    @Autowired
    public void setBCryptPasswordEncoder(BCryptPasswordEncoder encoder) {
        this.bCryptPasswordEncoder = encoder;
    }

    @Autowired
    public void setUsernameVerificationListeners(List<OnUsernameVerificationListener> listeners) {
        this.usernameVerificationListeners = listeners;
    }
}
