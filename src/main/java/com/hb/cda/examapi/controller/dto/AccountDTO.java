package com.hb.cda.examapi.controller.dto;

import java.util.List;

public class AccountDTO {
    private String id;
    private String name;

    public AccountDTO() {}
    public AccountDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
