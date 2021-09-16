package org.penough.boot.log.event;

import org.penough.boot.log.entity.OptLogDTO;
import org.springframework.context.ApplicationEvent;

/**
 * Spring上下文时间封装
 * @author Penough
 * @date 2021-08-11
 */
public class PeLogEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public PeLogEvent(OptLogDTO source) {
        super(source);
    }
}
