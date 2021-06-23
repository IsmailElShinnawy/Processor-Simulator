package storage;

import java.util.Hashtable;

import exceptions.RegisterFileException;

public class PipelineRegisterFile {
    private Hashtable<String, Register> htblNameRegister;

    public PipelineRegisterFile() {
        htblNameRegister = new Hashtable<String, Register>();
    }

    public void put(String pStrRegisterName, int iValue) {
        Register reg = new Register(pStrRegisterName);
        reg.setValue(iValue);
        htblNameRegister.put(pStrRegisterName, reg);
    }

    public Register get(String pStrRegisterName) throws RegisterFileException {
        if (!htblNameRegister.containsKey(pStrRegisterName))
            throw new RegisterFileException(
                    String.format("Register %s does not exist in the pipeline register file", pStrRegisterName));
        return htblNameRegister.get(pStrRegisterName);
    }

}
