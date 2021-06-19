package storage;

import java.util.Hashtable;

import exceptions.StorageException;

public class PipelineRegisterFile {
    private Hashtable<String, Register> htblNameRegister;

    public PipelineRegisterFile(){
        htblNameRegister = new Hashtable<String, Register>();
    }

    public void put(String pStrRegisterName, int iValue) {
        if(htblNameRegister.containsKey(pStrRegisterName)){
            htblNameRegister.get(pStrRegisterName).setValue(iValue);
        }
        else{
            Register reg = new Register(pStrRegisterName);
            reg.setValue(iValue);
            htblNameRegister.put(pStrRegisterName, reg);
        }
    }

    public Register get(String pStrRegisterName) throws StorageException {
        if(htblNameRegister.containsKey(pStrRegisterName)){
            return htblNameRegister.get(pStrRegisterName);
        }
        else{
            throw new StorageException("The register you're trying to access does not exit");
        }
    }


}
