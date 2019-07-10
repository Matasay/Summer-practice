package summerprac.services;

public class ApplicationImageSvc {
    private static ApplicationImageSvc ourInstance = new ApplicationImageSvc();

    public static ApplicationImageSvc getInstance() {
        return ourInstance;
    }

    private ApplicationImageSvc() {
    }
}
