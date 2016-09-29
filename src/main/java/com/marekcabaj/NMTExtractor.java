package com.marekcabaj;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NMTExtractor {

    public static final String RESERVED_PROPERTY = "reserved";
    public static final String COMMITTED_PROPERTY = "committed";
    public static final String CATEGORY_PROPERTY = "category";

    private String jcmdOutput;

    public NMTExtractor(String jcmdOutput) {
        this.jcmdOutput = jcmdOutput;
    }

    public Map<String, Map<String, Integer>> getNMTProperties() {
        Pattern pattern = Pattern.compile("-\\s*(?<" + CATEGORY_PROPERTY + ">.*) \\(reserved=(?<" + RESERVED_PROPERTY + ">\\d*)KB, committed=(?<" + COMMITTED_PROPERTY + ">\\d*)KB\\)");
        Matcher matcher = pattern.matcher(jcmdOutput);
        Map<String, Map<String, Integer>> nmtProperties = new HashMap<>();
        while (matcher.find()) {
            Map<String, Integer> properties = new HashMap<>();
            properties.put(RESERVED_PROPERTY, Integer.parseInt(matcher.group(RESERVED_PROPERTY)));
            properties.put(COMMITTED_PROPERTY, Integer.parseInt(matcher.group(COMMITTED_PROPERTY)));
            String category = matcher.group(CATEGORY_PROPERTY).toLowerCase().replace(" ", ".");
            nmtProperties.put(category, properties);
        }
        return nmtProperties;
    }

    public Map<String, Integer> getTotal() {
        Pattern pattern = Pattern.compile("Total: reserved=(?<" + RESERVED_PROPERTY + ">\\d*)KB, committed=(?<" + COMMITTED_PROPERTY + ">\\d*)KB");
        Matcher matcher = pattern.matcher(jcmdOutput);
        matcher.find();
        Map<String, Integer> properties = new HashMap<>();
        properties.put(RESERVED_PROPERTY, Integer.parseInt(matcher.group(RESERVED_PROPERTY)));
        properties.put(COMMITTED_PROPERTY, Integer.parseInt(matcher.group(COMMITTED_PROPERTY)));
        return properties;
    }
}
