package com.tktkgg.selfcontrol.dto.response;

import java.util.List;

public class UsersResponse {
    private List<UserResponse> users;
    private int page;
    private int size;
    private boolean hasNext;

    public UsersResponse(List<UserResponse> users, int page, int size, boolean hasNext) {
        this.users = users;
        this.page = page;
        this.size = size;
        this.hasNext = hasNext;
    }

    public List<UserResponse> getUsers() {
        return users;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public boolean hasNext() {
        return hasNext;
    }
}
