package summerprac.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import summerprac.model.Account;
import summerprac.services.AccountSvc;

import java.util.ArrayList;

@RestController
@RequestMapping("/app_software_store")
public class HttpController {
    private AccountSvc accountSvc = AccountSvc.getInstance();

    @RequestMapping(
            params = {"table=accounts","operation=view"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ArrayList<Account> fetchAccounts(){
        return accountSvc.getAllRecords();
    }
}
