/**
 * Http Java Server that receives and sends http requests.
 * 
 * Description: This is the main class in the project
 * @Author DevBinnooh & Bader A
 * @link http://www.binnooh.com
 */
package com.httpServer;

public class Main {

    public static void main(String[] args) {
        HttpServer Server = new HttpServer();
        Server.start();

    }
}
