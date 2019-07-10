package summerprac.repositories;

import summerprac.model.Purchase;
import summerprac.services.ControlService;

import java.sql.*;
import java.util.ArrayList;

public class PurchaseRepos {
    private static PurchaseRepos instance;
    private String scriptSelect;
    private String scriptGetAll;
    private String scriptUpdate;
    private String scriptInsert;
    private String scriptDelete;
    private String err_msg;


    public static PurchaseRepos getInstance() {
        return instance != null ? instance : new PurchaseRepos();
    }

    private PurchaseRepos() {
        String[] scripts = ControlService.getInstance().getSQLScripts("purchase");
        if (scripts != null) {
            scriptSelect = scripts[0];
            scriptGetAll = scripts[1];
            scriptUpdate = scripts[2];
            scriptInsert = scripts[3];
            scriptDelete = scripts[4];
        }
        else{
            System.out.println("Table: 'purchase' scripts not found");
        }
        err_msg = "'purchase' record with cash_voucher_first: %s  and  cash_voucher_last: %s  ";
    }

    private SQLException notFoundException(long cash_voucher_first, long cash_voucher_last){
        return new SQLException(String.format(err_msg + "not found.", cash_voucher_first, cash_voucher_last));
    }

    private SQLException alreadyException(long cash_voucher_first, long cash_voucher_last){
        return new SQLException(String.format(err_msg + "already in database.", cash_voucher_first, cash_voucher_last));
    }

    private Purchase convertToModel(ResultSet rs) throws SQLException{
        return new Purchase(
                rs.getLong(1),
                rs.getLong(2),
                rs.getString(3),
                rs.getTimestamp(4).toLocalDateTime().toString().replace('T', ' '),
                rs.getString(5).replace('?', '$')
        );
    }

    private PreparedStatement getPurchaseStatement(PreparedStatement ps,
                                                   long cash_voucher_first, long cash_voucher_last, int account_id,
                                                   Timestamp payment_datetime, String payment_amount) throws SQLException{
        ps.setLong(1,cash_voucher_first);
        ps.setLong(2, cash_voucher_last);
        ps.setInt(3, account_id);
        ps.setTimestamp(4,payment_datetime);
        ps.setString(5,payment_amount.replace('.',','));
        return ps;
    }

    public int getIdByKey(Connection connection, long key_cash_voucher_first, long key_cash_voucher_last)
            throws SQLException{
        PreparedStatement ps = connection.prepareStatement(scriptSelect);
        ps.setLong(1, key_cash_voucher_first);
        ps.setLong(2, key_cash_voucher_last);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return rs.getInt(6);
        }
        throw notFoundException(key_cash_voucher_first, key_cash_voucher_last);
    }

    public Purchase select(Connection connection, long key_cash_voucher_first, long key_cash_voucher_last)
            throws SQLException{
        PreparedStatement ps = connection.prepareStatement(scriptSelect);
        ps.setLong(1, key_cash_voucher_first);
        ps.setLong(2, key_cash_voucher_last);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return convertToModel(rs);
        }
        throw notFoundException(key_cash_voucher_first, key_cash_voucher_last);
    }

    public ArrayList<Purchase> getAll(Connection connection) throws SQLException {
        ArrayList<Purchase> purchases = new ArrayList<>();
        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery(scriptGetAll);
        Purchase p;
        while (rs.next()){
            p = convertToModel(rs);
            purchases.add(p);
        }
        return purchases;
    }

    public void update(Connection connection, long key_cash_voucher_first, long key_cash_voucher_last,
                       long cash_voucher_first, long cash_voucher_last, int account_id, Timestamp payment_datetime,
                       String payment_amount) throws SQLException{
        PreparedStatement ps = connection.prepareStatement(scriptUpdate);
        ps = getPurchaseStatement(ps, cash_voucher_first, cash_voucher_last,
                account_id, payment_datetime, payment_amount);
        ps.setLong(6,key_cash_voucher_first);
        ps.setLong(7,key_cash_voucher_last);
        System.out.println(ps.executeUpdate());
    }

    public void insert(Connection connection, long cash_voucher_first, long cash_voucher_last, int account_id,
                       Timestamp payment_datetime, String payment_amount) throws SQLException {
        try {
            PreparedStatement ps = connection.prepareStatement(scriptInsert);
            ps = getPurchaseStatement(ps, cash_voucher_first, cash_voucher_last,
                    account_id, payment_datetime, payment_amount);
            ps.executeUpdate();
        }
        catch (SQLException ex){
            throw alreadyException(cash_voucher_first,cash_voucher_last);
        }
    }
    public void delete(Connection connection, long key_cash_voucher_first, long key_cash_voucher_last)
            throws SQLException {
        PreparedStatement ps = connection.prepareStatement(scriptDelete);
        ps.setLong(1,key_cash_voucher_first);
        ps.setLong(2,key_cash_voucher_last);
        System.out.println(ps.executeUpdate());
    }
}
