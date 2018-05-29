package cn.com.taiji;

import static java.lang.System.out;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.support.ServletRequestHandledEvent;

import cn.com.taiji.bean.FooProperties;
import cn.com.taiji.bean.TaijCongfiguration;

@SpringBootApplication
public class BasicApplication {

	private static final Logger log = LoggerFactory.getLogger(BasicApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BasicApplication.class, args);
	}

	@Autowired
	private FooProperties fooProperties;
	
	@Autowired
	private TaijCongfiguration taijCongfiguration;
	
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
			System.out.println(fooProperties);
			System.out.println(taijCongfiguration);
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
		final String XYZ_URL = "/xyz";

		return (ApplicationEvent event) -> {
			if (event instanceof ServletRequestHandledEvent) {
				ServletRequestHandledEvent e = (ServletRequestHandledEvent) event;
				if (e.getRequestUrl().equals(XYZ_URL))
					counterService.increment("xyz.hits");
			}
		};
	}

	@Bean
	public HealthIndicator myHealth() {
		return () -> {
			RestTemplate restTemplate = new RestTemplate();
			try {
				String string = restTemplate.getForObject("https://www.baidu.com", String.class);
				System.out.println(string);
				return Health.up().build();
			} catch (Exception e) {
				return Health.down().withDetail("Error Code", 404).build();
			}

		};
	}
}
