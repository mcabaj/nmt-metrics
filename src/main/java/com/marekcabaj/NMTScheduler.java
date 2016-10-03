package com.marekcabaj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@ConditionalOnProperty("nmt.scheduler.enabled")
class NMTScheduler {

    private final Logger logger = LoggerFactory.getLogger(NMTScheduler.class);

    @Autowired
    private ApplicationContext context;

    @Autowired
    private JcmdCommandRunner jcmdCommandRunner;

    @Scheduled(initialDelayString = "${nmt.scheduler.delay:60000}", fixedDelayString = "${nmt.scheduler.delay:60000}")
    private void readNMTProperties() {
        NMTExtractor nmtExtractor = new NMTExtractor(jcmdCommandRunner.runNMTSummary());
        Map<String, Map<String, Integer>> nmtProperties = nmtExtractor.getNMTProperties();

        Map<String, NMTPropertiesHandler> propertiesHandlers = context.getBeansOfType(NMTPropertiesHandler.class);
        propertiesHandlers.forEach((handlerClassName, handler) -> {
            logger.debug("Sending NMT properties to handler : {} ", handlerClassName);
            handler.handleNMTProperties(nmtProperties);
        });
    }
}
