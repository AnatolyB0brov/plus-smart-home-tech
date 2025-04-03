package ru.yandex.practicum;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.yandex.practicum.processor.HubConsumer;
import ru.yandex.practicum.processor.SnapshotConsumer;

@SpringBootApplication
public class AnalyzerApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AnalyzerApplication.class, args);

        final HubConsumer hubConsumer =
                context.getBean(HubConsumer.class);
        final SnapshotConsumer snapshotConsumer =
                context.getBean(SnapshotConsumer.class);

        // запускаем в отдельном потоке обработчик событий
        // от пользовательских хабов
        Thread hubEventsThread = new Thread(hubConsumer);
        hubEventsThread.setName("HubEventHandlerThread");
        hubEventsThread.start();

        // В текущем потоке начинаем обработку
        // снимков состояния датчиков
        snapshotConsumer.run();
    }
}

