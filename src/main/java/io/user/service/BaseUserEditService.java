package io.user.service;

import io.activitylog.form.CreateActivityLogForm;
import io.lib.exception.CommonRuntimeException;
import io.lib.exception.ExceptionType;
import io.lib.form.SessionUserIdForm;
import io.lib.service.BaseJpaRepoEditService;
import io.user.entity.User;
import io.user.entity.UserStatus;
import io.user.form.UserEditForm;
import io.user.repository.UserRepository;
import org.apache.commons.lang.StringUtils;


public abstract class BaseUserEditService<U extends User, R extends UserRepository<U>> extends BaseJpaRepoEditService<U, R> {
    protected U register(UserEditForm form){
        U user = getNewUser();
        user.setName(form.getName());
        user.setCountryCode(form.getCountryCode());
        user.setPhoneCode(form.getPhoneCode());
        user.setPhoneNumber(form.getPhoneNumber());
        user.setEmail(form.getEmail());
        user.setNationalIdNumber(form.getNationalIdNumber());
        user.setUserStatus(UserStatus.PENDING_APPROVAL);
        user.setCreatedByEntityId(form.getSessionUserId());

        user = save(user, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(user.getEntityId());
        activityLogForm.setAction("User account creation");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return user;
    }

    public U edit(String userId, UserEditForm form) {
        U user = findByEntityId(userId);

        checkPhoneNumberExists(form.getPhoneNumber(), userId);
        checkEmailExists(form.getEmail(), userId);
        checkNationalIdNumberExists(form.getNationalIdNumber(), userId);

        user.setName(form.getName());
        user.setNationalIdNumber(form.getNationalIdNumber());

        if (!StringUtils.equals(user.getEmail(), form.getEmail())) {
            user.setEmailVerified(false);
            user.setEmail(form.getEmail());
        }

        if (!StringUtils.equals(user.getPhoneNumber(), form.getPhoneNumber())) {
            user.setPhoneNumberVerified(false);
            user.setPhoneNumber(form.getPhoneNumber());
        }

        user.setPhoneCode(form.getPhoneCode());
        user.setCountryCode(form.getCountryCode());

        user = save(user);

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(user.getEntityId());
        activityLogForm.setAction("User details update");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return user;
    }

    private void  checkPhoneNumberExists(String phoneNumber, String userId){
        boolean phoneExists = repository.existsByPhoneNumberAndEntityIdNot(
                phoneNumber,
                userId
        );
        if (phoneExists) {
            throw new CommonRuntimeException(ExceptionType.ALREADY_EXISTS, "error.user.phone.number.exits");
        }
    }

    private void checkEmailExists(String email, String  userId){
        if(StringUtils.isBlank(email)){
            return;
        }

        boolean emailExists = repository.existsByEmailAndEntityIdNot(
                email,
                userId
        );
        if (emailExists) {
            throw new CommonRuntimeException(ExceptionType.ALREADY_EXISTS, "error.user.email.exits");
        }
    }

    private void checkNationalIdNumberExists(String nationalIdNumber, String userId){
        if(StringUtils.isBlank(nationalIdNumber)){
            return;
        }

        boolean nationalIdExists = repository.existsByNationalIdNumberAndEntityIdNot(
                nationalIdNumber,
                userId
        );
        if (nationalIdExists) {
            throw new CommonRuntimeException(ExceptionType.ALREADY_EXISTS, "error.user.national.id.exits");
        }
    }

    public U suspend(String userId, SessionUserIdForm form) {
        U user = findByEntityId(userId);
        user.setUserStatus(UserStatus.SUSPENDED);
        user = save(user, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(user.getEntityId());
        activityLogForm.setAction("User suspension");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return user;
    }

    public U activate(String userId, SessionUserIdForm form) {
        U user = findByEntityId(userId);
        user.setUserStatus(UserStatus.ACTIVE);
        user = save(user, form.getSessionUserId());

        var activityLogForm = new CreateActivityLogForm();
        activityLogForm.setOwningEntityId(user.getEntityId());
        activityLogForm.setAction("User activation");
        activityLogForm.setSessionUserId(form.getSessionUserId());
        activityLogQueuingService.enqueueActivityLog(activityLogForm);

        return user;
    }

    protected abstract U getNewUser();
}
