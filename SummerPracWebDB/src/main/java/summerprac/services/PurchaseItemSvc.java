package summerprac.services;

public class PurchaseItemSvc {
    private static PurchaseItemSvc ourInstance = new PurchaseItemSvc();

    public static PurchaseItemSvc getInstance() {
        return ourInstance;
    }

    private PurchaseItemSvc() {
    }
}
