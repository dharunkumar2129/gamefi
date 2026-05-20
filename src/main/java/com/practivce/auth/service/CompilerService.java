package com.practivce.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;

@Service
public class CompilerService {

    // Free public API for running code
    private static final String PISTON_URL = "https://emkc.org/api/v2/piston/execute";

    public String execute(String code, String language, String input) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            // 1. Prepare the Request Body
            PistonRequest request = new PistonRequest(
                    language,
                    "15.0.2",
                    Collections.singletonList(new PistonFile("Main.java", code)),
                    input
            );

            // 2. Send POST Request
            PistonResponse response = restTemplate.postForObject(PISTON_URL, request, PistonResponse.class);

            // 3. Handle Result
            if (response != null && response.getRun() != null) {
                if (response.getRun().getCode() != 0) {
                    return "ERROR:\n" + response.getRun().getStderr(); // Compilation failed
                }
                return response.getRun().getStdout().trim(); // Success output
            }
            return "Error: No response from compiler.";

        } catch (Exception e) {
            return "Server Error: " + e.getMessage();
        }
    }

    // --- Internal DTOs for Piston API ---
    static class PistonRequest {
        private String language;
        private String version;
        private List<PistonFile> files;
        private String stdin;

        public PistonRequest() {}

        public PistonRequest(String language, String version, List<PistonFile> files, String stdin) {
            this.language = language;
            this.version = version;
            this.files = files;
            this.stdin = stdin;
        }

        public String getLanguage() { return language; }
        public void setLanguage(String language) { this.language = language; }

        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }

        public List<PistonFile> getFiles() { return files; }
        public void setFiles(List<PistonFile> files) { this.files = files; }

        public String getStdin() { return stdin; }
        public void setStdin(String stdin) { this.stdin = stdin; }
    }

    static class PistonFile {
        private String name;
        private String content;

        public PistonFile() {}

        public PistonFile(String name, String content) {
            this.name = name;
            this.content = content;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    static class PistonResponse {
        private RunResult run;

        public PistonResponse() {}

        public RunResult getRun() { return run; }
        public void setRun(RunResult run) { this.run = run; }
    }

    static class RunResult {
        private String stdout;
        private String stderr;
        private int code;

        public RunResult() {}

        public String getStdout() { return stdout; }
        public void setStdout(String stdout) { this.stdout = stdout; }

        public String getStderr() { return stderr; }
        public void setStderr(String stderr) { this.stderr = stderr; }

        public int getCode() { return code; }
        public void setCode(int code) { this.code = code; }
    }
}