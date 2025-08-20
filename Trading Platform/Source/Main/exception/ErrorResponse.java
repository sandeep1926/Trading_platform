package com.supersection.trading.exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse (
     LocalDateTime timestamp,
     String message,
     String details
) {}