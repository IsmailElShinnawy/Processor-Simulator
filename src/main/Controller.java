package main;

import java.io.IOException;

import exceptions.AssemblerException;
import exceptions.MemoryReadException;
import exceptions.MemoryWriteException;
import exceptions.RegisterNotFoundException;
import exceptions.ZeroRegisterException;
import exceptions.pcGetException;
import exceptions.pcSetException;

public class Controller {

    private Simulator simulator;
    private Assembler assembler;

    private static final int INSTRUCTION_MEMORY_SIZE = 1024, DATA_MEMORY_SIZE = 1024;

    public Controller() throws IOException, MemoryReadException, RegisterNotFoundException, pcGetException,
            ZeroRegisterException, pcSetException, MemoryWriteException, AssemblerException {
        simulator = new Simulator(INSTRUCTION_MEMORY_SIZE, DATA_MEMORY_SIZE);
        assembler = new Assembler("src/program", simulator);

        assembler.assemble();

        simulator.start();
    }

    public static void main(String[] args) throws IOException, MemoryReadException, RegisterNotFoundException,
            pcGetException, ZeroRegisterException, pcSetException, MemoryWriteException, AssemblerException {
        new Controller();
    }

}
