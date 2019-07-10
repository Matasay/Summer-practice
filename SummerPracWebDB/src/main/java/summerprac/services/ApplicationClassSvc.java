package summerprac.services;

public class ApplicationClassSvc {
    private static ApplicationClassSvc ourInstance = new ApplicationClassSvc();

    public static ApplicationClassSvc getInstance() {
        return ourInstance;
    }

    private ApplicationClassSvc() {
    }
}
