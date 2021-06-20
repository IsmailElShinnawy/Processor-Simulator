package stages;

import exceptions.MemoryReadException;
import exceptions.RegisterNotFoundException;
import main.Simulator;

public class InstructionFetchStage extends Stage {

    public InstructionFetchStage(Simulator pSimSimulator) {
        super(pSimSimulator);
    }

    @Override
    public void execute() throws MemoryReadException {
        System.out.printf("FETCHING INSTRUCTION FROM @ %d\n", getSimulator().getRegisterFile().getPCValue());

        int instruction = getSimulator().getMemory().getWord(getSimulator().getRegisterFile().getPCValue());

        System.out.printf("INSTRUCTION 0b%s FETCHED\n", convertToBin32(instruction));
        System.out.println("--------------------------------------------------");

        getNextPipelineRegisterFile().put("pc", getSimulator().getRegisterFile().getPCValue());
        getNextPipelineRegisterFile().put("ir", instruction);
    }

}
