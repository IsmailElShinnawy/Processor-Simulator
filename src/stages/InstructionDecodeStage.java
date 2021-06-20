package stages;

import exceptions.RegisterNotFoundException;
import exceptions.pcGetException;
import main.Simulator;

public class InstructionDecodeStage extends Stage {

	static final int OPCODE_MASK = 0xF0000000, R1_MASK = 0x0F800000, R2_MASK = 0x007C0000, R3_MASK = 0x0003E000,
			SHAMT_MASK = 0x00001FFF, IMM_MASK = 0x0002FFFF, ADDRESS_MASK = 0x0FFFFFFF;

	public InstructionDecodeStage(Simulator pSimSimulator) {
		super(pSimSimulator);
	}

	@Override
	public void execute() throws RegisterNotFoundException, pcGetException {
		// getting instruction from the prev pipleline register file
		int instruction = getPrevPipelineRegisterFile().get("ir").getValue();
		System.out.printf("DECODING INSTRUCTION 0b%s\n", convertToBin32(instruction));

		// extracting fields
		int opcode = (instruction & OPCODE_MASK) >>> 28; // 4 bits 32-28
		int r1 = (instruction & R1_MASK) >> 23; // 5 bits 27-23
		int r2 = (instruction & R2_MASK) >> 18; // 5 bits 22-18
		int r3 = (instruction & R3_MASK) >> 13; // 5 bits 17-13
		int shamt = instruction & SHAMT_MASK; // 13 bits 12-0
		int imm = instruction & IMM_MASK; // 18 bits 17-0
		int address = instruction & ADDRESS_MASK; // 28 bits 27-0

		// getting operands from the register file
		int r1Value = getSimulator().getRegisterFile().getRegisterValue(r1);
		int r2Value = getSimulator().getRegisterFile().getRegisterValue(r2);
		int r3Value = getSimulator().getRegisterFile().getRegisterValue(r3);

		// populating the next pipeline register file
		this.getNextPipelineRegisterFile().put("opcode", opcode);
		this.getNextPipelineRegisterFile().put("r1", r1);
		this.getNextPipelineRegisterFile().put("r2", r2);
		this.getNextPipelineRegisterFile().put("r3", r3);
		this.getNextPipelineRegisterFile().put("shamt", shamt);
		this.getNextPipelineRegisterFile().put("imm", imm);
		this.getNextPipelineRegisterFile().put("address", address);
		this.getNextPipelineRegisterFile().put("r1Value", r1Value);
		this.getNextPipelineRegisterFile().put("r2Value", r2Value);
		this.getNextPipelineRegisterFile().put("r3Value", r3Value);

		System.out.println("DECODING OUTPUT:");
		System.out.println("OPCODE: " + opcode);
		System.out.println("R1: " + r1 + " R1_VALUE: " + r1Value);
		System.out.println("R2: " + r2 + " R2_VALUE: " + r2Value);
		System.out.println("R3: " + r3 + " R3_VALUE: " + r3Value);
		System.out.println("SHAMT: " + shamt);
		System.out.println("IMM: " + imm);
		System.out.println("ADDRESS: " + address);

		// incrementing the PC by one
		getSimulator().getRegisterFile().setPCValue(getSimulator().getRegisterFile().getPCValue() + 1);

		System.out.println("\nINCREMENTING PC VALUE TO " + getSimulator().getRegisterFile().getPCValue());
		System.out.println("--------------------------------------------------");

		this.getNextPipelineRegisterFile().put("pc", getSimulator().getRegisterFile().getPCValue());
		this.getNextPipelineRegisterFile().put("ir", this.getPrevPipelineRegisterFile().get("ir").getValue());
	}

}