/**
 * Http Java Server that receives and sends http requests.
 * 
 * Description: This is the main class in the project
 * @Author DevBinnooh & Bader A
 * @link http://www.binnooh.com
 */
package com.httpServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        final String path = "src/config/httpd.conf";
        final String MIME_path = "src/config/mime.types";
        HttpdConf serverConf = HttpdConf.getInstance();
        if (!serverConf.readHttpd(path)) {
            return;//TODO report error and print absolute path
        }
        serverConf.readMIME(MIME_path);

        ServerSocket serversocket = null;
        try {
            serversocket = new ServerSocket(HttpdConf.getPort());
        } catch (Exception e) {
            System.out.println("Cant create server using port" + HttpdConf.getPort());
            return;
        }
        HttpdConf.testPrint();

        while (true) {
            try {
                
                Socket accept = serversocket.accept();
                System.out.println("Client connected on port: " + HttpdConf.getPort() + "\n");
                new Thread(new HttpServer(accept)).start();
            } catch (Exception e) {
                Logger log = Logger.getLogger(HttpdConf.getLog_File());
                log.log(Level.WARNING, "httpServer: Client Can not be connected to {0}", HttpdConf.getPort());
            }

        }
    }
}
