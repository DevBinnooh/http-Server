/**
 *
 * Title: HttpServer.java
 * Description: This class is invoked by the main class, then it would be
 * responsible for invoking the other classes: HttpConf.java, EnvVars.java,
 * RequestResolver.java, and RequestResponse.java
 * @Author DevBinnooh & Bader A
 */
package com.httpServer;

import java.net.ServerSocket;
import java.net.Socket;

class HttpServer extends Thread {

    private int PortNumber;
    private String path = "src/config/httpd.conf";
    private String MIME_path = "src/config/mime.types";

    public HttpServer() {
    }

    @Override
    public void run() {
        HttpdConf conf = new HttpdConf();
        if (!HttpdConf.readHttpd(path)) {
            return;
        }
        HttpdConf.readMIME(MIME_path);

        PortNumber = HttpdConf.Port;

        ServerSocket serversocket = null;
        try {
            serversocket = new ServerSocket(PortNumber);
        } catch (Exception e) {
            System.out.println("Cant create server using port" + PortNumber);
            return;
        }
        conf.testPrint();
        while (true) {

            try {
                Socket accept = serversocket.accept();
                System.out.println("Client connected on port: " + PortNumber + "\n");
                RequestResolver client = new RequestResolver(accept);
                RequestResponse respClient = new RequestResponse(client.GetEnvVars(), accept);
                System.out.println("Client Method: " + client.GetEnvVars().REQUEST_METHOD +
                                    "\nRequested URI: "+ client.GetEnvVars().REQUEST_URI+
                                    "\nClient Agent: " + client.GetEnvVars().HTTP_USER_AGENT);

            } catch (Exception e) {
            }
        }
    }
}
