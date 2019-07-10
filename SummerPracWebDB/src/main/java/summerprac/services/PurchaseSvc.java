package summerprac.services;

import summerprac.model.Purchase;

import java.util.ArrayList;

public class PurchaseSvc implements IService<Purchase> {
    private static PurchaseSvc instance;

    public static PurchaseSvc getInstance() {
        return instance != null ? instance : new PurchaseSvc();
    }

    @Override
    public ArrayList<Purchase> getAllRecords() {
        return null;
    }

    @Override
    public String insertRecord(String json_req) {
        return null;
    }

    @Override
    public String updateRecord(String json_req) {
        return null;
    }

    @Override
    public String deleteRecord(String json_req) {
        return null;
    }

    private PurchaseSvc() {

    }
}
