package stages;

import exceptions.MemoryReadException;
import exceptions.RegisterNotFoundException;
import main.Simulator;

public class InstructionFetchStage extends Stage {

    private int nop;

    public InstructionFetchStage(Simulator pSimSimulator) {
        super(pSimSimulator);
    }

    @Override
    public void execute() throws MemoryReadException {
        System.out.println("FETCHING STAGE");
        if (nop == 1) {
            System.out.println("NO OPERATION");
            getNextPipelineRegisterFile().put("NOP", 1);
            nop = 0;
            return;
        }
        System.out.printf("FETCHING INSTRUCTION FROM @ %d\n", getSimulator().getRegisterFile().getPCValue());

        int instruction = getSimulator().getMemory().getWord(getSimulator().getRegisterFile().getPCValue());

        System.out.printf("INSTRUCTION 0b%s FETCHED\n", convertToBin32(instruction));
        System.out.println("----------------------------------------------------------");

        getNextPipelineRegisterFile().put("pc", getSimulator().getRegisterFile().getPCValue());
        getNextPipelineRegisterFile().put("ir", instruction);
        getNextPipelineRegisterFile().put("NOP", 0);

    }

    public void setNOP(int nop) {
        this.nop = nop;
    }

}
