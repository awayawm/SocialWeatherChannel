package tjenkinson.caspar_serverconnection.commands;

public class CaspCmd {

    private String cmdString;

    public CaspCmd(String cmdString) {
        this.cmdString = cmdString;
    }

    public void setCmdString(String cmdString) {
        this.cmdString = cmdString;
    }

    public String getCmdString() {
        return cmdString;
    }
}
