package storage;

import exceptions.RegisterNotFoundException;
import exceptions.ZeroRegisterException;
import exceptions.pcGetException;
import exceptions.pcSetException;

public class RegisterFile {
    private Register aRegRegisters[];
    private final int PC = 32;

    public RegisterFile() {
        aRegRegisters = new Register[33];
        for (int i = 0; i < 32; i++) {
            String regName = "R" + i;
            aRegRegisters[i] = new Register(regName);
        }
        aRegRegisters[PC] = new Register("PC");
    }

    public int getRegisterValue(int piRegisterAddress) throws RegisterNotFoundException, pcGetException {
        if (piRegisterAddress >= 0 && piRegisterAddress < 32) {
            return aRegRegisters[piRegisterAddress].getValue();
        } else if (piRegisterAddress == 32) {
            throw new pcGetException("Incorrect method to get PC value, please us .getPC() method");
        } else {
            throw new RegisterNotFoundException("You're trying to access a register that does not exist");
        }
    }

    public void setRegisterValue(int piRegisterAddress, int piValue)
            throws RegisterNotFoundException, ZeroRegisterException, pcSetException { // To set GPRS and Zero Reg
        if (piRegisterAddress >= 1 && piRegisterAddress < 32) {
            aRegRegisters[piRegisterAddress].setValue(piValue);
        } else if (piRegisterAddress == 32) {
            throw new pcSetException("Incorrect method to get PC value, please us .setPC() method");
        } else if (piRegisterAddress == 0) {
            throw new ZeroRegisterException("Can not set the value of zero register");
        } else {
            throw new RegisterNotFoundException("You're trying to set the value of a register that does not exist");
        }
    }

    public int getPCValue() {
        return aRegRegisters[PC].getValue();
    }

    public void setPCValue(int piValue) {
        aRegRegisters[PC].setValue(piValue);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(
                "++++++++++++++++++++++++++++++++++REGISTER FILE++++++++++++++++++++++++++++++++++\n");
        for (Register r : aRegRegisters) {
            sb.append(r.toString()).append("\n");
        }
        return sb.toString();
    }
}
