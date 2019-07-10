package summerprac.services;

import summerprac.model.Account;

import java.util.ArrayList;

interface IService<Obj> {
    ArrayList<Obj> getAllRecords();
    String insertRecord(String json_req);
    String updateRecord(String json_req);
    String deleteRecord(String json_req);
}
