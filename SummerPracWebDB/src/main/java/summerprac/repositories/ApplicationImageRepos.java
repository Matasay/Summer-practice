package summerprac.repositories;

public class ApplicationImageRepos {
    private static ApplicationImageRepos ourInstance = new ApplicationImageRepos();

    public static ApplicationImageRepos getInstance() {
        return ourInstance;
    }

    private ApplicationImageRepos() {
    }
}
