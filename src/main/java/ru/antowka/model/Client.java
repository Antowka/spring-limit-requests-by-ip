package ru.antowka.model;

import lombok.Data;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

@Data
public class Client {
    private String ip;
    private Set<Long> timestampsOfRequests = Collections.synchronizedSet(new TreeSet<>());
}
