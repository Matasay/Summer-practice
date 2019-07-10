package summerprac.services;

public class SoftwareSvc {
    private static SoftwareSvc ourInstance = new SoftwareSvc();

    public static SoftwareSvc getInstance() {
        return ourInstance;
    }

    private SoftwareSvc() {
    }
}
