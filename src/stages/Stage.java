package stages;

import exceptions.MemoryException;
import exceptions.RegisterFileException;
import main.Simulator;
import storage.PipelineRegisterFile;

public abstract class Stage {
    private Simulator simSimulator;
    private PipelineRegisterFile prevPipelineRegisterFile;
    private PipelineRegisterFile nextPipelineRegisterFile;

    public Stage(Simulator pSimSimulator) {
        simSimulator = pSimSimulator;
    }

    public abstract void execute() throws RegisterFileException, MemoryException;

    public Simulator getSimulator() {
        return simSimulator;
    }

    /*
     * SETTERS AND GETTERS
     */
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

    /*
     * HELPER METHODS
     */
    public static String convertToBin32(int x) {
        String bin = "";
        for (int i = 0; i < 32; ++i) {
            bin = (((x >> i) & 1) == 1 ? '1' : '0') + bin;
        }
        return bin;
    }
}
