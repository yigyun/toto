package com.main.toto.global.security.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.session.AbstractSessionEvent;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.core.session.SessionRegistryImpl;

@Log4j2
public class CustomSessionRegistryImpl  extends SessionRegistryImpl {

    @Override
    public void onApplicationEvent(AbstractSessionEvent event) {
        if (event instanceof SessionDestroyedEvent) {
            log.info("Session Destroyed Event Session Remove");
            String sessionId = ((SessionDestroyedEvent) event).getId();
            removeSessionInformation(sessionId);
        }
        super.onApplicationEvent(event);
    }
}
