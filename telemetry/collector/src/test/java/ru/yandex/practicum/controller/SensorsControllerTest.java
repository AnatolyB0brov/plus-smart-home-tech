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
                        post("/events/sensors")
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

    @Test
    void deviceAddedTest() throws Exception {
        String json = "{\"hubId\":\"hub-1\",\"timestamp\":\"2025-02-25T19:45:32.386580494Z\"," +
                "\"id\":\"c7b8d4a1-8e37-4c1d-9130-5b0150e13954\"," +
                "\"deviceType\":\"MOTION_SENSOR\",\"type\":\"DEVICE_ADDED\"}";
        mockMvc.perform(
                        post("/events/hubs")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void scenarioAddedTest() throws Exception {
        String json = "{\"hubId\":\"hub-1\",\"timestamp\":\"2025-02-26T17:00:51.585493301Z\"," +
                "\"name\":\"Автосвет (коридор)\"," +
                "\"conditions\":[{\"sensorId\":\"c7b8d4a1-8e37-4c1d-9130-5b0150e13954\"," +
                "\"type\":\"MOTION\",\"operation\":\"EQUALS\",\"value\":1}," +
                "{\"sensorId\":\"ed9e9587-4148-4fb5-81e0-61d072568628\",\"type\":\"LUMINOSITY\",\"operation\":\"LOWER_THAN\",\"value\":500}]," +
                "\"actions\":[{\"sensorId\":\"006b61ad-cac8-4adf-9dce-43892b5d060f\",\"type\":\"ACTIVATE\",\"value\":null}]," +
                "\"type\":\"SCENARIO_ADDED\"}";
        mockMvc.perform(
                        post("/events/hubs")
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }


}