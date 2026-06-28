package com.aiexpensetracker.security.service.Impl;

import com.aiexpensetracker.user.entity.User;

import java.util.UUID;

public interface CurrentUserService {

    User getCurrentUser();

    UUID getCurrentUserId();

    String getCurrentUserEmail();
}