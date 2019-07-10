package summerprac.repositories;

public class FeedbackRepos {
    private static FeedbackRepos ourInstance = new FeedbackRepos();

    public static FeedbackRepos getInstance() {
        return ourInstance;
    }

    private FeedbackRepos() {
    }
}
