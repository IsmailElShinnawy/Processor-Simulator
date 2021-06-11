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
    }

    public void start() {
    }

    public RegisterFile getRegisterFile() {
        return null;
    }

    public Memory getMemory() {
        return null;
    }

}
