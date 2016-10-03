package com.marekcabaj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

@Component
class JcmdCommandRunner {

    private final Logger logger = LoggerFactory.getLogger(JcmdCommandRunner.class);
    private static String os = System.getProperty("os.name").toLowerCase();
    private static boolean isUnix = os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0;
    private static boolean isWindows = os.indexOf("win") >= 0;
    private static String JCMD_CMD;

    @PostConstruct
    private void init() {
        if (isUnix) {
            JCMD_CMD = "./jcmd";
        } else if (isWindows) {
            JCMD_CMD = "jcmd";
        } else {
            throw new RuntimeException("OS not supported ! JcmdCommandRunner only supports Windows and Unix systems");
        }
    }

    @Autowired
    private Environment environment;

    String runNMTSummary() {
        return runJcmdCommand("VM.native_memory summary");
    }

    String runNMTBaseline() {
        return runJcmdCommand("VM.native_memory baseline");
    }

    private String runJcmdCommand(String command) {
        ProcessBuilder builder = new ProcessBuilder(JCMD_CMD, environment.getProperty("PID"), command);
        builder.directory(new File(environment.getProperty("java.home") + File.separator + "bin"));
        String cmd = builder.command().toString();
        logger.info("Running command : {}", cmd);
        builder.redirectErrorStream(true);
        try {
            Process process = builder.start();
            String output = readCommandOutput(process);
            logger.debug("Output of command {} : {}", cmd, output);
            return output;
        } catch (IOException e) {
            logger.error("Error while starting command : {}", cmd, e);
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
