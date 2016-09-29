package com.lmig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.lmig.NMTExtractor.COMMITTED_PROPERTY;
import static com.lmig.NMTExtractor.RESERVED_PROPERTY;

@Component
public class NMTMetric implements PublicMetrics {

    private final Logger logger = LoggerFactory.getLogger(NMTMetric.class);
    private static final String NMT_METRIC_PREFIX = "nmt.";

    @Autowired
    private JcmdCommandRunner jcmdCommandRunner;

    @Override
    public Collection<Metric<?>> metrics() {
        logger.info("Adding NMT metrics");
        NMTExtractor nmtExtractor = new NMTExtractor(jcmdCommandRunner.runNMTSummary());
        Map<String, Map<String, Integer>> nmtProperties = nmtExtractor.getNMTProperties();
        List<Metric<?>> metrics = new ArrayList<>();

        if(nmtProperties.isEmpty()) {
            return Collections.emptyList();
        }

        nmtProperties.forEach((category, properties) -> {
            metrics.add(new Metric<>(NMT_METRIC_PREFIX + category + "." + RESERVED_PROPERTY, properties.get(RESERVED_PROPERTY)));
            metrics.add(new Metric<>(NMT_METRIC_PREFIX + category + "." + COMMITTED_PROPERTY, properties.get(COMMITTED_PROPERTY)));
            logger.debug("Added metrics for category : {}", category);
        });

        metrics.add(new Metric<>(NMT_METRIC_PREFIX + "total." + RESERVED_PROPERTY, nmtExtractor.getTotal().get(RESERVED_PROPERTY)));
        metrics.add(new Metric<>(NMT_METRIC_PREFIX + "total." + COMMITTED_PROPERTY, nmtExtractor.getTotal().get(COMMITTED_PROPERTY)));
        logger.debug("Added metrics for category : total");

        return metrics;
    }
}
