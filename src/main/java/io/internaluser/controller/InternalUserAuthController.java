package io.internaluser.controller;

import io.internaluser.entity.InternalUser;
import io.internaluser.service.InternalUserAuthService;
import io.internaluser.service.InternalUserReadService;
import io.internaluser.view.InternalUserAuthView;
import io.lib.view.EntityApiResponse;
import io.userauthentication.controller.AbstractAuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.lib.service.SystemConfig.INTERNAL_USER_BASE_URL;

@RestController
@RequestMapping(INTERNAL_USER_BASE_URL +"/auth")
public class InternalUserAuthController extends AbstractAuthController<InternalUser> {
    private InternalUserAuthService internalUserAuthService;
    private InternalUserReadService internalUserReadService;

    @GetMapping("fetch")
    public EntityApiResponse<InternalUserAuthView> fetch(Authentication auth){
        InternalUser user = internalUserAuthService.findByEntityId(auth.getName());
        return new EntityApiResponse<>(internalUserReadService.getInternalUserAuthView(user));
    }

    @Override
    protected InternalUserAuthService getAuthService() {
        return internalUserAuthService;
    }

    @Autowired
    public void setInternalUserAuthService(InternalUserAuthService service) {
        this.internalUserAuthService = service;
    }

    @Autowired
    public void setInternalUserReadService(InternalUserReadService service) {
        this.internalUserReadService = service;
    }
}
