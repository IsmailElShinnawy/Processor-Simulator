package stages;

import exceptions.MemoryException;
import main.Simulator;

public class InstructionFetchStage extends Stage {

    private int nop;

    public InstructionFetchStage(Simulator pSimSimulator) {
        super(pSimSimulator);
    }

    @Override
    public void execute() throws MemoryException {
        System.out.println("FETCHING STAGE");
        // if NOP signal is up, then do not execute anything
        if (nop == 1) {
            System.out.println("NO OPERATION");
            // clears the current NOP signal
            nop = 0;
            // propagate forward the NOP signal
            getNextPipelineRegisterFile().put("NOP", 1);
            return;
        }
        System.out.printf("FETCHING INSTRUCTION FROM @ %d\n", getSimulator().getRegisterFile().getPCValue());

        int instruction = getSimulator().getMemory().getWord(getSimulator().getRegisterFile().getPCValue());

        System.out.printf("INSTRUCTION 0b%s FETCHED\n", convertToBin32(instruction));
        System.out.println("----------------------------------------------------------");

        getNextPipelineRegisterFile().put("pc", getSimulator().getRegisterFile().getPCValue());
        // propagate forward the ir value
        getNextPipelineRegisterFile().put("ir", instruction);
        // propagate forward the NOP signal
        getNextPipelineRegisterFile().put("NOP", 0);

    }

    public void setNOP(int nop) {
        this.nop = nop;
    }

}
