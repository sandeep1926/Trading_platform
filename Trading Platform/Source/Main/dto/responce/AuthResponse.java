package com.supersection.trading.dto.response;

import com.supersection.trading.domain.Status;

public record AuthResponse(
        String token,
        String username,
        Status status,
        String message,
        boolean isTwoFactorAuthEnabled
) {
}