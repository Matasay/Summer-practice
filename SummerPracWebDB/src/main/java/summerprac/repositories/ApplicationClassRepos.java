package summerprac.repositories;

import summerprac.model.ApplicationClass;
import summerprac.services.ControlService;

import java.sql.*;
import java.util.ArrayList;

public class ApplicationClassRepos {
    private static ApplicationClassRepos instance;

    private String scriptSelect;
    private String scriptGetAll;
    private String scriptUpdate;
    private String scriptInsert;
    private String scriptDelete;
    private String err_msg;

    public static ApplicationClassRepos getInstance() {
        return instance != null ? instance : new ApplicationClassRepos();
    }

    private ApplicationClassRepos() {
        String[] scripts = ControlService.getInstance().getSQLScripts("application_class");
        if (scripts != null) {
            scriptSelect = scripts[0];
            scriptGetAll = scripts[1];
            scriptUpdate = scripts[2];
            scriptInsert = scripts[3];
            scriptDelete = scripts[4];
        }
        else{
            System.out.println("Table: 'application_class' scripts not found");
        }
        err_msg = "'application_class' record with class_name: %s ";
    }

    private SQLException notFoundException(String class_name){
        return new SQLException(String.format(err_msg + "not found.", class_name));
    }

    private SQLException alreadyException(String class_name){
        return new SQLException(String.format(err_msg + "already in database.", class_name));
    }

    private ApplicationClass convertToModel(ResultSet rs) throws SQLException{
        return new ApplicationClass(
                rs.getString(1),
                rs.getString(2)
        );
    }

    private PreparedStatement getAppClassStatement(PreparedStatement ps,
                                                   String class_name, int parent_class_id) throws SQLException{
        ps.setString(1,class_name);
        ps.setInt(2,parent_class_id);
        return ps;
    }

    public int getIdByKey(Connection connection, String key_class_name) throws SQLException{
        PreparedStatement ps = connection.prepareStatement(scriptSelect);
        ps.setString(1,key_class_name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return rs.getInt(3);
        }
        throw notFoundException(key_class_name);
    }

    public ApplicationClass select(Connection connection, String key_class_name) throws SQLException{
        PreparedStatement ps = connection.prepareStatement(scriptSelect);
        ps.setString(1,key_class_name);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return convertToModel(rs);
        }
        throw notFoundException(key_class_name);
    }

    public ArrayList<ApplicationClass> getAll(Connection connection) throws SQLException{
        ArrayList<ApplicationClass> classArrayList = new ArrayList<>();
        Statement s = connection.createStatement();
        ResultSet rs = s.executeQuery(scriptGetAll);
        ApplicationClass ac;
        while (rs.next()){
            ac = convertToModel(rs);
            classArrayList.add(ac);
        }
        return classArrayList;
    }

    public void update(Connection connection, String key_class_name, String class_name, int parent_class_id)
            throws SQLException{
        PreparedStatement ps = connection.prepareStatement(scriptUpdate);
        ps = getAppClassStatement(ps,class_name,parent_class_id);
        ps.setString(3,key_class_name);
        System.out.println(ps.executeUpdate());
    }

    public void insert (Connection connection, String class_name, int parent_class_id) throws SQLException{
        try {
            PreparedStatement ps = connection.prepareStatement(scriptInsert);
            ps = getAppClassStatement(ps, class_name,parent_class_id);
            ps.executeUpdate();
        }
        catch (SQLException ex){
            throw alreadyException(class_name);
        }
    }

    public void delete (Connection connection, String key_class_name) throws SQLException{
        PreparedStatement ps = connection.prepareStatement(scriptDelete);
        ps.setString(1, key_class_name);
        System.out.println(ps.executeUpdate());
    }

}
