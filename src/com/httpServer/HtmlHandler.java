/**
 *
 *
 *
 * Title: HtmlHandler.java
 * Description: This class is invoked by the RequestResponse.java class, and it
 * is responsible for handling HTML
 * @Author DevBinnooh & Bader A
 */
package com.httpServer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;

class HtmlHandler {

    private static String outputHeader;
    private static String outputBody;

    static void ProcessRequest(EnvVars vars, Socket client) {


        String repsonseHeader = null;
        BufferedInputStream bis = null;
        File rFile = new File(vars.DOCUMENT_ROOT + vars.REQUEST_URI);
        if (!rFile.exists() || !rFile.isFile()) {
            System.out.println("File does not exist");
            return;
        }
        BufferedReader in = null;

        if (vars.REQUEST_METHOD.equals("PUT")) {
            MakePut(vars, client);
            repsonseHeader = RequestResponse.GenerateResponseHeader(201, null, 0);
        } else {
            try {
                in = new BufferedReader(new FileReader(vars.DOCUMENT_ROOT + vars.REQUEST_URI));
            } catch (Exception e) {
                System.out.println("Error in opening File " + rFile.getAbsolutePath());
            }
        }

        long contentLegth = rFile.length();
        String mimeType = HttpdConf.getTypemm(rFile.getAbsolutePath());
        if (!(mimeType.equals("application/octet-stream")) && null != mimeType) {
            repsonseHeader = RequestResponse.GenerateResponseHeader(200, mimeType, contentLegth);
        } else {
            repsonseHeader = RequestResponse.GenerateResponseHeader(404, mimeType, 0);
        }
        try {
            while (in.ready() && in.readLine() != null) {
                outputBody += in.readLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(HtmlHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            if (in != null) {
                byte[] messageHeaders;
                byte[] messageBody;
                OutputStream out = client.getOutputStream();
                outputHeader = repsonseHeader + "\r\n";
                try {
                    messageHeaders = outputHeader.getBytes("UTF-8");
                    messageBody = outputBody.getBytes("UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    messageHeaders = outputHeader.getBytes();
                    messageBody = outputBody.getBytes();
                }

                out.write(messageHeaders);
                out.write(messageBody);

                wr.flush();
            }
            wr.close();
        } catch (Exception e) {
            System.out.println("Error sending response: " + e.toString());
        }
    }

    private static void MakePut(EnvVars vars, Socket client) {
        try {
            FileWriter fstream = new FileWriter(vars.SCRIPT_FILENAME);
            BufferedWriter out = new BufferedWriter(fstream);
            InputStream istr = client.getInputStream();

            int c = istr.read();
            while (c > 0) {
                out.write(c);
                c = istr.read();
            }
            out.close();

        } catch (Exception e) {
        }
    }

    protected void setHeader(String header, String value) {
    }
}
