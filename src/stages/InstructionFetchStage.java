package stages;

import exceptions.MemoryReadException;
import main.Simulator;

public class InstructionFetchStage extends Stage {

    public InstructionFetchStage(Simulator pSimSimulator) {
        super(pSimSimulator);
    }

    @Override
    public void execute() throws MemoryReadException {
        int instruction = getSimulator().getMemory().getWord(getSimulator().getRegisterFile().getPCValue());
        getNextPipelineRegisterFile().put("pc", getSimulator().getRegisterFile().getPCValue());
        getNextPipelineRegisterFile().put("ir", instruction);
    }

}
