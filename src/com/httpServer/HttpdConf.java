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

public class HttpdConf {

    public static String Server_Root = null;
    public static String Document_Root = null;
    public static String Server_Admin = null;
    private static String Log_File = null;
    private static String Uploads = null;
    private static HashMap<String, String> Script_Alias = new HashMap<String, String>();
    private static HashMap<String, String> Alias = new HashMap<String, String>();
    private static String Temp_Directory = null;
    public static Integer Port = 0; // Check Issues
    private static int Max_Thread = 0;
    private static String Cache_Enabled = null;
    private static String Persistent_Connection = null;
    public static List<String> Directory_Index = new ArrayList();
    private HashMap<String, Vector<String>> Directory_Access = new HashMap<String, Vector<String>>();
    private HashMap<String, String> Cgi_Handler = new HashMap<String, String>();
    private HashMap<String, String> Add_Icon_By_Type = new HashMap<String, String>();
    private HashMap<String, Vector<String>> Add_Icon = new HashMap<String, Vector<String>>();
    private static String Default_Icon = null;
    public final static String m_serverName = "DevBinnooh & Bader A";
    private static TreeMap<String, String> m_types = new TreeMap<String, String>();

    /**
     * Default constructor to reset your variables and data structures.
     */
    public HttpdConf() {
    }

    /**
     * Reads in an httpd.conf file, parses it and saves the data stored within that
     * file. This allows for proper configuration of your server since the
     * information stored in your configuration file should allow for your server
     * to function.
     *
     * @param path path to the httpd.conf file
     */
    public static boolean readHttpd(String path) {

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

    private static void ParseConfigLine(String currentLine) {
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
                Server_Root = val1.replaceAll("\"", "");
            } else if (key.equals("ServerAdmin")) {
                Server_Admin = val1;
            } else if (key.equals("DocumentRoot")) {
                Document_Root = val1.replaceAll("\"", "");
            } else if (key.equals("Listen")) {
                Port = Integer.parseInt(val1);
            } else if (key.equals("LogFile")) {
                Log_File = val1;
            } else if (key.equals("ScriptAlias")) {
                Script_Alias.put(val1, val2);
            } else if (key.equals("Alias")) {
                Alias.put(val1, val2);
            } else if (key.equals("DirectoryIndex")) {
                Collections.addAll(Directory_Index, elements);
            } else if (key.equals("Listen")) {
                Port = Integer.getInteger(val1);
            } else if (key.equals("MaxThreads")) {
                Max_Thread = Integer.parseInt(val1);
            } else if (key.equals("KeepAlive")) {
                Persistent_Connection = val1;
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
        return Alias.get(fakeName);

    }

    /**
     * Used to read the mime.types file and save all the information from that file
     * into a data structure that can be used to validate file types when
     * generating response messages.
     *
     * @param path String value of path to mime.types file
     */
    public static boolean readMIME(String path) {
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
                m_types.put(types[i], elements[0]);
            }
        }
    }

    public static String GetType(String ext) {
        String type = m_types.get(ext);
        if (type != null) {
            return type;
        }

        return "application/octet-stream";

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
    public void testPrint() {
        System.out.println("ServerOwners: " + m_serverName);
        System.out.println("ServerRoot: " + Server_Root);
        System.out.println("DocumentRoot: " + Document_Root);
        System.out.println("ListenPort: " + Port);
        System.out.println("LogFile: " + Log_File);
    }
}
