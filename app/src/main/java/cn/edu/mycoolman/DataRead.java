package cn.edu.mycoolman;

public class DataRead {
    private byte start;
    private byte power;
    private byte tempSetting;
    private byte tempReal;
    private byte mode;
    private byte wind;
    private byte turbo;
    private byte sleep;
    private byte unit;
    private byte countdown;
    private byte logo;
    private byte atmosphere;
    private byte red;
    private byte green;
    private byte blue;
    private byte brightness;
    private byte errcode;
    private byte version;
    private byte reserver1;
    private byte reserver2;
    private byte crcH;
    private byte crcL;
    private byte end;
    private byte[] data;

    public void setData(byte[] data) {
        this.data = data;
        this.power = data[1];
        this.tempSetting = data[2];
        this.tempReal = data[3];
        this.mode = data[4];
        this.wind = data[5];
        this.turbo = data[6];
        this.sleep = data[7];
        this.unit = data[8];
        this.countdown = data[9];
        this.logo = data[10];
        this.atmosphere = data[11];
        this.red = data[12];
        this.green = data[13];
        this.blue = data[14];
        this.brightness = data[15];
        this.errcode = data[16];
    }

    public byte getPower() {
        return power;
    }

    public byte getTempSetting() {
        return tempSetting;
    }

    public byte getTempReal() {
        return tempReal;
    }

    public byte getMode() {
        return mode;
    }

    public byte getWind() {
        return wind;
    }

    public byte getTurbo() {
        return turbo;
    }

    public byte getSleep() {
        return sleep;
    }

    public byte getUnit() {
        return unit;
    }

    public byte getCountdown() {
        return countdown;
    }

    public byte getLogo() {
        return logo;
    }

    public byte getAtmosphere() {
        return atmosphere;
    }

    public byte getRed() {
        return red;
    }

    public byte getGreen() {
        return green;
    }

    public byte getBlue() {
        return blue;
    }

    public byte getBrightness() {
        return brightness;
    }

    public byte getErrcode() {
        return errcode;
    }

    public byte getVersion() {
        return version;
    }

    public byte getReserver1() {
        return reserver1;
    }

    public byte getReserver2() {
        return reserver2;
    }

    public byte getCrcH() {
        return crcH;
    }

    public byte getCrcL() {
        return crcL;
    }
}
