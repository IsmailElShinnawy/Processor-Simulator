package stages;

import main.Simulator;

public class InstructionFetchStage extends Stage {

    public InstructionFetchStage(Simulator pSimSimulator) {
        super(pSimSimulator);
    }

    @Override
    public void execute() {
        int instruction = getSimulator().getMemory().getWord(getSimulator().getRegisterFile().getPCValue());
        getNextPipelineRegisterFile().put("pc", getSimulator().getRegisterFile().getPCValue());
        getNextPipelineRegisterFile().put("ir", instruction);
    }

}
