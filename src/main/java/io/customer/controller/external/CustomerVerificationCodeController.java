package io.customer.controller.external;

import io.customer.service.CustomerReadService;
import io.lib.service.SystemConfig;
import io.userauthentication.controller.AbstractVerificationCodeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(SystemConfig.CUSTOMER_USER_BASE_URL + "/verification-code")
public class CustomerVerificationCodeController extends AbstractVerificationCodeController<CustomerReadService> {
}
