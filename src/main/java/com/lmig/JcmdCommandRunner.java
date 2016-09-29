package com.lmig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

@Component
public class JcmdCommandRunner {

    private final Logger logger = LoggerFactory.getLogger(JcmdCommandRunner.class);
    private final static String JCMD_CMD = "jcmd";

    @Autowired
    private Environment environment;

    public String runNMTSummary() {
        return runJcmdCommand("VM.native_memory summary");
    }

    public String runNMTBaseline() {
        return runJcmdCommand("VM.native_memory baseline");
    }

    private String runJcmdCommand(String command) {
        ProcessBuilder builder = new ProcessBuilder(JCMD_CMD, environment.getProperty("PID"), command);
        builder.directory(new File(environment.getProperty("java.home")+ File.separator +"bin"));
        logger.info("Running command : {}, in directory : {}", builder.command().toString(), builder.directory().toString());
                builder.redirectErrorStream(true);
        try {
            Process process = builder.start();
            String output = readCommandOutput(process);
            logger.info("Command output : {}", output);
            return output;
        } catch (IOException e) {
            logger.error("Error while starting command : {}, in directory : {}", builder.command().toString(), builder.directory().toString(), e);
        }

        return null;
    }

    private String readCommandOutput(Process process) {
        StringBuilder sb = new StringBuilder();
        Scanner scanner = new Scanner(process.getInputStream());
        while (scanner.hasNextLine()) {
            sb.append(scanner.nextLine());
            sb.append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }
}
