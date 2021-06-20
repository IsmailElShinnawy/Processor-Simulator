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

		// extracting fields
		int opcode = (instruction & OPCODE_MASK) >>> 28; // 4 bits 32-28
		int rd = (instruction & R1_MASK) >> 23; // 5 bits 27-23
		int rs = (instruction & R2_MASK) >> 18; // 5 bits 22-18
		int rt = (instruction & R3_MASK) >> 13; // 5 bits 17-13
		int shamt = instruction & SHAMT_MASK; // 13 bits 12-0
		int imm = instruction & IMM_MASK; // 18 bits 17-0
		int address = instruction & ADDRESS_MASK; // 28 bits 27-0

		// getting operands from the register file
		int rsValue = getSimulator().getRegisterFile().getRegisterValue(rs);
		int rtValue = getSimulator().getRegisterFile().getRegisterValue(rt);

		// populating the next pipeline register file
		this.getNextPipelineRegisterFile().put("opcode", opcode);
		this.getNextPipelineRegisterFile().put("rs", rs);
		this.getNextPipelineRegisterFile().put("rt", rt);
		this.getNextPipelineRegisterFile().put("rd", rd);
		this.getNextPipelineRegisterFile().put("shamt", shamt);
		this.getNextPipelineRegisterFile().put("imm", imm);
		this.getNextPipelineRegisterFile().put("address", address);
		this.getNextPipelineRegisterFile().put("rsValue", rsValue);
		this.getNextPipelineRegisterFile().put("rtValue", rtValue);

		// incrementing the PC by one
		getSimulator().getRegisterFile().setPCValue(getSimulator().getRegisterFile().getPCValue() + 1);

		this.getNextPipelineRegisterFile().put("pc", getSimulator().getRegisterFile().getPCValue());
	}

}