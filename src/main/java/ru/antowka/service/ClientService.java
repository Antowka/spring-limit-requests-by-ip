package ru.antowka.service;

public interface ClientService {
    boolean isLimitForClientRequests(String ip, Integer limit, Integer timeoutInSec);
    void testLimitForService();
}
