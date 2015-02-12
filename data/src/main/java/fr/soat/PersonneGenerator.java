package fr.soat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;

public class PersonneGenerator {

	// generate a file containing a random list of personnes
	public static void main(String[] args) throws IOException {
		int nbPersons = 1000000;
		Faker faker = new Faker(Locale.FRANCE);
		try (FileWriter w = new FileWriter(new File("personnes.txt"))) {
			for (int i = 0; i < nbPersons; i++) {
				Name name = faker.name();
				w.write(name.firstName() + "\t" + name.lastName() + "\n");
			}
		}
	}

}
