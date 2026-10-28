package io.internaluser.controller;

import io.internaluser.service.InternalUserReadService;
import io.lib.service.SystemConfig;
import io.userauthentication.controller.AbstractVerificationCodeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(SystemConfig.INTERNAL_USER_BASE_URL + "/verification-code")
public class InternalUserVerificationCodeController
        extends AbstractVerificationCodeController<InternalUserReadService> {
}
