package stages;

import exceptions.MemoryException;
import exceptions.RegisterFileException;
import main.Simulator;

public class MemoryAccessStage extends Stage {

    public MemoryAccessStage(Simulator pSimSimulator) {
        super(pSimSimulator);
    }

    @Override
    public void execute() throws RegisterFileException, MemoryException {
        System.out.println("MEMORY ACCESS STAGE");
        // if a NOP is propagated from a previous stage, then do not execute anything
        if (getPrevPipelineRegisterFile().get("NOP").getValue() == 1) {
            System.out.println("NO OPERATION");
            // propagate forward the NOP signal
            getNextPipelineRegisterFile().put("NOP", 1);
            return;
        }
        System.out.printf("MEMORY ACCESS FOR INSTRUCTION 0b%s\n",
                convertToBin32(this.getPrevPipelineRegisterFile().get("ir").getValue()));

        int ac = this.getPrevPipelineRegisterFile().get("ac").getValue();
        int MAR = this.getPrevPipelineRegisterFile().get("MAR").getValue();
        int memRead = this.getPrevPipelineRegisterFile().get("memoryRead").getValue();
        int memWrite = this.getPrevPipelineRegisterFile().get("memoryWrite").getValue();
        int MemToReg = 0;
        int MBR = 0;

        if (memWrite == 1) {
            System.out.println("MEMORY WRITE");
            System.out.printf("SET MEMORY WORD @ %d to %d\n", MAR, ac);
            getSimulator().getMemory().setWord(MAR, ac);
        } else if (memRead == 1) {
            System.out.println("MEMORY READ");
            MBR = getSimulator().getMemory().getWord(MAR);
            System.out.printf("READ MEMORY WORD @ %d = %d\n", MAR, MBR);
            MemToReg = 1;
        } else {
            System.out.println("NO MEMORY ACCESS NEEDED FOR INSTRUCTION");
        }

        this.getNextPipelineRegisterFile().put("ac", ac);
        this.getNextPipelineRegisterFile().put("MBR", MBR);
        this.getNextPipelineRegisterFile().put("MemToReg", MemToReg);
        this.getNextPipelineRegisterFile().put("wbReg", this.getPrevPipelineRegisterFile().get("wbReg").getValue());
        this.getNextPipelineRegisterFile().put("wb", this.getPrevPipelineRegisterFile().get("wb").getValue());

        // propagate forward the IR value
        this.getNextPipelineRegisterFile().put("ir", this.getPrevPipelineRegisterFile().get("ir").getValue());

        // propagate forward the NOP signal
        getNextPipelineRegisterFile().put("NOP", 0);

        System.out.println("OUTPUT TO NEXT STAGE:");
        System.out.println("AC = " + ac);
        System.out.println("MBR = " + MBR);
        System.out.println("MemToReg = " + MemToReg);
        System.out.println("wbReg = " + this.getPrevPipelineRegisterFile().get("wbReg").getValue());
        System.out.println("wb = " + this.getPrevPipelineRegisterFile().get("wb").getValue());

        System.out.println("----------------------------------------------------------");

    }

}
