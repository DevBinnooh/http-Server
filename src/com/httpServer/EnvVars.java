/**
 * 
 * Title: EnvVars.java
 * Description: This class includes all the environment variables needed in this
 * project
 * @Author DevBinnooh & Bader A
 */

package com.httpServer;

public class EnvVars {

    public String DOCUMENT_ROOT;    //The root directory of your server
    public String HTTP_COOKIE;      //The visitor's cookie, if one is set
    public String HTTP_HOST;        //The hostname of the page being attempted
    public String HTTP_REFERER;     //The URL of the page that called your program
    public String HTTP_USER_AGENT;  //The browser type of the visitor
    public String HTTPS;            //"on" if the program is being called through a secure server
    public String PATH;             //The system path your server is running under
    public String QUERY_STRING;     //The query string (see GET, below)
    public String REMOTE_ADDR;      //The IP address of the visitor
    public String REMOTE_HOST;      //The hostname of the visitor (if your server has reverse-name-lookups on; otherwise this is the IP address again)
    public String REMOTE_PORT;      //The port the visitor is connected to on the web server
    public String REMOTE_USER;      //The visitor's username (for .htaccess-protected pages)
    public String REQUEST_METHOD;   //GET or POST
    public String REQUEST_URI;      //The interpreted pathname of the requested document or CGI (relative to the document root)
    public String SCRIPT_FILENAME;  //The full pathname of the current CGI
    public String SCRIPT_NAME;      //The interpreted pathname of the current CGI (relative to the document root)
    public String SERVER_ADMIN;     //The email address for your server's webmaster
    public String SERVER_NAME;      //Your server's fully qualified domain name (e.g. www.cgi101.com)
    public String SERVER_PORT;      //The port number your server is listening on
    public String SERVER_SOFTWARE;  //The server software you're using (e.g. Apache 1.3)

    public EnvVars() {
        DOCUMENT_ROOT = HttpdConf.getDocument_Root();
        PATH = HttpdConf.getServer_Root();
        SERVER_ADMIN = HttpdConf.getServer_Admin();
        SERVER_NAME = HttpdConf.getM_serverName();
        SERVER_PORT = HttpdConf.getPort().toString();
    }

    String[] GetEnv() {

        String[] env = new String[20];

        env[0] = "DOCUMENT_ROOT=" + DOCUMENT_ROOT;
        env[1] = "HTTP_COOKIE=" + HTTP_COOKIE;
        env[2] = "HTTP_HOST=" + HTTP_HOST;
        env[3] = "HTTP_REFERER=" + HTTP_REFERER;
        env[4] = "HTTP_USER_AGENT=" + HTTP_USER_AGENT;
        env[5] = "HTTPS=" + HTTPS;
        env[6] = "PATH=" + PATH;
        env[7] = "QUERY_STRING=" + QUERY_STRING;
        env[8] = "REMOTE_ADDR=" + REMOTE_ADDR;
        env[9] = "REMOTE_HOST=" + REMOTE_HOST;
        env[10] = "REMOTE_PORT=" + REMOTE_PORT;
        env[11] = "REMOTE_USER=" + REMOTE_USER;
        env[12] = "REQUEST_METHOD=" + REQUEST_METHOD;
        env[13] = "REQUEST_URI=" + REQUEST_URI;
        env[14] = "SCRIPT_FILENAME=" + SCRIPT_FILENAME;
        env[15] = "SCRIPT_NAME=" + SCRIPT_NAME;
        env[16] = "SERVER_ADMIN=" + SERVER_ADMIN;
        env[17] = "SERVER_NAME=" + SERVER_NAME;
        env[18] = "SERVER_PORT=" + SERVER_PORT;
        env[19] = "SERVER_SOFTWARE=" + SERVER_SOFTWARE;

        return env;
    }
}
