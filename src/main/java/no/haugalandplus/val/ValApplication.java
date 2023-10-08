package no.haugalandplus.val;

import no.haugalandplus.val.dto.PollInstDTO;
import no.haugalandplus.val.entities.*;
import no.haugalandplus.val.repository.*;
import no.haugalandplus.val.service.PollInstService;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.parameters.P;

import java.util.Arrays;

@SpringBootApplication
public class ValApplication {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		modelMapper.getConfiguration().setAmbiguityIgnored(true);

		return modelMapper;
	}

	private static ConfigRepository configRepository;

	private static PollRepository pollRepository;

	private static UserRepository userRepository;

	private static VoteRepository voteRepository;

	private static PollInstRepository pollInstRepository;


	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ValApplication.class, args);

		// Inits repos

//		configRepository = context.getBean(ConfigRepository.class);
//		pollRepository = context.getBean(PollRepository.class);
//		userRepository = context.getBean(UserRepository.class);
//		voteRepository = context.getBean(VoteRepository.class);
//		pollInstRepository = context.getBean(PollInstRepository.class);
//
//
//		// Creates test data
//
//		User nils = new User("NilsMichael", "Fitjar");
//		User martin = new User("MartinTunge", "Sterri");
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
