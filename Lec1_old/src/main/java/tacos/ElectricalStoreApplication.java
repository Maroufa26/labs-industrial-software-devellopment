package tacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication   // <1>
public class ElectricalStoreApplication {

  public static void main(String[] args) {
    SpringApplication.run(ElectricalStoreApplication.class, args); // <2>
  }

}
