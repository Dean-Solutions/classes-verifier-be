package edu.agh.dean.classesverifierbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableAsync
@EnableScheduling
public class DeanApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeanApplication.class, args);
    }

    //execute only once when database is empty. To check tokens, please connect to the database
//    @Bean
//    public CommandLineRunner commandLineRunner(
//            AuthenticationService service
//    )  {
//        return args -> {
//            var dean = RegisterRequest.builder()
//                    .firstName("Garek")
//                    .lastName("Majęcki")
//                    .email("garek@agh.edu.pl")
//                    .password("123")
//                    .indexNumber("000001")
//                    .role(Role.DEAN)
//                    .build();
//            System.out.println("Dean token: " + service.register(dean).getAccessToken());
//            var studentRep = RegisterRequest.builder()
//                    .firstName("Jan")
//                    .lastName("Kowalski")
//                    .email("jkowalski@agh.edu.pl")
//                    .indexNumber("000002")
//                    .password("123")
//                    .role(Role.STUDENT_REP)
//                    .build();
//
//            System.out.println("Student representative token: " + service.register(studentRep).getAccessToken());
//    }
//    ;
//    }

}