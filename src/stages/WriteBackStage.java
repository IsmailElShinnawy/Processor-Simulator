package stages;

import exceptions.RegisterNotFoundException;
import exceptions.ZeroRegisterException;
import exceptions.pcSetException;
import main.Simulator;

public class WriteBackStage extends Stage {

    public WriteBackStage(Simulator pSimSimulator) {
        super(pSimSimulator);
    }

    @Override
    public void execute() throws RegisterNotFoundException, ZeroRegisterException, pcSetException {
        if (this.getPrevPipelineRegisterFile().get("wb").getValue() == 1) {
            int writeBackRegisterAddress = this.getPrevPipelineRegisterFile().get("wbReg").getValue();
            int dataToBeWritten = 0;
            if (this.getPrevPipelineRegisterFile().get("MemToReg").getValue() == 1) {
                dataToBeWritten = this.getPrevPipelineRegisterFile().get("MBR").getValue();
            } else {
                dataToBeWritten = this.getPrevPipelineRegisterFile().get("ac").getValue();
            }
            this.getSimulator().getRegisterFile().setRegisterValue(writeBackRegisterAddress, dataToBeWritten);
        } else {
            return;
        }

    }

}