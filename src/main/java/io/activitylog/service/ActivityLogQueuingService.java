package io.activitylog.service;

import io.activitylog.form.CreateActivityLogForm;
import io.lib.service.BaseQueuingService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;


@Service
public class ActivityLogQueuingService extends BaseQueuingService {
    private ActivityLogCreationService activityLogCreationService;

    public void enqueueActivityLog(CreateActivityLogForm form) {
        rabbitTemplate.convertAndSend(mainQueue.getName(), form);
    }

    @RabbitListener(queues = {"${activity.log.queue}"})
    void dequeueActivityLog(@Payload CreateActivityLogForm form){
        activityLogCreationService.saveActivityLog(form);
    }

    @Value("${activity.log.queue}")
    @Override protected void setMainQueue(String queue){
        super.setMainQueue(queue);
    }

    @Autowired
    void setActivityLogCreationConsumerService(ActivityLogCreationService service) {
        this.activityLogCreationService = service;
    }
}
