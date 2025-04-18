package com.rypsk.weeklymenucreator.api.model.audit;

import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    private static final String ANONYMOUS_USER = "anonymousUser";

    @NonNull
    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated() ||
                    ANONYMOUS_USER.equals(authentication.getPrincipal())) {
                return Optional.empty();
            }
            return Optional.of(authentication.getName());
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}