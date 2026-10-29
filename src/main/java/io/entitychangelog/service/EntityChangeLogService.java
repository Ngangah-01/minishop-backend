package io.entitychangelog.service;

import io.entitychangelog.entity.EntityChangeLog;
import io.entitychangelog.repository.EntityChangeLogRepository;
import io.lib.service.BaseQueuingService;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;

@Service
@Slf4j
public class EntityChangeLogService  extends BaseQueuingService {
    private EntityChangeLogRepository repository;
    private ObjectMapper objectMapper;

    public void enqueueEntityChange(Object obj){
        try {
            rabbitTemplate.convertAndSend(
                    mainQueue.getName(),
                    objectMapper.writeValueAsString(obj));
        }catch (Exception e){
            log.error("Error queueing entity changes: ", e);
        }
    }

    @RabbitListener(queues = {"${entity.change.log.queue}"})
    void dequeueEntityChange(@Payload String strVal){
        try {
            JSONObject obj = objectMapper.readValue(strVal, JSONObject.class);
            var changeLog = new EntityChangeLog();
            obj = nullifyNestedObjects(obj);
            changeLog.setObject(obj);
            try{
                changeLog.setEntityId(obj.getAsString("entityId"));
            }catch (Exception ignored){}

            repository.save(changeLog);
        }catch (Exception e){
            log.error("Error saving entity change log", e);
        }
    }

    private static JSONObject nullifyNestedObjects(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        JSONObject result = new JSONObject();
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (value instanceof LinkedHashMap<?,?> || value instanceof JSONArray) {
               continue;
            }
            result.put(key, value);
        }
        return result;
    }

    @Value("${entity.change.log.queue}")
    @Override protected void setMainQueue(String queue){
        super.setMainQueue(queue);
    }

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    public void setRepository(EntityChangeLogRepository repository) {
        this.repository = repository;
    }
}
