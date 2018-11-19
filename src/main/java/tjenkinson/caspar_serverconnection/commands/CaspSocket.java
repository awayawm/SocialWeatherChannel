package tjenkinson.caspar_serverconnection.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaspSocket {

    private Socket socket = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private String caspAddress = null;
    private int caspPort = -1;
    private boolean connected = false;
    private Logger logger = Logger.getLogger(getClass().getSimpleName());

    public CaspSocket(String caspAddress, int caspPort) throws IOException {
        this.caspAddress = caspAddress;
        this.caspPort = caspPort;
        open(); // make connection
    }

    private synchronized void open() throws UnknownHostException, IOException {
        if (connected) {
            return;
        }
        socket = new Socket(InetAddress.getByName(caspAddress), caspPort);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        connected = true;
    }

    public synchronized void close() throws IOException {
        if (!connected) {
            return;
        }
        out.close();
        in.close();
        socket.close();
        socket = null;
        in = null;
        out = null;
        connected = false;
    }


    public synchronized CaspReturn runCmd(CaspCmd cmd) throws IOException {
        open(); // connect if not already
        out.println(new String(cmd.getCmdString().getBytes("UTF-8"))); // send command through socket
        long requestTime = new Date().getTime();
        String line = in.readLine();
        Matcher myMatcher = Pattern.compile("^\\d+").matcher(line);
        boolean findSuccess = myMatcher.find();
        int status = -1;
        if (findSuccess) {
            status = Integer.parseInt(line.substring(myMatcher.start(), myMatcher.end()));
        }
        return new CaspReturn(status, line, requestTime);
    }
}