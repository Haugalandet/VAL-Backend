package no.haugalandplus.val;

import no.haugalandplus.val.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class ValApplication {

	private static ConfigRepository configRepository;

	private static PollRepository pollRepository;

	private static UserRepository userRepository;

	private static VoterRepository voterRepository;

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ValApplication.class, args);


	}


}
