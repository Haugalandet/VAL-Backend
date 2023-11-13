package no.haugalandplus.val;

import no.haugalandplus.val.dto.PollDTO;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.repository.UserRepository;
import no.haugalandplus.val.repository.VoteRepository;
import no.haugalandplus.val.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication
@EnableScheduling
public class ValApplication {

	@Bean
	public Map<Long, Sinks.Many<PollDTO>> globalSinkMap() {
		return new ConcurrentHashMap<>();
	}


	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		modelMapper.getConfiguration().setAmbiguityIgnored(true);

		return modelMapper;
	}

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ValApplication.class, args);
	}
}
