/**
 *
 * Title: HttpServer.java
 * Description: This class is invoked by the main class, then it would be
 * responsible for invoking the other classes: HttpConf.java, EnvVars.java,
 * RequestResolver.java, and RequestResponse.java
 * @Author DevBinnooh & Bader A
 */
package com.httpServer;

import java.net.Socket;

class HttpServer implements Runnable {

    private Socket myClient;

    public HttpServer() {
    }

    HttpServer(Socket accept) {
        this.myClient = accept;
    }

    @Override
    public void run() {
        RequestResolver client = new RequestResolver(this.myClient);
        RequestResponse respClient = new RequestResponse(client.GetEnvVars(), this.myClient);
        System.out.println("Client Method: " + client.GetEnvVars().REQUEST_METHOD
                + "\nRequested URI: " + client.GetEnvVars().REQUEST_URI
                + "\nClient Agent: " + client.GetEnvVars().HTTP_USER_AGENT);

    }
}
