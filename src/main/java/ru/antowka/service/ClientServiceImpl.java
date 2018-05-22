package ru.antowka.service;

import org.springframework.stereotype.Service;
import ru.antowka.annotation.RequestLimitByIp;
import ru.antowka.model.Client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ClientServiceImpl implements ClientService {

    private static final Map<String, Client> clientPool = new HashMap<>();

    @Override
    public boolean isLimitForClientRequests(String ip, Integer limit, Integer timeoutInSec) {

        Client client = clientPool.get(ip);
        if (client == null) {
            client = addClient(ip);
        }

        cleanOldTimestampsByClient(client, timeoutInSec);

        if(client.getTimestampsOfRequests().size() > 50) {
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
        client
                .getTimestampsOfRequests()
                .removeIf(timestamp -> timestamp / 1000 < new Date().getTime() / 1000 - timeLimit);
    }
}
