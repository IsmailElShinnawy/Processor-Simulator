package stages;

import exceptions.StorageException;
import main.Simulator;

public class WriteBackStage extends Stage {

    public WriteBackStage(Simulator pSimSimulator) {
        super(pSimSimulator);
    }

    @Override
    public void execute() throws StorageException {
        if(this.getPrevPipelineRegisterFile().get("wb").getValue()==1){
            int writeBackRegisterAddress = this.getPrevPipelineRegisterFile().get("wbReg").getValue();
            int dataToBeWritten=0;
            if(this.getPrevPipelineRegisterFile().get("MemToReg").getValue()==1){
                dataToBeWritten= this.getPrevPipelineRegisterFile().get("MBR").getValue();
            }
            else{
                dataToBeWritten= this.getPrevPipelineRegisterFile().get("ac").getValue();
            }
            this.getSimulator().getRegisterFile().setRegisterValue(writeBackRegisterAddress, dataToBeWritten);
        }
        else{
            return;
        }

    }

}