package com.gdc.avp.session;

import org.springframework.session.Session;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;

public class TokenSession implements Session, Serializable {
    @Override
    public String getId() {
        return null;
    }

    @Override
    public String changeSessionId() {
        return null;
    }

    @Override
    public <T> T getAttribute(String s) {
        return null;
    }

    @Override
    public Set<String> getAttributeNames() {
        return null;
    }

    @Override
    public void setAttribute(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {

    }

    @Override
    public Instant getCreationTime() {
        return null;
    }

    @Override
    public void setLastAccessedTime(Instant instant) {

    }

    @Override
    public Instant getLastAccessedTime() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(Duration duration) {

    }

    @Override
    public Duration getMaxInactiveInterval() {
        return null;
    }

    @Override
    public boolean isExpired() {
        return false;
    }
}
