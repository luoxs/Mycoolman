package cn.edu.mycoolman;

public class DataRead {
    private byte start;
    private byte power;
    private byte tempcool;
    private byte tempReal;
    private byte frozesetting;
    private byte frozereal;
    private byte turbo;
    private byte heat;
    private byte battery;
    private byte unit;
    private byte status;
    private byte errorcode;
    private byte vhigh;
    private byte vlow;
    private byte gc;
    private byte tempheat;
    private byte timer;
    private byte code1;
    private byte code2;
    private byte crcH;
    private byte crcL;
    private byte end;
    private byte[] data;

    public byte getStart() {
        return start;
    }

    public byte getPower() {
        return power;
    }

    public byte getTempcool() {
        return tempcool;
    }

    public byte getTempReal() {
        return tempReal;
    }

    public byte getFrozesetting() {
        return frozesetting;
    }

    public byte getFrozereal() {
        return frozereal;
    }

    public byte getTurbo() {
        return turbo;
    }

    public byte getHeat() {
        return heat;
    }

    public byte getBattery() {
        return battery;
    }

    public byte getUnit() {
        return unit;
    }

    public byte getStatus() {
        return status;
    }

    public byte getErrorcode() {
        return errorcode;
    }

    public byte getVhigh() {
        return vhigh;
    }

    public byte getVlow() {
        return vlow;
    }

    public byte getGc() {
        return gc;
    }

    public byte getTempheat() {
        return tempheat;
    }

    public byte getTimer() {
        return timer;
    }

    public byte getCode1() {
        return code1;
    }

    public byte getCode2() {
        return code2;
    }

    public byte getCrcH() {
        return crcH;
    }

    public byte getCrcL() {
        return crcL;
    }

    public byte getEnd() {
        return end;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
        this.start = data[0];
        this.power = data[1];
        this.tempcool = data[2];
        this.tempReal = data[3];
        this.frozesetting = data[4];
        this.frozereal = data[5];
        this.turbo = data[6];
        this.heat = data[7];
        this.battery = data[8];
        this.unit = data[9];
        this.status = data[10];
        this.errorcode = data[11];
        this.vhigh = data[12];
        this.vlow = data[13];
        this.gc = data[14];
        this.tempheat = data[15];
        this.timer = data[16];
        this.code1 = data[17];
        this.code2 = data[18];
    }
}
