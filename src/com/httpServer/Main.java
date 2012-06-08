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

public class Main {

    public static void main(String[] args) {
        final String path = "src/config/httpd.conf";
        final String MIME_path = "src/config/mime.types";

        if (!HttpdConf.readHttpd(path)) {
            return;//TODO report error and print absolute path
        }
        HttpdConf.readMIME(MIME_path);

        ServerSocket serversocket = null;
        try {
            serversocket = new ServerSocket(HttpdConf.Port);
        } catch (Exception e) {
            System.out.println("Cant create server using port" + HttpdConf.Port);
            return;
        }
        HttpdConf.testPrint();

        while (true) {
            try {
                
                Socket accept = serversocket.accept();
                System.out.println("Client connected on port: " + HttpdConf.Port + "\n");
                new Thread(new HttpServer(accept)).start();
            } catch (Exception e) {
            }

        }
    }
}
