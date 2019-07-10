package summerprac.repositories;

public class DigitalCopyRepos {
    private static DigitalCopyRepos ourInstance = new DigitalCopyRepos();

    public static DigitalCopyRepos getInstance() {
        return ourInstance;
    }

    private DigitalCopyRepos() {
    }
}
