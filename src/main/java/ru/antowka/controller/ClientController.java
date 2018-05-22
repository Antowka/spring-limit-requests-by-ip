package ru.antowka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.antowka.annotation.RequestLimitByIp;
import ru.antowka.service.ClientService;

@Controller
public class ClientController {

    @Autowired
    private ClientService clientService;

    @RequestMapping("/")
    @RequestLimitByIp(limit = 50, timeoutInSec = 60)
    public @ResponseBody
    String emptyString() {
        return "";
    }

    @RequestMapping("/test-limit-for-service")
    public @ResponseBody
    String testLimitForService() {
        clientService.testLimitForService();
        return "";
    }
}
