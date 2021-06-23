package storage;

import exceptions.RegisterFileException;

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

    public int getRegisterValue(int piRegisterAddress) throws RegisterFileException {
        if (piRegisterAddress < 0 || piRegisterAddress >= 32) {
            throw new RegisterFileException(String.format("Register @ %d is not a GPR", piRegisterAddress));
        }
        return aRegRegisters[piRegisterAddress].getValue();
    }

    public void setRegisterValue(int piRegisterAddress, int piValue) throws RegisterFileException {
        if (piRegisterAddress < 0 || piRegisterAddress >= 32) {
            throw new RegisterFileException(String.format("Register @ %d is not a GPR", piRegisterAddress));
        }
        if (piRegisterAddress == 0)
            return;
        aRegRegisters[piRegisterAddress].setValue(piValue);
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
