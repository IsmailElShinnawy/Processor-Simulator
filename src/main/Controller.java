package main;

import java.io.IOException;

import exceptions.AssemblerException;
import exceptions.MemoryException;
import exceptions.RegisterFileException;

public class Controller {

    private Simulator simulator;
    private Assembler assembler;

    private static final int INSTRUCTION_MEMORY_SIZE = 1024, DATA_MEMORY_SIZE = 1024;

    public Controller() throws IOException, AssemblerException, RegisterFileException, MemoryException {
        simulator = new Simulator(INSTRUCTION_MEMORY_SIZE, DATA_MEMORY_SIZE);
        assembler = new Assembler("src/program", simulator);

        assembler.assemble();

        simulator.start();
    }

    /**
     * MAIN PROGRAM TO BE RUN
     * 
     * @param args
     * @throws IOException
     * @throws MemoryException
     * @throws RegisterFileException
     * @throws AssemblerException
     */
    public static void main(String[] args)
            throws IOException, MemoryException, RegisterFileException, AssemblerException {
        new Controller();
    }

}
