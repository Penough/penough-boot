package org.penough.boot.log.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.penough.boot.log.entity.OptLogDTO;
import org.penough.boot.log.event.PeLogEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * 操作日志监听器
 * @author Penough
 * @date 2021-09-16
 */
@Component
@AllArgsConstructor
@Slf4j
public class PeOptLogListener {

    private Consumer<OptLogDTO> consumer;

    @Async
    @Order
    @EventListener(PeLogEvent.class)
    public void saveOptLog(PeLogEvent event){
        OptLogDTO optLog = (OptLogDTO)event.getSource();
        if (optLog == null) {
            return;
        }
        consumer.accept(optLog);
    }
}