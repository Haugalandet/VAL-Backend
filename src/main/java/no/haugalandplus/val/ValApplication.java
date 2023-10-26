package no.haugalandplus.val;

import no.haugalandplus.val.dto.ChoiceDTO;
import no.haugalandplus.val.dto.PollDTO;
import no.haugalandplus.val.entities.Choice;
import no.haugalandplus.val.entities.Poll;
import no.haugalandplus.val.entities.User;
import no.haugalandplus.val.repository.PollInstRepository;
import no.haugalandplus.val.repository.PollRepository;
import no.haugalandplus.val.repository.UserRepository;
import no.haugalandplus.val.repository.VoteRepository;
import no.haugalandplus.val.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Type;
import java.util.List;

@SpringBootApplication
public class ValApplication {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		modelMapper.getConfiguration().setAmbiguityIgnored(true);

		return modelMapper;
	}
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("*").allowedOrigins("*");
			}
		};
	}

	private static PollRepository pollRepository;

	private static UserRepository userRepository;

	private static UserService userService;


	private static VoteRepository voteRepository;

	private static PollInstRepository pollInstRepository;


	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ValApplication.class, args);

		// Inits repos

//		configRepository = context.getBean(ConfigRepository.class);
//		pollRepository = context.getBean(PollRepository.class);
		userService = context.getBean(UserService.class);
//		voteRepository = context.getBean(VoteRepository.class);
//		pollInstRepository = context.getBean(PollInstRepository.class);
//
//
//		// Creates test data
//
//		User nils = new User("NilsMichael", "Fitjar");
//		User user = new User("adm", "pas");
//
//		userService.insertUser(user);
//		User helene = new User("HeleneSineNotatarHubert", "Solhaug");
//		User lasse = new User("LasseLarsMartin", "Taraldset");
//
//
//		Config config = new Config(nils);
//
//		config.setConfigName("Default Config");
//
//		config.setDescription("Default configuration for a poll");
//
//		config.setAnon(false);
//
//		Poll poll = new Poll(nils, config);
//
//		PollInst pollInst = new PollInst();
//		pollInst.setPoll(poll);
//
//		userRepository.saveAll(Arrays.asList(nils, martin, helene, lasse));
//		configRepository.save(config);
//		pollRepository.save(poll);
//
//		pollInstRepository.save(pollInst);

	}


}
