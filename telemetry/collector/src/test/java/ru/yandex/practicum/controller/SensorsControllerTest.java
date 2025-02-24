package ru.yandex.practicum.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SensorsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void lightSensorEventTest() throws Exception {
        mockMvc.perform(
                        post("/sensors")
                                .content("{" +
                                        "  \"id\": \"sensor.light.3\"," +
                                        "  \"hubId\": \"hub-2\"," +
                                        "  \"timestamp\": \"2024-08-06T16:54:03.129Z\"," +
                                        "  \"type\": \"LIGHT_SENSOR_EVENT\"," +
                                        "  \"linkQuality\": 75," +
                                        "  \"luminosity\": 59" +
                                        "}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }
    //другие тесты
}