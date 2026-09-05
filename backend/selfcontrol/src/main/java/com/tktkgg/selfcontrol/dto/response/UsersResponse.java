package com.tktkgg.selfcontrol.dto.response;

import java.util.List;

public record UsersResponse(
    List<UserResponse> users,
    int page,
    int size,
    boolean hasNext
) {}