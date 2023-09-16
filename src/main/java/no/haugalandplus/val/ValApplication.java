package no.haugalandplus.val;

import no.haugalandplus.val.entities.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class ValApplication {

	private static ConfigRepository configRepository;

	private static PollRepository pollRepository;

	private static UserRepository userRepository;

	private static VoteRepository voteRepository;

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ValApplication.class, args);

		// Inits repos

		configRepository = context.getBean(ConfigRepository.class);
		pollRepository = context.getBean(PollRepository.class);
		userRepository = context.getBean(UserRepository.class);
		voteRepository = context.getBean(VoteRepository.class);


		// Creates test data

		User nils = new User("NilsMichael", "Fitjar");
		User martin = new User("MartinTunge", "Sterri");
		User helene = new User("HeleneSineNotatarHubert", "Solhaug");
		User lasse = new User("LasseLarsMartin", "Taraldset");


		Config config = new Config(nils);

		config.setConfigName("Default Config");

		config.setDescription("Default configuration for a poll");

		config.setAnon(false);

		config.setChoice0Name("Ja");
		config.setChoice1Name("Nei");

		Poll poll = new Poll(nils, config, "Er Nils kul?", "I denne pollen, skal man stemme på om Nils er kul!");

		userRepository.saveAll(Arrays.asList(nils, martin, helene, lasse));
		configRepository.save(config);
		pollRepository.save(poll);

		for (User u: new User[]{ martin, helene, lasse }) {
			Vote vote = new Vote(poll, u, true);
			voteRepository.save(vote);
		}


	}


}
