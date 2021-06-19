package storage;

import exceptions.MemoryReadException;
import exceptions.MemoryWriteException;

public class Memory {
    private int aiMem[];
    private int iSize;

    public Memory(int piSize) {
        aiMem = new int[piSize];
        iSize = piSize;
    }

    public void setWord(int piAddress, int piWord) throws MemoryWriteException {
        if (piAddress < iSize && piAddress >= 0) {
            aiMem[piAddress] = piWord;
        } else {
            throw new MemoryWriteException("The memory location you're trying to access does not exist");
        }
    }

    public int getWord(int piAddress) throws MemoryReadException {
        if (piAddress < iSize && piAddress >= 0) {
            return aiMem[piAddress];
        } else {
            throw new MemoryReadException("The memory location you're trying to access does not exist");
        }
    }
}
