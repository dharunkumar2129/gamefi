package com.practivce.auth.service;


import lombok.Builder;
import lombok.Data;
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
            PistonRequest request = PistonRequest.builder()
                    .language(language) // e.g. "java"
                    .version("15.0.2")  // Piston version
                    .files(Collections.singletonList(new PistonFile("Main.java", code)))
                    .stdin(input)       // Inject hidden test case input
                    .build();

            // 2. Send POST Request
            PistonResponse response = restTemplate.postForObject(PISTON_URL, request, PistonResponse.class);

            // 3. Handle Result
            if (response != null && response.run != null) {
                if (response.run.code != 0) {
                    return "ERROR:\n" + response.run.stderr; // Compilation failed
                }
                return response.run.stdout.trim(); // Success output
            }
            return "Error: No response from compiler.";

        } catch (Exception e) {
            return "Server Error: " + e.getMessage();
        }
    }

    // --- Internal DTOs for Piston API ---
    @Data @Builder
    static class PistonRequest {
        public String language;
        public String version;
        public List<PistonFile> files;
        public String stdin;
    }

    @Data
    static class PistonFile {
        public String name;
        public String content;
        public PistonFile(String name, String content) { this.name = name; this.content = content; }
    }

    @Data
    static class PistonResponse { public RunResult run; }

    @Data
    static class RunResult {
        public String stdout;
        public String stderr;
        public int code;
    }
}