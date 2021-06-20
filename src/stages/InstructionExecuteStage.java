package stages;

import exceptions.RegisterNotFoundException;
import main.Simulator;

public class InstructionExecuteStage extends Stage {

	public InstructionExecuteStage(Simulator pSimSimulator) {
		super(pSimSimulator);
	}

	@Override
	public void execute() throws RegisterNotFoundException {
		int opcode = this.getPrevPipelineRegisterFile().get("opcode").getValue();
		int rsValue = this.getPrevPipelineRegisterFile().get("rsValue").getValue();
		int rtValue = this.getPrevPipelineRegisterFile().get("rtValue").getValue();
		int shamt = this.getPrevPipelineRegisterFile().get("shamt").getValue();
		int imm = this.getPrevPipelineRegisterFile().get("imm").getValue();
		int address = this.getPrevPipelineRegisterFile().get("address").getValue();
		int rd = this.getPrevPipelineRegisterFile().get("rd").getValue();

		int ac = 0;
		int jFirstParameter = (this.getSimulator().getRegisterFile().getPCValue() & 0b11110000000000000000000000000000);
		// int jSecondParameter=address;
		int jump = jFirstParameter | address;
		int memoryRead = 0;
		int memoryWrite = 0;
		switch (opcode) {
			case 0:
				ac = rsValue + rtValue;
				this.getNextPipelineRegisterFile().put("wbReg", rd);
				break; // Add (R)
			case 1:
				ac = rsValue - rtValue;
				this.getNextPipelineRegisterFile().put("wbReg", rd);
				break; // Subtract (R)
			case 2:
				ac = rsValue * imm;
				this.getNextPipelineRegisterFile().put("wbReg", rd);
				break; // Multiply immediate (I)
			case 3:
				ac = rsValue + imm;
				this.getNextPipelineRegisterFile().put("wbReg", rd);
				break; // Add immediate (I)
			case 4:
				this.getSimulator().getRegisterFile()
						.setPCValue(this.getSimulator().getRegisterFile().getPCValue() + imm);
			case 5:
				ac = rsValue & imm;
				this.getNextPipelineRegisterFile().put("wbReg", rd);
				break; // And Immediate
			case 6:
				ac = rsValue | imm;
				this.getNextPipelineRegisterFile().put("wbReg", rd);
				break;// Or Immediate
			case 7:
				this.getSimulator().getRegisterFile().setPCValue(jump); // Jump
			case 8:
				ac = rsValue << shamt;
				this.getNextPipelineRegisterFile().put("wbReg", rd);
				break; // shift left logical
			case 9:
				ac = rsValue >>> shamt;
				this.getNextPipelineRegisterFile().put("wbReg", rd);
				break; // shift right logical
			case 10:
				ac = rsValue + imm;
				memoryRead = 1;
				this.getNextPipelineRegisterFile().put("MAR", rd);
				break; // Memory Read
			case 11:
				ac = rsValue + imm;
				memoryWrite = 1;
				this.getNextPipelineRegisterFile().put("MAR", rd);
				break; // Memory Write
		}
		this.getNextPipelineRegisterFile().put("ac", ac);
		this.getNextPipelineRegisterFile().put("memoryRead", memoryRead);
		this.getNextPipelineRegisterFile().put("memoryWrite", memoryWrite);
	}
}
