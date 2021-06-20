package stages;

import main.Simulator;
public class MemoryAccessStage extends Stage {

    public MemoryAccessStage(Simulator pSimSimulator) {
        super(pSimSimulator);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void execute() {
        int ac = this.getPrevPipelineRegisterFile().get("ac").getValue();
        int MAR = this.getPrevPipelineRegisterFile().get("MAR").getValue();
        int memRead = this.getPrevPipelineRegisterFile().get("memoryRead").getValue();
		int memWrite = this.getPrevPipelineRegisterFile().get("memoryWrite").getValue(); 
        int wb = 0;
        int MemToReg = 0;
        if(memWrite==1){
            getSimulator().getMemory().setWord(MAR, ac);
            wb = 1;
        }
         else{
             if(memRead==1){
                 MAR = getSimulator().getMemory().getWord(MAR);
                 MemToReg = 1;
                }
             }

        this.getNextPipelineRegisterFile().put("ac", ac);
        this.getNextPipelineRegisterFile().put("MBR", MAR);
        this.getNextPipelineRegisterFile().put("MemToReg", MemToReg);
        this.getNextPipelineRegisterFile().put("wbReg", this.getPrevPipelineRegisterFile().get("wbReg").getValue());
        this.getNextPipelineRegisterFile().put("wb", wb);

    }

}
