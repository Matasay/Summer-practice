package summerprac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import summerprac.services.ControlService;

@SpringBootApplication
public class StartingPoint {
    public static void main(String[] args){
        ControlService.InitInstance();
        SpringApplication.run(StartingPoint.class, args);
    }
}
