package storage;

public class Register {
    private int iValue;
    private String strName;

    public Register(String pstrName) {
        strName = pstrName;
        iValue = 0;
    }

    public int getValue() {
        return iValue;
    }

    public void setValue(int piValue) {
        iValue = piValue;
    }

    public String getName() {
        return strName;
    }

    public void setName(String pStrName) {
        strName = pStrName;
    }

    public String toString() {
        return strName + " = " + iValue;
    }
}
