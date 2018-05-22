package ru.antowka.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Client {
    private String ip;
    private List<Long> timestampsOfRequests = new ArrayList<>();
    private boolean isBlocked;
}
