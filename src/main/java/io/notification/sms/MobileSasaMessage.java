package io.notification.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
class MobileSasaMessage {
    @JsonProperty("senderID")
    private String senderId = "PHILMED LTD";

    private String message;
    private String phones;
}
