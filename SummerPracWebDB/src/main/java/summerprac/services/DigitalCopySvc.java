package summerprac.services;

public class DigitalCopySvc {
    private static DigitalCopySvc ourInstance = new DigitalCopySvc();

    public static DigitalCopySvc getInstance() {
        return ourInstance;
    }

    private DigitalCopySvc() {
    }
}
