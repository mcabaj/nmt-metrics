package com.marekcabaj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class NMTExtractor {

    private final Logger logger = LoggerFactory.getLogger(NMTExtractor.class);
    public static final String RESERVED_PROPERTY = "reserved";
    public static final String COMMITTED_PROPERTY = "committed";
    public static final String CATEGORY_PROPERTY = "category";

    private Map<String, Map<String, Integer>> nmtProperties = new HashMap<>();

    NMTExtractor(String jcmdOutput) {
        extractTotalProperty(jcmdOutput);
        extractAllCategories(jcmdOutput);
        logger.debug("Extracted NMT properties : {}", nmtProperties.toString());

        if (nmtProperties.isEmpty()) {
            logger.info("NMT properties are empty after extraction. Probably something wrong occurred during extraction");
        }
    }

    private void extractAllCategories(String jcmdOutput) {
        Pattern pattern = Pattern.compile("-\\s*(?<" + CATEGORY_PROPERTY + ">.*) \\(reserved=(?<" + RESERVED_PROPERTY + ">\\d*)KB, committed=(?<" + COMMITTED_PROPERTY + ">\\d*)KB\\)");
        Matcher matcher = pattern.matcher(jcmdOutput);
        while (matcher.find()) {
            Map<String, Integer> properties = new HashMap<>();
            properties.put(RESERVED_PROPERTY, Integer.parseInt(matcher.group(RESERVED_PROPERTY)));
            properties.put(COMMITTED_PROPERTY, Integer.parseInt(matcher.group(COMMITTED_PROPERTY)));
            String category = matcher.group(CATEGORY_PROPERTY).toLowerCase().replace(" ", ".");
            nmtProperties.put(category, properties);
        }
    }

    private void extractTotalProperty(String jcmdOutput) {
        Pattern pattern = Pattern.compile("Total: reserved=(?<" + RESERVED_PROPERTY + ">\\d*)KB, committed=(?<" + COMMITTED_PROPERTY + ">\\d*)KB");
        Matcher matcher = pattern.matcher(jcmdOutput);
        matcher.find();
        Map<String, Integer> properties = new HashMap<>();
        properties.put(RESERVED_PROPERTY, Integer.parseInt(matcher.group(RESERVED_PROPERTY)));
        properties.put(COMMITTED_PROPERTY, Integer.parseInt(matcher.group(COMMITTED_PROPERTY)));
        nmtProperties.put("total", properties);
    }

    Map<String, Map<String, Integer>> getNMTProperties() {
        return nmtProperties;
    }
}
