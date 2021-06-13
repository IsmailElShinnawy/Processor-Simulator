package stages;

import main.Simulator;
import storage.PipelineRegisterFile;

public abstract class Stage {
    private Simulator simSimulator;
    private PipelineRegisterFile prevPipelineRegisterFile;
    private PipelineRegisterFile nextPipelineRegisterFile;

    public Stage(Simulator pSimSimulator) {

    }

    public abstract void execute();

    public Simulator getSimulator() {
        return null;
    }

    public void setPrevPipelineRegisterFile(PipelineRegisterFile pPrevPipelineRegisterFile) {

    }

    public void setNextPipelineRegisterFile(PipelineRegisterFile pNextPipelineRegisterFile) {

    }

    public PipelineRegisterFile getPrevPipelineRegisterFile() {
        return prevPipelineRegisterFile;
    }

    public PipelineRegisterFile getNextPipelineRegisterFile() {
        return nextPipelineRegisterFile;
    }
}
