package summerprac.repositories;

public class PublisherRepos {
    private static PublisherRepos ourInstance = new PublisherRepos();

    public static PublisherRepos getInstance() {
        return ourInstance;
    }

    private PublisherRepos() {
    }
}
