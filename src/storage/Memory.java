
package storage;

import exceptions.MemoryException;

public class Memory {
    private int aiMem[];
    private int iSize;

    public Memory(int piSize) {
        aiMem = new int[piSize];
        iSize = piSize;
    }

    public void setWord(int piAddress, int piWord) throws MemoryException {
        if (piAddress < iSize && piAddress >= 0) {
            aiMem[piAddress] = piWord;
        } else {
            throw new MemoryException("The memory location you're trying to access does not exist");
        }
    }

    public int getWord(int piAddress) throws MemoryException {
        if (piAddress < iSize && piAddress >= 0)
            return aiMem[piAddress];
        throw new MemoryException("The memory location you're trying to access does not exist");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("++++++++++++++++ MEMORY ++++++++++++++++\n");
        for (int i = 0; i < aiMem.length; ++i) {
            sb.append(String.format("%-15d\t\t%d\n", i, aiMem[i]));
        }
        return sb.toString();
    }
}