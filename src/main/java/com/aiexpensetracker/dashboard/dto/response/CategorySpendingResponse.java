package com.aiexpensetracker.dashboard.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public record CategorySpendingResponse(

        UUID categoryId,

        String categoryName,

        String color,

        BigDecimal amount,

        Double percentage

) {}
