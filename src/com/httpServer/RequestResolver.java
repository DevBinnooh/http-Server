/**
 *
 * Title: RequestResolver.java
 * Description: This class is responsible for understanding and handling
 * the request that is received from the browser. It is invoked by the
 * HttpServer.java class
 * @Author DevBinnooh & Bader A
 */

package com.httpServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class RequestResolver {

    private EnvVars m_vars;

    public RequestResolver(Socket client){
        m_vars = new EnvVars();

        InputStreamReader ir;
        try {
            ir = new InputStreamReader(client.getInputStream());
        } catch (IOException ex) {
            return;
        }
        BufferedReader reader = new BufferedReader(ir);

        try{
            String line = reader.readLine();
            if(line != null && !line.equals(""))
                ParseRequest(line.split(" "));

            while(line != null && !line.equals("")){
                ParseAtribute(line.split(":",2));
                line = reader.readLine();
            }
        }
        catch(Exception e){
        }
    }

    private void ParseRequest(String[] elements ){

        if(elements.length>1){

            String method = elements[0];
            String request = elements[1];

            String[] qSplit = request.split("\\?");
            if(qSplit.length>1){
                m_vars.QUERY_STRING = qSplit[1];
                m_vars.SCRIPT_FILENAME = m_vars.DOCUMENT_ROOT + qSplit[0];
                m_vars.SCRIPT_NAME = qSplit[0];
            }
            else{
                m_vars.SCRIPT_FILENAME = m_vars.DOCUMENT_ROOT + request;
                m_vars.SCRIPT_NAME = request;
            }

            m_vars.REQUEST_URI = request;

            method = method.toUpperCase();
            if(method.equals("GET") || method.equals("POST") ||method.equals("PUT"))
                m_vars.REQUEST_METHOD = method;
        }

    }

    private void ParseAtribute(String[] elements){
        if(!(elements.length>1)){
            return;
        }
        String key = elements[0].trim();
        String val = elements[1].trim();

        if(key.equals("Host")){
            m_vars.HTTP_HOST = val;
        }
        else if(key.equals("User-Agent")){
            m_vars.HTTP_USER_AGENT = val;
        }
        else if(key.equals("Referer")){
            m_vars.HTTP_REFERER = val;
        }
        else if(key.equals("Cookie")){
            m_vars.HTTP_COOKIE = val;
        }
    }

    public EnvVars GetEnvVars(){
        return m_vars;
    }

}
