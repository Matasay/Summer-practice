package summerprac.services;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Properties;

public class ControlService {
    private Properties serverConf;
    private static ControlService instance;

    private Properties InitConfig(){
        boolean isReadOk = true;
        System.out.println("Fetching server config");
        ClassLoader cll = Thread.currentThread().getContextClassLoader();
        Properties confProp = new Properties();
        try (InputStream propIS = cll.getResourceAsStream("config.prop")){
            confProp.load(propIS);
            System.out.println("config file loaded");
            if (!confProp.containsKey("username")) {
                confProp.setProperty("username","russa");
                System.out.println("Username not found: set default 'russa'");
                isReadOk = false;
            }
            if (!confProp.containsKey("password")) {
                confProp.setProperty("password","StReLock97");
                System.out.println("Password not found: set default '*******'");
                isReadOk = false;
            }
            if (!confProp.containsKey("url")){
                confProp.setProperty("url", String.format("jdbc:postgresql://%s:%s/%s", "localhost", 5432, "app_software_store"));
                System.out.println("Connection URL not found: set default: "+ confProp.getProperty("url"));
                isReadOk = false;
            }
            if (!confProp.containsKey("script_file")){
                confProp.setProperty("script_file","SQLScripts.xml");
                System.out.println("Script file name not found: set default 'SQLScripts.xml'");
                isReadOk = false;
            }
        }
        catch (IOException ex){
            System.out.println("Server config not found: setting defaults");
            confProp.setProperty("username","russa");
            confProp.setProperty("password","StReLock97");
            confProp.setProperty("url", String.format("jdbc:postgresql://%s:%s/%s", "localhost", 5432, "app_software_store"));
            confProp.setProperty("script_file","SQLScripts.xml");
            isReadOk = false;
        }
        if (!isReadOk) {
            try (PrintWriter writer = new PrintWriter(new File(cll.getResource("config.prop").getPath()))) {
                System.out.println("Writing server config to file: 'config.prop'...");
                confProp.store(writer, LocalDateTime.now().toString());
                System.out.println("Write successful");
            }
            catch (Exception ex){
                System.out.println("Can't write to file!");
            }
        }
        return confProp;
    }

    private ControlService(){
        serverConf = InitConfig();
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSQL driver ready");
        }
        catch (ClassNotFoundException ex){
            System.out.println("PostgreSQL driver not loaded!");
        }
    }

    public static ControlService getInstance(){
        return instance != null ? instance : new ControlService();
    }

    public static void InitInstance(){
        if (instance == null){
            instance = new ControlService();
        }
    }

    public final Connection getConnection() throws SQLException {
        return DriverManager.getConnection(serverConf.getProperty("url"), serverConf);
    }

    //[0]select,[1]getall,[2]update,[3]insert,[4]delete
    public String[] getSQLScripts(String tableName){
        String[] scripts = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            ClassLoader cll = Thread.currentThread().getContextClassLoader();
            String filename = serverConf.getProperty("script_file");
            InputStream is = cll.getResourceAsStream(filename);
            if (is == null){
                throw new NullPointerException("script_file not found");
            }
            Document document = builder.parse(is);
            NodeList nodeList = document.getElementsByTagName("scripts");
            Element e = (Element)nodeList.item(0);
            nodeList = e.getElementsByTagName("table");
            NamedNodeMap nnm = null;
            Node n = null;
            for (short i = 0; i < nodeList.getLength(); ++i){
                e = (Element)nodeList.item(i);
                nnm = e.getAttributes();
                if (nnm != null){
                   n = nnm.getNamedItem("name");
                   if (n != null){
                      if (n.getNodeValue().equals(tableName)){
                          break;
                      }
                   }
                }
            }
            if (n != null){
                scripts = new String[5];
                nodeList = e.getElementsByTagName("operation");
                for (short i = 0; i < nodeList.getLength(); ++i){
                    n = nodeList.item(i);
                    nnm = n.getAttributes();
                    n = nnm.getNamedItem("name");
                    e = (Element)nodeList.item(i);
                    if (n != null){
                        switch (n.getNodeValue()){
                            case "select": {
                                scripts[0] = e.getChildNodes().item(0).getNodeValue();
                                break;
                            }
                            case "getall":{
                                scripts[1] = e.getChildNodes().item(0).getNodeValue();
                                break;
                            }
                            case "update":{
                                scripts[2] = e.getChildNodes().item(0).getNodeValue();
                                break;
                            }
                            case "insert":{
                                scripts[3] = e.getChildNodes().item(0).getNodeValue();
                                break;
                            }
                            case "delete":{
                                scripts[4] = e.getChildNodes().item(0).getNodeValue();
                                break;
                            }
                        }
                    }
                }
            }
        }
        catch (Exception ex ){
            System.out.println(ex.getMessage());
            System.out.println(String.format("Scripts for table '%s' not found", tableName));
        }
        return scripts;
    }
}
