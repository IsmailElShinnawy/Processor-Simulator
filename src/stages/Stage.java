package stages;

import main.Simulator;
import storage.PipelineRegisterFile;

public abstract class Stage {
    private Simulator simSimulator;
    private PipelineRegisterFile prevPipelineRegisterFile;
    private PipelineRegisterFile nextPipelineRegisterFile;

    public Stage(Simulator pSimSimulator) {
         simSimulator = pSimSimulator;
    }

    public abstract void execute();

    public Simulator getSimulator() {
        return simSimulator;
    }

    public void setPrevPipelineRegisterFile(PipelineRegisterFile pPrevPipelineRegisterFile) {
        prevPipelineRegisterFile = pPrevPipelineRegisterFile;
    }

    public void setNextPipelineRegisterFile(PipelineRegisterFile pNextPipelineRegisterFile) {
        nextPipelineRegisterFile = pNextPipelineRegisterFile;
    }
}
