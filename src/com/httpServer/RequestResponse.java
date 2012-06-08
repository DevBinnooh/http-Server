/**
 *
 * Title: RequestResponse.java
 * Description: This class is responsible for generating a response that is sent
 * to the browser. It is invoked by the HttpServer.java class
 * @Author DevBinnooh & Bader A
 */

package com.httpServer;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class RequestResponse {

    private BufferedInputStream m_responseContent;
    private long m_contentLength = 0;
    private String m_repsonseHeader;
    private String m_mimeType;

    public RequestResponse(EnvVars vars, Socket client) {
        String fileName = vars.SCRIPT_FILENAME.replaceAll("\"", "");
        File rFile = new File(fileName);
        if (!rFile.exists() || rFile.isDirectory()) {
            for (int i = 0; i < HttpdConf.Directory_Index.size(); i++) {
                rFile = new File(fileName + "/" + HttpdConf.Directory_Index.get(i));
                if (rFile.exists()) {
                    vars.SCRIPT_FILENAME = rFile.getAbsolutePath();
                    break;
                }
            }
        }

        if (!rFile.exists()) {
            m_repsonseHeader = RequestResponse.GenerateResponseHeader(404, null, 0);
            SendReposnse(client);
            return;
        } else {
            m_contentLength = rFile.length();
            HtmlHandler.ProcessRequest(vars, client);

        }


    }

    public static String GenerateResponseHeader(int returnCode, String mime, long contentLenght) {

        String result = returnCode + " ";

        switch (returnCode) {
            case 200:
                result += "OK";
                break;
            case 204:
                result += "No Content";
                break;
            case 302:
                result += "Found";
                break;
            case 304:
                result += "Not Modified";
                break;
            case 400:
                result += "Bad Request";
                break;
            case 401:
                result += "Unauthorized";
                break;
            case 403:
                result += "Forbidden";
                break;
            case 404:
                result += "Not Found";
                break;
            case 500:
                result += "Internal Server Error";
                break;
            case 501:
                result += "Not Implemented";
                break;
        }

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy, HH:mm:ss 'GMT'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String sDate = sdf.format(cal.getTime());

        String response = "HTTP/1.1 " + result + "\r\n"
                + "Date: " + sDate + "\r\n"
                + "Server: " + HttpdConf.m_serverName + "\r\n";
        if (contentLenght > 0) {
            response += "Content-Length: " + contentLenght + "\r\n";
        }
        if (mime != null) {
            response += "Content-Type: " + mime + "\r\n";
        }

        response += "\r\n";


        return response;
    }

    private void SendReposnse(Socket client) {
        try {
            BufferedWriter wr = new BufferedWriter(
                    new OutputStreamWriter(client.getOutputStream()));
            wr.write(getM_repsonseHeader());
            wr.flush();
            if (m_responseContent != null) {
                OutputStream out = client.getOutputStream();
                byte[] loader = new byte[(int) m_contentLength];
                while ((m_responseContent.read(loader)) > 0) {
                    out.write(loader);
                }

                wr.flush();
            }
            wr.close();
        } catch (IOException e) {
            System.out.println("Error sending response: " + e.toString());
        }
    }

    /**
     * @return the m_repsonseHeader
     */
    public String getM_repsonseHeader() {
        return m_repsonseHeader;
    }

    /**
     * @param m_repsonseHeader the m_repsonseHeader to set
     */
    public void setM_repsonseHeader(String m_repsonseHeader) {
        this.m_repsonseHeader = m_repsonseHeader;
    }
}
