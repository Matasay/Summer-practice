package summerprac.repositories;

public class SoftwareRepos {
    private static SoftwareRepos ourInstance = new SoftwareRepos();

    public static SoftwareRepos getInstance() {
        return ourInstance;
    }

    private SoftwareRepos() {
    }
}
