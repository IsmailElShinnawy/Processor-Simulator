package main;

import java.util.Hashtable;

import exceptions.MemoryReadException;
import exceptions.MemoryWriteException;
import exceptions.RegisterNotFoundException;
import exceptions.ZeroRegisterException;
import exceptions.pcGetException;
import exceptions.pcSetException;
import stages.InstructionExecuteStage;
import stages.InstructionFetchStage;
import stages.MemoryAccessStage;
import stages.WriteBackStage;
import stages.InstructionDecodeStage;
import storage.Memory;
import storage.PipelineRegisterFile;
import storage.RegisterFile;

public class Simulator {

    private Memory memMemory;
    private RegisterFile rfileRegFile;
    private int iTotalClkCycles;
    private Hashtable<Integer, String> htblOPCodeInstruction;

    private InstructionFetchStage ifStage;
    private InstructionDecodeStage idStage;
    private InstructionExecuteStage iexStage;
    private MemoryAccessStage maStage;
    private WriteBackStage wbStage;

    private PipelineRegisterFile IFtoIDPipelineRegisterFile;
    private PipelineRegisterFile IDtoIEXPipelineRegisterFile;
    private PipelineRegisterFile IEXtoMAPipelineRegisterFile;
    private PipelineRegisterFile MAtoWBPipelineRegisterFile;

    public Simulator(int piInstructionMemorySize, int piDataMemorySize) {
        // Initialising Memory
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

    public void start() throws MemoryReadException, RegisterNotFoundException, pcGetException, ZeroRegisterException,
            pcSetException, MemoryWriteException {
        System.out.println("--------------------------Welcome to MacNeumann--------------------------");
        int currentClkCycle = 1;
        boolean fd = false, fe = false;
        while (currentClkCycle <= iTotalClkCycles) {
            System.out.printf("CURRENT CLK CYCLE %d\n", currentClkCycle);
            if (currentClkCycle > 6 && currentClkCycle % 2 != 0)
                wbStage.execute();
            if (currentClkCycle > 5 && currentClkCycle % 2 == 0)
                maStage.execute();
            if (currentClkCycle > 3) {
                if (!fe)
                    iexStage.execute();
                else
                    iexStage.sendToNextStage();
                fe = !fe;
            }
            if (currentClkCycle > 1) {
                if (!fd)
                    idStage.execute();
                else {
                    idStage.incrementPC();
                }
                fd = !fd;
            }
            if (currentClkCycle % 2 != 0)
                ifStage.execute();
            ++currentClkCycle;
            System.out.println(this.getRegisterFile());
        }
        // System.out.println(memMemory);
    }

    public void flush() {
        ifStage.setNOP(1);
        idStage.setNOP(1);
    }

    public RegisterFile getRegisterFile() {
        return rfileRegFile;
    }

    public Memory getMemory() {
        return memMemory;
    }

    public void setTotalClkCycles(int piNumberOfInstruction) {
        iTotalClkCycles = 7 + ((piNumberOfInstruction - 1) * 2);
    }

}
