
package stages;

import exceptions.RegisterNotFoundException;
import main.Simulator;

public class InstructionExecuteStage extends Stage {

	public InstructionExecuteStage(Simulator pSimSimulator) {
		super(pSimSimulator);
	}

	@Override
	public void execute() throws RegisterNotFoundException {
		System.out.printf("EXECUTING INSTRUCTION 0b%s\n",
				convertToBin32(this.getPrevPipelineRegisterFile().get("ir").getValue()));

		int opcode = this.getPrevPipelineRegisterFile().get("opcode").getValue();
		int r1Value = this.getPrevPipelineRegisterFile().get("r1Value").getValue();
		int r2Value = this.getPrevPipelineRegisterFile().get("r2Value").getValue();
		int r3Value = this.getPrevPipelineRegisterFile().get("r3Value").getValue();
		int shamt = this.getPrevPipelineRegisterFile().get("shamt").getValue();
		int imm = this.getPrevPipelineRegisterFile().get("imm").getValue();
		int address = this.getPrevPipelineRegisterFile().get("address").getValue();
		int rd = this.getPrevPipelineRegisterFile().get("r1").getValue();

		int ac = 0;
		int jFirstParameter = (this.getSimulator().getRegisterFile().getPCValue() & 0b11110000000000000000000000000000);
		// int jSecondParameter=address;
		int jump = jFirstParameter | address;
		int memoryRead = 0;
		int memoryWrite = 0;
		int wbReg = 0;
		int wb = 1;
		int mar = 0;
		switch (opcode) {
			case 0:
				System.out.println("ADD");
				System.out.printf("R2_VALUE = %d  R3_VALUE = %d\n", r2Value, r3Value);
				ac = r2Value + r3Value;
				System.out.println("OUTPUT = " + ac);
				// this.getNextPipelineRegisterFile().put("wbReg", rd);
				wbReg = rd;
				break; // Add (R)
			case 1:
				System.out.println("SUB");
				System.out.printf("R2_VALUE = %d  R3_VALUE = %d\n", r2Value, r3Value);
				ac = r2Value - r3Value;
				System.out.println("OUTPUT = " + ac);
				// this.getNextPipelineRegisterFile().put("wbReg", rd);
				wbReg = rd;
				break; // Subtract (R)
			case 2:
				System.out.println("MULI");
				System.out.printf("R2_VALUE = %d  IMM = %d\n", r2Value, imm);
				ac = r2Value * imm;
				System.out.println("OUTPUT = " + ac);
				// this.getNextPipelineRegisterFile().put("wbReg", rd);
				wbReg = rd;
				break; // Multiply immediate (I)
			case 3:
				System.out.println("ADDI");
				System.out.printf("R2_VALUE = %d  IMM = %d\n", r2Value, imm);
				ac = r2Value + imm;
				System.out.println("OUTPUT = " + ac);
				// this.getNextPipelineRegisterFile().put("wbReg", rd);
				wbReg = rd;
				break; // Add immediate (I)
			case 4:
				System.out.println("BNE");
				System.out.printf("R1_VALUE = %d  R2_VALUE = %d\n", r1Value, r2Value);
				if (r1Value != r2Value) {
					this.getSimulator().getRegisterFile()
							.setPCValue(this.getSimulator().getRegisterFile().getPCValue() + imm);
					System.out.printf("JUMPED TO @ %d\n", this.getSimulator().getRegisterFile().getPCValue());
				}
				wb = 0;
			case 5:
				System.out.println("ANDI");
				System.out.printf("R2_VALUE = %d  IMM = %d\n", r2Value, imm);
				ac = r2Value & imm;
				System.out.println("OUTPUT = " + ac);
				// this.getNextPipelineRegisterFile().put("wbReg", rd);
				wbReg = rd;
				break; // And Immediate
			case 6:
				System.out.println("ORI");
				System.out.printf("R2_VALUE = %d  IMM = %d\n", r2Value, imm);
				ac = r2Value | imm;
				System.out.println("OUTPUT = " + ac);
				// this.getNextPipelineRegisterFile().put("wbReg", rd);
				wbReg = rd;
				break;// Or Immediate
			case 7:
				System.out.printf("JUMPING TO @ %d\n", jump);
				this.getSimulator().getRegisterFile().setPCValue(jump); // Jump
				wb = 0;
			case 8:
				System.out.println("SLL");
				System.out.printf("R2_VALUE = %d  SHAMT = %d\n", r2Value, shamt);
				ac = r2Value << shamt;
				System.out.println("OUTPUT = " + ac);
				// this.getNextPipelineRegisterFile().put("wbReg", rd);
				wbReg = rd;
				break; // shift left logical
			case 9:
				System.out.println("SRL");
				System.out.printf("R2_VALUE = %d  SHAMT = %d\n", r2Value, shamt);
				ac = r2Value >>> shamt;
				System.out.println("OUTPUT = " + ac);
				// this.getNextPipelineRegisterFile().put("wbReg", rd);
				wbReg = rd;
				break; // shift right logical
			case 10:
				System.out.println("LW");
				System.out.printf("R2_VALUE (base) = %d  IMM (offset) = %d\n", r2Value, imm);
				ac = r2Value + imm;
				memoryRead = 1;
				System.out.println("MEMORY ADDRESS = " + ac);
				// this.getNextPipelineRegisterFile().put("MAR", ac);
				mar = ac;
				break; // Memory Read
			case 11:
				System.out.println("SW");
				System.out.printf("R2_VALUE (base) = %d  IMM (offset) = %d R1_VALUE = %d\n", r2Value, imm, r1Value);
				ac = r1Value;
				memoryWrite = 1;
				// this.getNextPipelineRegisterFile().put("MAR", rd);
				mar = r2Value + imm;
				wb = 0;
				break; // Memory Write
		}
		this.getNextPipelineRegisterFile().put("ac", ac);
		this.getNextPipelineRegisterFile().put("MAR", mar);
		this.getNextPipelineRegisterFile().put("memoryRead", memoryRead);
		this.getNextPipelineRegisterFile().put("memoryWrite", memoryWrite);
		this.getNextPipelineRegisterFile().put("wbReg", wbReg);
		this.getNextPipelineRegisterFile().put("wb", wb);
		this.getNextPipelineRegisterFile().put("ir", this.getPrevPipelineRegisterFile().get("ir").getValue());

		System.out.println("SENT TO NEXT STAGE:");
		System.out.println("AC = " + ac);
		System.out.println("MAR = " + mar);
		System.out.println("MEMORY_READ = " + memoryRead);
		System.out.println("MEMORY_WRITE = " + memoryWrite);
		System.out.println("WB = " + wb);
		System.out.println("WB_REG = " + wbReg);

	}
}
