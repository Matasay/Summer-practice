package summerprac.repositories;

import summerprac.model.Account;
import summerprac.services.ControlService;

import java.sql.*;
import java.util.ArrayList;

public class AccountRepos {
    private static AccountRepos instance;
    private String scriptSelect;
    private String scriptGetAll;
    private String scriptUpdate;
    private String scriptInsert;
    private String scriptDelete;
    private String err_msg;

    public static AccountRepos getInstance() {
        return instance != null ? instance : new AccountRepos();
    }

    private AccountRepos() {
        String[] scripts = ControlService.getInstance().getSQLScripts("account");
        if (scripts != null) {
            scriptSelect = scripts[0];
            scriptGetAll = scripts[1];
            scriptUpdate = scripts[2];
            scriptInsert = scripts[3];
            scriptDelete = scripts[4];
        }
        else{
            System.out.println("Table: 'account' scripts not found");
        }
        err_msg = "'account' record with email: %s ";
    }

    private SQLException notFoundException(String email){
        return new SQLException(String.format(err_msg + "not found.", email));
    }

    private SQLException alreadyException(String email){
        return new SQLException(String.format(err_msg + "already in database.", email));
    }

    public int getIdByKey(Connection connection, String key_email) throws SQLException{
            PreparedStatement ps = connection.prepareStatement(scriptSelect);
            ps.setString(1, key_email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getInt(8);
            }
            throw notFoundException(key_email);
    }

    private Account convertToModel(ResultSet rs) throws SQLException {
        return new Account(
                rs.getString(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getDate(5).toString(),
                rs.getString(6).charAt(0),
                rs.getString(7)
        );
    }

    public Account select(Connection connection, String key_email) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(scriptSelect);
        ps.setString(1,key_email);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return convertToModel(rs);
        }
        throw notFoundException(key_email);
    }

    public void insert (Connection connection,String email, String password, String first_name,
                        String last_name, String date_birth, char gender, String avatar_name) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement(scriptInsert);
            ps = getAccountStatement(ps,
                    email,password,first_name,last_name,date_birth,gender,avatar_name);
            ps.executeUpdate();
        }
        catch (SQLException ex){
            throw alreadyException(email);
        }
    }

    public ArrayList<Account> getAll(Connection connection) throws SQLException {
        ArrayList<Account> accountList = new ArrayList<>();
        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery(scriptGetAll);
        Account a;
        while ( rs.next()) {
            a = convertToModel(rs);
            accountList.add(a);
        }
        return accountList;
    }

    public void update(Connection connection, String key_email, String email, String password, String first_name,
                       String last_name, String date_birth, char gender, String avatar_name) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(scriptUpdate);
        ps = getAccountStatement(ps,
                    email,password,first_name,last_name,date_birth,gender,avatar_name);
            ps.setString(8, key_email);
            System.out.println(ps.executeUpdate());
    }

    public void delete(Connection connection, String key_email) throws SQLException{
        PreparedStatement ps = connection.prepareStatement(scriptDelete);
        ps.setString(1, key_email);
        System.out.println(ps.executeUpdate());
    }



    private PreparedStatement getAccountStatement(PreparedStatement ps, String email, String password,
                                                 String first_name, String last_name, String date_birth,
                                                 char gender, String avatar_name) throws SQLException{
        ps.setString(1,email);
        ps.setString(2, password);
        ps.setString(3, first_name);
        ps.setString(4,last_name);
        ps.setDate(5,Date.valueOf(date_birth));
        ps.setString(6, String.valueOf(gender));
        ps.setString(7, avatar_name);
        return ps;
    }
}
