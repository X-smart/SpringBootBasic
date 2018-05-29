package cn.com.taiji;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.support.ServletRequestHandledEvent;

@SpringBootApplication
public class BasicApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicApplication.class, args);
	}
		 
		@Bean
		public CommandLineRunner runner() {
			return args -> {
				log.debug("Using log4j2 ...... ?");
				System.out.println();
				System.out.println("CommandLine Runner:");
				for (String arg : args) {
					System.out.println(arg);
				}
				
			};
		}

		@Bean
		public ApplicationRunner appRunner() {
			return args -> {
				System.out.println();
				System.out.println(dataSource);
				System.out.println("Application Runner:");
				for (String opt : args.getOptionNames()) {
					System.out.print(opt);
					System.out.print("->");
					System.out.println(args.getOptionValues(opt).stream().collect(Collectors.joining(",", "[", "]")));
					// String.join(",", args.getOptionValues(opt));
				}
			};
		}

		@Autowired
		private CounterService counterService;

		@Bean
		public ApplicationListener<ApplicationEvent> helloListener() {
			final String HELLO_URL = "/hello";

			return (ApplicationEvent event) -> {
				if (event instanceof ServletRequestHandledEvent) {
					ServletRequestHandledEvent e = (ServletRequestHandledEvent) event;
					if (e.getRequestUrl().equals(HELLO_URL))
						counterService.increment("hello.hits");
				}
			};
		}

		@Bean
		public HealthIndicator myHealth() {
			return () -> {
				double i=(Math.random()*2);
				if (i>1) {
				return Health.up().build();
				}else {
				return Health.down().withDetail("Error Code", 404).build();
				}
				};
		}
	}
