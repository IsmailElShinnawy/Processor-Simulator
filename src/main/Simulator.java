package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import exceptions.MemoryException;
import exceptions.RegisterFileException;
import stages.InstructionDecodeStage;
import stages.InstructionExecuteStage;
import stages.InstructionFetchStage;
import stages.MemoryAccessStage;
import stages.WriteBackStage;
import storage.Memory;
import storage.PipelineRegisterFile;
import storage.RegisterFile;

public class Simulator {

    private Memory memMemory;
    private RegisterFile rfileRegFile;

    private InstructionFetchStage ifStage;
    private InstructionDecodeStage idStage;
    private InstructionExecuteStage iexStage;
    private MemoryAccessStage maStage;
    private WriteBackStage wbStage;

    private PipelineRegisterFile IFtoIDPipelineRegisterFile;
    private PipelineRegisterFile IDtoIEXPipelineRegisterFile;
    private PipelineRegisterFile IEXtoMAPipelineRegisterFile;
    private PipelineRegisterFile MAtoWBPipelineRegisterFile;

    private int iCurrentClkCycle;

    // after "HLT" instruction is fetched then you only need to run simulation for 4
    // more cycles
    private int remainingClkCycles = 4;

    /**
     * Constructor of the simulator, initializes memory, register file, instruction
     * cycle stages and pipeline register files and it sets up the linkage of stages
     * and the pipeline register files
     * 
     * @param piInstructionMemorySize size of the instruction segment in memory
     * @param piDataMemorySize        size of the data segmetn in memory
     */
    public Simulator(int piInstructionMemorySize, int piDataMemorySize) {
        // Initialising Memory and register file
        memMemory = new Memory(piInstructionMemorySize + piDataMemorySize);
        rfileRegFile = new RegisterFile();

        // Initialising Stages
        ifStage = new InstructionFetchStage(this);
        idStage = new InstructionDecodeStage(this);
        iexStage = new InstructionExecuteStage(this);
        maStage = new MemoryAccessStage(this);
        wbStage = new WriteBackStage(this);

        // Initalising Pipeline Register files
        IFtoIDPipelineRegisterFile = new PipelineRegisterFile();
        IDtoIEXPipelineRegisterFile = new PipelineRegisterFile();
        IEXtoMAPipelineRegisterFile = new PipelineRegisterFile();
        MAtoWBPipelineRegisterFile = new PipelineRegisterFile();

        // Connections
        ifStage.setPrevPipelineRegisterFile(null);
        ifStage.setNextPipelineRegisterFile(IFtoIDPipelineRegisterFile);

        idStage.setPrevPipelineRegisterFile(IFtoIDPipelineRegisterFile);
        idStage.setNextPipelineRegisterFile(IDtoIEXPipelineRegisterFile);

        iexStage.setPrevPipelineRegisterFile(IDtoIEXPipelineRegisterFile);
        iexStage.setNextPipelineRegisterFile(IEXtoMAPipelineRegisterFile);

        maStage.setPrevPipelineRegisterFile(IEXtoMAPipelineRegisterFile);
        maStage.setNextPipelineRegisterFile(MAtoWBPipelineRegisterFile);

        wbStage.setPrevPipelineRegisterFile(MAtoWBPipelineRegisterFile);
        wbStage.setNextPipelineRegisterFile(null);
    }

    /**
     * Main method of the simulator which starts and runs the simulation by calling
     * the correct stages to execution at the correct clock cycles
     * 
     * @throws MemoryException       when invalid memory operation occurs
     * @throws RegisterFileException when invalid register file operation occurs
     * @throws FileNotFoundException on I/O failure
     */
    public void start() throws MemoryException, RegisterFileException, FileNotFoundException {
        System.out.println("--------------------------START OF SIMULATION--------------------------");
        iCurrentClkCycle = 1;
        boolean fd = false, fe = false, end = false;
        while (true) {
            // checks if program ended
            if (end && remainingClkCycles-- == 0) {
                break;
            }
            System.out.printf("CURRENT CLK CYCLE %d\n", iCurrentClkCycle);
            if (iCurrentClkCycle > 6 && iCurrentClkCycle % 2 != 0)
                wbStage.execute();
            if (iCurrentClkCycle > 5 && iCurrentClkCycle % 2 == 0)
                maStage.execute();
            if (iCurrentClkCycle > 3) {
                if (!fe)
                    iexStage.execute();
                else
                    iexStage.sendToNextStage();
                fe = !fe;
            }
            if (iCurrentClkCycle > 1) {
                if (!fd)
                    idStage.execute();
                else {
                    idStage.incrementPC();
                }
                fd = !fd;
            }
            if (iCurrentClkCycle % 2 != 0) {
                ifStage.execute();
                // checks if instruction fetched is "HLT"
                if (IFtoIDPipelineRegisterFile.get("ir").getValue() == 0xFFFFFFFF) {
                    end = true;
                    idStage.setNOP(1);
                }
            }
            ++iCurrentClkCycle;
            System.out.println(getRegisterFile());
        }
        // System.out.println(memMemory);
        PrintWriter pw = new PrintWriter(new File("src/memory"));
        pw.append(memMemory.toString());
        pw.flush();
        pw.close();
    }

    /**
     * used to signal simulator to flush/remove any instructions int the IF and ID
     * stages
     */
    public void flush() {
        ifStage.setNOP(1);
        idStage.setNOP(1);
    }

    /*
     * GETTERS
     */
    public RegisterFile getRegisterFile() {
        return rfileRegFile;
    }

    public Memory getMemory() {
        return memMemory;
    }

}
