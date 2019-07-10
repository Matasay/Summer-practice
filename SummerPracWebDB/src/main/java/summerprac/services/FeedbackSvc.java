package summerprac.services;

public class FeedbackSvc {
    private static FeedbackSvc ourInstance = new FeedbackSvc();

    public static FeedbackSvc getInstance() {
        return ourInstance;
    }

    private FeedbackSvc() {
    }
}
