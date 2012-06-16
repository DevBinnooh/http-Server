/**
 * 
 * Title: HttpdConf.java
 * Description: Description: This class will configure the server to the
 * specifications found within the httpd.conf file.
 * @Author DevBinnooh & Bader A
 */
package com.httpServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;

public class HttpdConf {

    private static String Server_Root = null;
    private static String Document_Root = null;
    private static String Server_Admin = null;
    private static String Log_File = null;
    private static String Uploads = null;
    private static HashMap<String, String> Script_Alias = new HashMap<String, String>();
    private static HashMap<String, String> Alias = new HashMap<String, String>();
    private static String Temp_Directory = null;
    private static Integer Port = 0; // Check Issues
    private static int Max_Thread = 0;
    private static String Cache_Enabled = null;
    private static String Persistent_Connection = null;
    private static List<String> Directory_Index = new ArrayList();
    private HashMap<String, Vector<String>> Directory_Access = new HashMap<String, Vector<String>>();
    private HashMap<String, String> Cgi_Handler = new HashMap<String, String>();
    private HashMap<String, String> Add_Icon_By_Type = new HashMap<String, String>();
    private HashMap<String, Vector<String>> Add_Icon = new HashMap<String, Vector<String>>();
    private static String Default_Icon = null;
    private final static String m_serverName = "DevBinnooh & Bader A";
    private static TreeMap<String, String> m_types = new TreeMap<String, String>();
    private static MimetypesFileTypeMap mm_types ;
    private static HttpdConf instance;
    /**
     * Default constructor to reset your variables and data structures.
     */
    private HttpdConf() {
        try {
            mm_types = new MimetypesFileTypeMap("src/config/mime.types");
        } catch (IOException ex) {
            Logger.getLogger(HttpdConf.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized static  HttpdConf getInstance (){
        if (null == instance){
            instance = new HttpdConf();
        }
        return instance;
    }

    /**
     * Reads in an httpd.conf file, parses it and saves the data stored within that
     * file. This allows for proper configuration of your server since the
     * information stored in your configuration file should allow for your server
     * to function.
     *
     * @param path path to the httpd.conf file
     */
    public boolean readHttpd(String path) {

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(path));
            String currentLine = in.readLine(); // reading the first line
            /*
             * While loop is needed to read all lines
             * While Parser is needed inside the loop
             */
            while (currentLine != null) {
                ParseConfigLine(currentLine);
                currentLine = in.readLine();
            }
            in.close();
        } catch (IOException ex) {
            System.out.println("Can't load config file");
            Logger.getLogger(HttpdConf.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;

    }

    private synchronized static void ParseConfigLine(String currentLine) {
        if (currentLine.startsWith("#")) {
            return;
        }
        String[] elements = currentLine.split(" ");
        String key, val1, val2;
        if (elements.length > 1) {
            key = elements[0];
            val1 = elements[1];
            if (elements.length > 2) {
                val2 = elements[2];
            } else {
                val2 = null;
            }
            if (key.equals("ServerRoot")) {
                setServer_Root(val1.replaceAll("\"", ""));
            } else if (key.equals("ServerAdmin")) {
                setServer_Admin(val1);
            } else if (key.equals("DocumentRoot")) {
                setDocument_Root(val1.replaceAll("\"", ""));
            } else if (key.equals("Listen")) {
                setPort((Integer) Integer.parseInt(val1));
            } else if (key.equals("LogFile")) {
                setLog_File(val1);
            } else if (key.equals("ScriptAlias")) {
                getScript_Alias().put(val1, val2);
            } else if (key.equals("Alias")) {
                getAlias().put(val1, val2);
            } else if (key.equals("DirectoryIndex")) {
                Collections.addAll(getDirectory_Index(), elements);
            } else if (key.equals("Listen")) {
                setPort(Integer.getInteger(val1));
            } else if (key.equals("MaxThread")) {
                setMax_Thread(Integer.parseInt(val1));
            } else if (key.equals("KeepAlive")) {
                setPersistent_Connection(val1);
            }
        }

    }

    /**
     * Function to convert aliases set within the httpd.conf file to their
     * absolute path. This allows for aliases to be found on the server and
     * returned back to the client.
     *
     * @param fakeName String which contains the alias of the file or directory
     * @return String value which contains the absolute path to the file or
     *   directory as determined within the httpd.conf file
     */
    public String solveAlias(String fakeName) {
        return getAlias().get(fakeName);

    }

    /**
     * Used to read the mime.types file and save all the information from that file
     * into a data structure that can be used to validate file types when
     * generating response messages.
     *
     * @param path String value of path to mime.types file
     */
    public boolean readMIME(String path) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(path));

            String line = in.readLine();
            while (line != null) {
                ParseLine(line.trim());
                line = in.readLine();
            }
            in.close();
        } catch (IOException ex) {
            System.out.println("Can not open: " + path + "\n please make sure you include the file in the same directory of jar");
            Logger.getLogger(HttpdConf.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    private static void ParseLine(String line) {
        if (line.startsWith("#")) {
            return;
        }

        String[] elements = line.split("\\t");
        if (elements.length > 1) {
            String types[] = elements[elements.length - 1].split(" ");
            for (int i = 0; i < types.length; i++) {
                getM_types().put(types[i], elements[0]);
            }
        }
    }

    public static String GetType(String ext) {
        String type = getM_types().get(ext);
        if (type != null) {
            return type;
        }

        return "application/octet-stream";
    }
    
    public static String getTypemm(String path){
        return mm_types.getContentType(path);
        }

    /**
     * Helper function to determine whether the name of a file or directory is an
     * alias for another file or directory as noted in the httpd.conf file.
     *
     * @param name String value of the alias we want to check to determine
     *   whether it is or is not an alias for another file or directory
     * @return true if it is an alias, false otherwise
     */
    public boolean isScript(String name) {
        return false;
    }

    /**
     * Helper function to see if we've parsed our httpd.conf file properly. Used
     * for debugging purposes.
     */
    public static void testPrint() {
        System.out.println("ServerOwners: " + getM_serverName());
        System.out.println("ServerRoot: " + getServer_Root());
        System.out.println("DocumentRoot: " + getDocument_Root());
        System.out.println("ListenPort: " + getPort());
        System.out.println("LogFile: " + getLog_File());
    }
 /**
     * @return the Log_File
     */
    public static String getLog_File() {
        return Log_File;
    }

    /**
     * @param aLog_File the Log_File to set
     */
    public static void setLog_File(String aLog_File) {
        Log_File = aLog_File;
    }

    
    /**
     * @return the Document_Root
     */
    public static String getDocument_Root() {
        return Document_Root;
    }

    /**
     * @param aDocument_Root the Document_Root to set
     */
    public static void setDocument_Root(String aDocument_Root) {
        Document_Root = aDocument_Root;
    }

    /**
     * @return the Server_Root
     */
    public static String getServer_Root() {
        return Server_Root;
    }

    /**
     * @param aServer_Root the Server_Root to set
     */
    public static void setServer_Root(String aServer_Root) {
        Server_Root = aServer_Root;
    }

    /**
     * @return the Server_Admin
     */
    public static String getServer_Admin() {
        return Server_Admin;
    }

    /**
     * @param aServer_Admin the Server_Admin to set
     */
    public static void setServer_Admin(String aServer_Admin) {
        Server_Admin = aServer_Admin;
    }
    /**
     * @return the Uploads
     */
    public static String getUploads() {
        return Uploads;
    }

    /**
     * @param aUploads the Uploads to set
     */
    public static void setUploads(String aUploads) {
        Uploads = aUploads;
    }

    /**
     * @return the Script_Alias
     */
    public static HashMap<String, String> getScript_Alias() {
        return Script_Alias;
    }

    /**
     * @param aScript_Alias the Script_Alias to set
     */
    public static void setScript_Alias(HashMap<String, String> aScript_Alias) {
        Script_Alias = aScript_Alias;
    }

    /**
     * @return the Alias
     */
    public static HashMap<String, String> getAlias() {
        return Alias;
    }

    /**
     * @param aAlias the Alias to set
     */
    public static void setAlias(HashMap<String, String> aAlias) {
        Alias = aAlias;
    }

    /**
     * @return the Temp_Directory
     */
    public static String getTemp_Directory() {
        return Temp_Directory;
    }

    /**
     * @param aTemp_Directory the Temp_Directory to set
     */
    public static void setTemp_Directory(String aTemp_Directory) {
        Temp_Directory = aTemp_Directory;
    }

    /**
     * @return the Port
     */
    public static Integer getPort() {
        return Port;
    }

    /**
     * @param aPort the Port to set
     */
    public static void setPort(Integer aPort) {
        Port = aPort;
    }

    /**
     * @return the Max_Thread
     */
    public static int getMax_Thread() {
        return Max_Thread;
    }

    /**
     * @param aMax_Thread the Max_Thread to set
     */
    public static void setMax_Thread(int aMax_Thread) {
        Max_Thread = aMax_Thread;
    }

    /**
     * @return the Cache_Enabled
     */
    public static String getCache_Enabled() {
        return Cache_Enabled;
    }

    /**
     * @param aCache_Enabled the Cache_Enabled to set
     */
    public static void setCache_Enabled(String aCache_Enabled) {
        Cache_Enabled = aCache_Enabled;
    }

    /**
     * @return the Persistent_Connection
     */
    public static String getPersistent_Connection() {
        return Persistent_Connection;
    }

    /**
     * @param aPersistent_Connection the Persistent_Connection to set
     */
    public static void setPersistent_Connection(String aPersistent_Connection) {
        Persistent_Connection = aPersistent_Connection;
    }

    /**
     * @return the Directory_Index
     */
    public static List<String> getDirectory_Index() {
        return Directory_Index;
    }

    /**
     * @param aDirectory_Index the Directory_Index to set
     */
    public static void setDirectory_Index(List<String> aDirectory_Index) {
        Directory_Index = aDirectory_Index;
    }

    /**
     * @return the Default_Icon
     */
    public static String getDefault_Icon() {
        return Default_Icon;
    }

    /**
     * @param aDefault_Icon the Default_Icon to set
     */
    public static void setDefault_Icon(String aDefault_Icon) {
        Default_Icon = aDefault_Icon;
    }

    /**
     * @return the m_serverName
     */
    public static String getM_serverName() {
        return m_serverName;
    }

    /**
     * @return the m_types
     */
    public static TreeMap<String, String> getM_types() {
        return m_types;
    }

    /**
     * @param aM_types the m_types to set
     */
    public static void setM_types(TreeMap<String, String> aM_types) {
        m_types = aM_types;
    }
    /**
     * @return the Directory_Access
     */
    public HashMap<String, Vector<String>> getDirectory_Access() {
        return Directory_Access;
    }

    /**
     * @param Directory_Access the Directory_Access to set
     */
    public void setDirectory_Access(HashMap<String, Vector<String>> Directory_Access) {
        this.Directory_Access = Directory_Access;
    }

    /**
     * @return the Cgi_Handler
     */
    public HashMap<String, String> getCgi_Handler() {
        return Cgi_Handler;
    }

    /**
     * @param Cgi_Handler the Cgi_Handler to set
     */
    public void setCgi_Handler(HashMap<String, String> Cgi_Handler) {
        this.Cgi_Handler = Cgi_Handler;
    }

    /**
     * @return the Add_Icon_By_Type
     */
    public HashMap<String, String> getAdd_Icon_By_Type() {
        return Add_Icon_By_Type;
    }

    /**
     * @param Add_Icon_By_Type the Add_Icon_By_Type to set
     */
    public void setAdd_Icon_By_Type(HashMap<String, String> Add_Icon_By_Type) {
        this.Add_Icon_By_Type = Add_Icon_By_Type;
    }

    /**
     * @return the Add_Icon
     */
    public HashMap<String, Vector<String>> getAdd_Icon() {
        return Add_Icon;
    }

    /**
     * @param Add_Icon the Add_Icon to set
     */
    public void setAdd_Icon(HashMap<String, Vector<String>> Add_Icon) {
        this.Add_Icon = Add_Icon;
    }
}
