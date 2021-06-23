package stages;

import exceptions.RegisterFileException;
import main.Simulator;

public class WriteBackStage extends Stage {

    public WriteBackStage(Simulator pSimSimulator) {
        super(pSimSimulator);
    }

    @Override
    public void execute() throws RegisterFileException {
        System.out.println("WRITE BACK STAGE");

        // if a NOP is propagated from a previous stage, then do not execute anything
        if (getPrevPipelineRegisterFile().get("NOP").getValue() == 1) {
            System.out.println("NO OPERATION");
            return;
        }
        System.out.printf("WRITE BACK STAGE FOR INSTRUCTION 0b%s\n",
                convertToBin32(this.getPrevPipelineRegisterFile().get("ir").getValue()));

        if (this.getPrevPipelineRegisterFile().get("wb").getValue() == 1) {
            int writeBackRegisterAddress = this.getPrevPipelineRegisterFile().get("wbReg").getValue();
            int dataToBeWritten = 0;
            if (this.getPrevPipelineRegisterFile().get("MemToReg").getValue() == 1) {
                dataToBeWritten = this.getPrevPipelineRegisterFile().get("MBR").getValue();
            } else {
                dataToBeWritten = this.getPrevPipelineRegisterFile().get("ac").getValue();
            }
            this.getSimulator().getRegisterFile().setRegisterValue(writeBackRegisterAddress, dataToBeWritten);
            System.out.printf("DESTINATION_REGISTER = %d  DATA = %d\n", writeBackRegisterAddress, dataToBeWritten);
            System.out.printf("REGISTER R%d UPDATED TO %d\n", writeBackRegisterAddress, dataToBeWritten);
        } else {
            System.out.println("NO WRITE BACK NEEDED");
        }
        System.out.println("----------------------------------------------------------");

    }

}