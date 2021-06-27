package stages;

import exceptions.RegisterFileException;
import main.Simulator;

public class InstructionDecodeStage extends Stage {

	// masks to be used in decoding
	private static final int OPCODE_MASK = 0xF0000000, R1_MASK = 0x0F800000, R2_MASK = 0x007C0000, R3_MASK = 0x0003E000,
			SHAMT_MASK = 0x00001FFF, IMM_MASK = 0x0002FFFF, NEG_IMM_MASK = 0xFFFC0000, ADDRESS_MASK = 0x0FFFFFFF;
	// flag to signal a NOP
	private int nop;

	public InstructionDecodeStage(Simulator pSimSimulator) {
		super(pSimSimulator);
	}

	@Override
	public void execute() throws RegisterFileException {
		System.out.println("DECODING STAGE");
		// if NOP signal is up or a NOP is propagated from a previous stage, then do not
		// execute anything
		if (nop == 1 || getPrevPipelineRegisterFile().get("NOP").getValue() == 1) {
			System.out.println("NO OPERATION");
			return;
		}
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
		if (((imm >> 17) & 1) == 1) { // if immediate is negative then sign extend
			imm |= NEG_IMM_MASK;
		}
		int address = instruction & ADDRESS_MASK; // 28 bits 27-0

		// getting operands from the register file
		int r1Value = getSimulator().getRegisterFile().getRegisterValue(r1);
		int r2Value = getSimulator().getRegisterFile().getRegisterValue(r2);
		int r3Value = getSimulator().getRegisterFile().getRegisterValue(r3);

		// populating the next pipeline register file
		getNextPipelineRegisterFile().put("opcode", opcode);
		getNextPipelineRegisterFile().put("r1", r1);
		getNextPipelineRegisterFile().put("r2", r2);
		getNextPipelineRegisterFile().put("r3", r3);
		getNextPipelineRegisterFile().put("shamt", shamt);
		getNextPipelineRegisterFile().put("imm", imm);
		getNextPipelineRegisterFile().put("address", address);
		getNextPipelineRegisterFile().put("r1Value", r1Value);
		getNextPipelineRegisterFile().put("r2Value", r2Value);
		getNextPipelineRegisterFile().put("r3Value", r3Value);

		System.out.println("DECODING OUTPUT:");
		System.out.println("OPCODE: " + opcode);
		System.out.println("R1: " + r1 + " R1_VALUE: " + r1Value);
		System.out.println("R2: " + r2 + " R2_VALUE: " + r2Value);
		System.out.println("R3: " + r3 + " R3_VALUE: " + r3Value);
		System.out.println("SHAMT: " + shamt);
		System.out.println("IMM: " + imm);
		System.out.println("ADDRESS: " + address);

		System.out.println("----------------------------------------------------------");

	}

	public void incrementPC() throws RegisterFileException {
		System.out.println("DECODING STAGE");
		// if NOP signal is up or a NOP is propagated from a previous stage, then do not
		// execute anything
		if (nop == 1 || getPrevPipelineRegisterFile().get("NOP").getValue() == 1) {
			System.out.println("NO OPERATION");
			// clear NOP signal
			nop = 0;
			// propagate forward the NOP signal
			getNextPipelineRegisterFile().put("NOP", 1);
			return;
		}
		System.out.printf("DECODING INSTRUCTION 0b%s\n",
				convertToBin32(getPrevPipelineRegisterFile().get("ir").getValue()));

		// incrementing the PC by one
		getSimulator().getRegisterFile().setPCValue(getSimulator().getRegisterFile().getPCValue() + 1);
		getNextPipelineRegisterFile().put("pc", getSimulator().getRegisterFile().getPCValue());
		// propagate forward the ir value
		getNextPipelineRegisterFile().put("ir", getPrevPipelineRegisterFile().get("ir").getValue());
		// propagate forward the NOP signal
		getNextPipelineRegisterFile().put("NOP", 0);

		System.out.println("INCREMENTING PC VALUE TO " + getSimulator().getRegisterFile().getPCValue());
		System.out.println("----------------------------------------------------------");
	}

	/*
	 * SETTERS
	 */
	public void setNOP(int nop) {
		this.nop = nop;
	}

}