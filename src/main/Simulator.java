package main;

import java.util.Hashtable;

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
    private int iClkCycles;
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

        maStage.setNextPipelineRegisterFile(IEXtoMAPipelineRegisterFile);
        maStage.setNextPipelineRegisterFile(MAtoWBPipelineRegisterFile);

        wbStage.setPrevPipelineRegisterFile(MAtoWBPipelineRegisterFile);
        wbStage.setNextPipelineRegisterFile(null);
    }

    public void start() {
        System.out.println("Welcome to MacNeumann");
        iClkCycles = 1;
        boolean fd = false;
        boolean fe = false;
        while (true) {
            if (iClkCycles%2!=0){
                ifStage.execute();
                if(fd == false){idStage.execute();}
                if(fe == false){iexStage.execute();}
                wbStage.execute();
                fd = false;
                fe = false;
            }
            else{
                idStage.execute();
                iexStage.execute();
                maStage.execute();
                fd = true;
                fe = true;
            }
            System.out.println("Cycle"+iClkCycles);
            System.out.println();
            iClkCycles++;
        }
    }
    public RegisterFile getRegisterFile() {
        return rfileRegFile;
    }

    public Memory getMemory() {
        return memMemory;
    }

}
