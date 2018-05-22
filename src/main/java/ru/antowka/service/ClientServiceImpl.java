package ru.antowka.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.antowka.annotation.RequestLimitByIp;
import ru.antowka.model.Client;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClientServiceImpl implements ClientService {

    private static final Map<String, Client> clientPool = new ConcurrentHashMap<>();

    @Value("${client.timeout.inactive.sec}")
    private Integer timeoutForInactiveClient;

    @Override
    public boolean isLimitForClientRequests(String ip, Integer limit, Integer timeoutInSec) {

        Client client = clientPool.get(ip);
        if (client == null) {
            client = addClient(ip);
        }

        cleanOldTimestampsByClient(client, timeoutInSec);

        if (client.getTimestampsOfRequests().size() > 50) {
            return false;
        }

        client.getTimestampsOfRequests().add(new Date().getTime());

        return true;
    }

    /**
     * for tests only
     */
    public static void cleanClientPool() {
        clientPool.clear();
    }

    @Override
    @RequestLimitByIp(limit = 50, timeoutInSec = 60)
    public void testLimitForService() {
        //do something
    }

    private Client addClient(String ip) {
        Client client = new Client();
        client.setIp(ip);
        client.getTimestampsOfRequests().add(new Date().getTime());
        clientPool.put(ip, client);
        return client;
    }

    private void cleanOldTimestampsByClient(Client client, Integer timeLimit) {

        for (Iterator<Long> it = client.getTimestampsOfRequests().iterator(); it.hasNext();) {
            Long timestamp = it.next();
            if (timestamp < new Date().getTime() - timeLimit * 1000) {
                it.remove();
                continue;
            }
            break;
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void cleanOldClients() {
        Long inactiveTimeout = new Date().getTime() - timeoutForInactiveClient * 1000;
        clientPool
                .entrySet()
                .removeIf(client -> client
                        .getValue()
                        .getTimestampsOfRequests()
                        .stream()
                        .allMatch(timestamp -> timestamp < inactiveTimeout));
    }
}
