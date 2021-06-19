package storage;

import exceptions.StorageException;

public class RegisterFile {
    private Register aRegRegisters[];
    private final int PC = 32;

    public RegisterFile() {
        aRegRegisters = new Register[33];
        for(int i=0;i<33;i++){
            String regName="";
            if( i== 32){
                regName="PC";
            }
            else{
                regName="R"+i;
            }
            aRegRegisters[i]=new Register(regName);
        }
    }

    public int getRegisterValue(int piRegisterAddress) throws StorageException {
        if(piRegisterAddress>=0 && piRegisterAddress<32){
            return aRegRegisters[piRegisterAddress].getValue();
        }
        else if(piRegisterAddress==32){
            throw new StorageException("Incorrect method to get PC value, please us .getPC() method");
        }
        else{
            throw new StorageException("You're trying to access a register that does not exist");
        }
    }

    public void setRegisterValue(int piRegisterAddress, int piValue) throws StorageException { //To set GPRS and Zero Reg
        if(piRegisterAddress>=1 && piRegisterAddress<32){
            aRegRegisters[piRegisterAddress].setValue(piValue);
        }
        else if(piRegisterAddress==32){
            throw new StorageException("Incorrect method to get PC value, please us .setPC() method");
        }
        else if(piRegisterAddress==0){
            throw new StorageException("Can not set the value of zero register");
        }
        else{
            throw new StorageException("You're trying to set the value of a register that does not exist");
        }
    }

    public int getPCValue() {
        return aRegRegisters[PC].getValue();
    }

    public void setPCValue(int piValue)  {
        aRegRegisters[PC].setValue(piValue);
    }
}
