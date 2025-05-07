package project.shopclone.global.common;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class CustomMetricService {

    private final Counter customCounter;

    public CustomMetricService(MeterRegistry registry) {
        this.customCounter = registry.counter("custom_metric_counter");
    }

    public void incrementCustomMetric() {
        customCounter.increment();
    }
}