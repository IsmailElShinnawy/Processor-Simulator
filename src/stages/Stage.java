package stages;

import exceptions.RegisterNotFoundException;
import exceptions.ZeroRegisterException;
import exceptions.pcSetException;
import main.Simulator;
import storage.PipelineRegisterFile;

public abstract class Stage {
    private Simulator simSimulator;
    private PipelineRegisterFile prevPipelineRegisterFile;
    private PipelineRegisterFile nextPipelineRegisterFile;

    public Stage(Simulator pSimSimulator) {
        simSimulator = pSimSimulator;
    }

    public abstract void execute() throws RegisterNotFoundException, ZeroRegisterException, pcSetException;

    public Simulator getSimulator() {
        return simSimulator;
    }

    public void setPrevPipelineRegisterFile(PipelineRegisterFile pPrevPipelineRegisterFile) {
        prevPipelineRegisterFile = pPrevPipelineRegisterFile;
    }

    public void setNextPipelineRegisterFile(PipelineRegisterFile pNextPipelineRegisterFile) {
        nextPipelineRegisterFile = pNextPipelineRegisterFile;
    }

    public PipelineRegisterFile getPrevPipelineRegisterFile() {
        return prevPipelineRegisterFile;
    }

    public PipelineRegisterFile getNextPipelineRegisterFile() {
        return nextPipelineRegisterFile;
    }

    public PipelineRegisterFile getNextPipelineRegisterFile() {
        return nextPipelineRegisterFile;
    }

    public PipelineRegisterFile getPrevPipelineRegisterFile() {
        return prevPipelineRegisterFile;
    }
}
