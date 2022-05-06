package com.example.imagemachine.modal;

public class Machine {
    private String MachineID;
    private String MachineName;
    private String MachineType;
    private String MachineQr;
    private String MachineMt;

    public Machine(String machineID, String machineName, String machineType, String machineQr, String machineMt) {
        MachineID = machineID;
        MachineName = machineName;
        MachineType = machineType;
        MachineQr = machineQr;
        MachineMt = machineMt;
    }

    public String getMachineID() {
        return MachineID;
    }

    public void setMachineID(String machineID) {
        MachineID = machineID;
    }

    public String getMachineName() {
        return MachineName;
    }

    public void setMachineName(String machineName) {
        MachineName = machineName;
    }

    public String getMachineType() {
        return MachineType;
    }

    public void setMachineType(String machineType) {
        MachineType = machineType;
    }

    public String getMachineQr() {
        return MachineQr;
    }

    public void setMachineQr(String machineQr) {
        MachineQr = machineQr;
    }

    public String getMachineMt() {
        return MachineMt;
    }

    public void setMachineMt(String machineMt) {
        MachineMt = machineMt;
    }
}
