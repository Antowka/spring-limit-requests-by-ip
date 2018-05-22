package ru.antowka.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import ru.antowka.AppInit;
import ru.antowka.service.ClientServiceImpl;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = AppInit.class)
@AutoConfigureMockMvc
public class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Before
    public void setUp() {
        ClientServiceImpl.cleanClientPool();
    }

    @Test
    public void emptyString() {
        assertTrue(checkController("/"));
    }

    @Test
    public void testLimitForService() {
        assertTrue(checkController("/test-limit-for-service"));
    }

    private MockHttpServletResponse doRequestToController(String url) {

        try {
            ResultActions resultActions = mvc.perform(get(url));
            return resultActions.andReturn().getResponse();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private boolean checkController(String url) {
        boolean hasTimeoutException = false;
        boolean workIsRestored = false;

        for (int i = 0; i <= 52; i++) {

            MockHttpServletResponse mockHttpServletResponse = doRequestToController(url);
            int status = mockHttpServletResponse.getStatus();

            if (i >= 50 && status == 502) {
                hasTimeoutException = true;
                sleep(61000);
                continue;
            }

            if (i > 50 && status == 200 && hasTimeoutException) {
                workIsRestored = true;
            }

            if (i < 50 && status != 200 || hasTimeoutException && workIsRestored) break;

            sleep(300);
        }

        return hasTimeoutException && workIsRestored;
    }

    private void sleep(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}