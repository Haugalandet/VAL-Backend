package no.haugalandplus.val;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ValApplication {

	public static void main(String[] args) {
		try (EntityManagerFactory factory = Persistence.createEntityManagerFactory("jpa-tutorial");
			 EntityManager em = factory.createEntityManager()) {


			// Insert new object
			em.getTransaction().begin();
			em.persist(new User("Nils Michael", "passord123"));
			em.getTransaction().commit();

		}


		SpringApplication.run(ValApplication.class, args);

	}


}
