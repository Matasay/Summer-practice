package summerprac.services;

public class PublisherSvc {
    private static PublisherSvc ourInstance = new PublisherSvc();

    public static PublisherSvc getInstance() {
        return ourInstance;
    }

    private PublisherSvc() {
    }
}
