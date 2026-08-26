package com.Grassroot.JobSearch;

import com.Grassroot.JobSearch.llm.LlmProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(LlmProperties.class)
public class JobSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobSearchApplication.class, args);
	}
}
