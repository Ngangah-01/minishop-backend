package io.lib.service;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseQueuingService {
    protected RabbitTemplate rabbitTemplate;
    protected RabbitAdmin rabbitAdmin;
    protected Queue mainQueue;

    @Autowired
    void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Autowired
    void setRabbitAdmin(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    protected Queue registerDeadLetterQueue(String mainQueueName) {
        String deadLetterQueueName = String.format("%s%s", mainQueueName, ".dlq");
        Queue deadLetterQueue = new Queue(deadLetterQueueName, true);
        rabbitAdmin.declareQueue(deadLetterQueue);
        return deadLetterQueue;
    }

    protected DirectExchange registerDeadLetterExchange(String mainQueueName) {
        String deadLetterExchangeName = String.format("%s%s", mainQueueName, ".dlx");
        DirectExchange deadLetterExchange = new DirectExchange(deadLetterExchangeName);
        rabbitAdmin.declareExchange(deadLetterExchange);
        return deadLetterExchange;
    }

    protected void registerDeadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange, String routingKey) {
        Binding binding = BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with(routingKey);
        rabbitAdmin.declareBinding(binding);
    }

    protected void setMainQueue(String queueName) {
        Queue deadLetterQueue = registerDeadLetterQueue(queueName);
        DirectExchange deadLetterExchange = registerDeadLetterExchange(queueName);

        String deadLetterRoutingKey = deadLetterQueue.getName();
        registerDeadLetterBinding(deadLetterQueue, deadLetterExchange, deadLetterRoutingKey);
        registerMainQueue(queueName, deadLetterExchange.getName(), deadLetterRoutingKey);
    }

    private void registerMainQueue(String queueName, String deadLetterExchangeName, String deadLetterRoutingKey) {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", deadLetterExchangeName);
        args.put("x-dead-letter-routing-key", deadLetterRoutingKey);
        mainQueue = new Queue(queueName, true, false, false, args);
        rabbitAdmin.declareQueue(mainQueue);
    }
}
