package com.aiexpensetracker.dashboard.model;

import com.aiexpensetracker.user.entity.User;

import java.time.LocalDate;
import java.time.YearMonth;

public record DashboardContext(

        User user,

        YearMonth period,

        LocalDate startDate,

        LocalDate endDate

) {
}