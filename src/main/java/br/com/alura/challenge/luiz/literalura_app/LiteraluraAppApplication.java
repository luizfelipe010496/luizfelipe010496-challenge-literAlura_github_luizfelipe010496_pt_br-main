package br.com.alura.challenge.luiz.literalura_app;

import br.com.alura.challenge.luiz.literalura_app.principal.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraAppApplication implements CommandLineRunner {

	@Autowired
	private Principal principal;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraAppApplication.class, args);
	}

    public void run() throws Exception {
        run((String[]) null);
    }

    @Override
	public void run(String... args) throws Exception {
		principal.menu();
		System.exit(0);
	}
}