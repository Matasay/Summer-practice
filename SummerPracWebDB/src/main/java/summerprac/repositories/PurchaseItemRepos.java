package summerprac.repositories;

import summerprac.model.PurchaseItem;
import summerprac.services.ControlService;

import java.sql.*;
import java.util.ArrayList;

public class PurchaseItemRepos {
    private static PurchaseItemRepos instance;
    private String scriptSelect;
    private String scriptGetAll;
    private String scriptUpdate;
    private String scriptInsert;
    private String scriptDelete;
    private String err_msg;

    public static PurchaseItemRepos getInstance() {
        return instance != null ? instance : new PurchaseItemRepos();
    }

    private PurchaseItemRepos() {
        String[] scripts = ControlService.getInstance().getSQLScripts("purchase_item");
        if (scripts != null) {
            scriptSelect = scripts[0];
            scriptGetAll = scripts[1];
            scriptUpdate = scripts[2];
            scriptInsert = scripts[3];
            scriptDelete = scripts[4];
        }
        else{
            System.out.println("Table: 'purchase_item' scripts not found");
        }
        err_msg = "'purchase_item' record with this email and cash_voucher  ";
    }

    private SQLException notFoundException(){
        return new SQLException(err_msg + "not found.");
    }

    private SQLException alreadyException(){
        return new SQLException(err_msg + "already in database.");
    }

    private PurchaseItem convertToModel(ResultSet rs) throws SQLException{
        return new PurchaseItem(
                rs.getString(1),
                rs.getLong(2),
                rs.getLong(3),
                rs.getShort(4),
                rs.getString(5).replace('?','$')
        );
    }

    private PreparedStatement getPurItemStatement(PreparedStatement ps, int account_id, int purchase_id,
                                                  short copies_count, String price_copy) throws SQLException{
        ps.setInt(1,account_id);
        ps.setInt(2, purchase_id);
        ps.setShort(3, copies_count);
        ps.setString(4, price_copy.replace('.',','));
        return ps;
    }

    public int getIdByKey(Connection connection, String key_email, long key_cash_voucher_first,
                          long key_cash_voucher_last) throws SQLException{
        PreparedStatement ps = connection.prepareStatement(scriptSelect);
        ps.setString(1,key_email);
        ps.setLong(2,key_cash_voucher_first);
        ps.setLong(3,key_cash_voucher_last);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return rs.getInt(6);
        }
        throw notFoundException();
    }

    public PurchaseItem select(Connection connection, String key_email, long key_cash_voucher_first,
                               long key_cash_voucher_last) throws SQLException{
        PreparedStatement ps = connection.prepareStatement(scriptSelect);
        ps.setString(1,key_email);
        ps.setLong(2,key_cash_voucher_first);
        ps.setLong(3,key_cash_voucher_last);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return convertToModel(rs);
        }
        throw notFoundException();
    }

    public ArrayList<PurchaseItem> getAll(Connection connection) throws SQLException{
        ArrayList<PurchaseItem> purchaseItems = new ArrayList<>();
        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery(scriptGetAll);
        PurchaseItem pi;
        while (rs.next()){
            pi = convertToModel(rs);
            purchaseItems.add(pi);
        }
        return purchaseItems;
    }

    public void update(Connection connection, int key_account_id, int key_purchase_id, int account_id,
                       int purchase_id, short copies_count, String price_copy) throws SQLException{
        PreparedStatement ps = connection.prepareStatement(scriptUpdate);
        ps = getPurItemStatement(ps, account_id, purchase_id, copies_count, price_copy);
        ps.setInt(5,key_account_id);
        ps.setInt(6,key_purchase_id);
        System.out.println(ps.executeUpdate());
    }

    public void insert(Connection connection, int account_id, int purchase_id, short copies_count, String price_copy)
            throws SQLException{
        try {
            PreparedStatement ps = connection.prepareStatement(scriptInsert);
            ps = getPurItemStatement(ps,account_id,purchase_id,copies_count,price_copy);
            ps.executeUpdate();
        }
        catch (SQLException ex){
            throw alreadyException();
        }
    }

    public void delete(Connection connection, int key_account_id, int key_purchase_id) throws SQLException{
        PreparedStatement ps =connection.prepareStatement(scriptDelete);
        ps.setInt(1,key_account_id);
        ps.setInt(2,key_purchase_id);
        System.out.println(ps.executeUpdate());
    }
}
