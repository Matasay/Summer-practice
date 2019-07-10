package summerprac.services;

import summerprac.model.Account;
import summerprac.repositories.AccountRepos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountSvc implements IService<Account> {
    private static AccountSvc instance;
    private AccountRepos repository;
    private ControlService controlSvc;

    public static AccountSvc getInstance() {
        return instance != null ? instance : new AccountSvc();
    }

    private AccountSvc() {
        repository = AccountRepos.getInstance();
        controlSvc = ControlService.getInstance();
    }

    @Override
    public ArrayList<Account> getAllRecords() {
        try (Connection connection = controlSvc.getConnection()){
            ArrayList<Account> accounts = repository.getAll(connection);
            return accounts;
        }
        catch (SQLException ex){
            return new ArrayList<Account>();
        }
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
}
