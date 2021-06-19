package main;

import java.io.IOException;
import java.util.Hashtable;

import exceptions.AssemblerException;
import exceptions.MemoryWriteException;
import util.Scanner;

public class Assembler {

    private Simulator simSimulator;
    private String strISAPath = "src/ISA.csv";
    private Hashtable<String, Integer> htblInstructionOpCode;
    private Hashtable<String, Character> htblInstructionType;
    private Hashtable<String, Integer> htblRegisterNameNumber;
    private Scanner aFileScanner;

    /**
     * Constructor for the assembler
     * 
     * @param pStrFileName  the path to the program file to be assembled
     * @param pSimSimulator and instance of the simulator that this assembler
     *                      belongs to
     * @throws IOException when IO fails
     */
    public Assembler(String pStrFileName, Simulator pSimSimulator) throws IOException {
        this.simSimulator = pSimSimulator;
        aFileScanner = new Scanner(pStrFileName, " ");

        htblInstructionOpCode = new Hashtable<String, Integer>();
        htblInstructionType = new Hashtable<String, Character>();
        htblRegisterNameNumber = new Hashtable<String, Integer>();

        parseISA(); // parses the ISA file
        mapRegistersToNumbers(); // populates the register table
    }

    /**
     * Parses the ISA.csv file that represents the supported ISA and stores relevant
     * data in the htblInstructionOpCode and the htblInstructionType variables
     * 
     * @throws IOException when IO fails
     */
    private void parseISA() throws IOException {
        Scanner sc = new Scanner(strISAPath, ",");
        sc.nextLine();
        while (sc.ready()) {
            String ins = sc.next();
            char type = sc.next().charAt(0);
            int opcode = sc.nextInt();

            htblInstructionOpCode.put(ins, opcode);
            htblInstructionType.put(ins, type);
        }
    }

    /**
     * Helper method to populate the htblRegisterNameNumber table with the register
     * name key and the register number value
     */
    private void mapRegistersToNumbers() {
        for (int i = 0; i <= 32; ++i) {
            htblRegisterNameNumber.put("R" + i, i);
        }
    }

    private void setSimulatorRegistersNames() {
    }

    /**
     * Main method of the Assembler instance that parses the program file and by
     * consulting relevant hash-tables it converts the parsed instruction to
     * equivalent machine code, also it stores the instructions in the simulators
     * memory
     * 
     * @throws IOException          when IO fails
     * @throws AssemblerException   when syntax errors occur in the program file
     *                              such as unsupported instruction and unknown
     *                              registers
     * @throws MemoryWriteException
     */
    public void assemble() throws IOException, AssemblerException, MemoryWriteException {
        System.out.println("ASSEMBLING FILE...");
        // counter used to keep track of the instructions number and used in memory
        // addressing
        int iCount = 0;
        while (aFileScanner.ready()) {
            // reads the operation that needs to be executed
            String ins = aFileScanner.next().toUpperCase();
            int opcode;
            char type;
            try {
                // consults the table to find the correct opcode for this operation
                opcode = htblInstructionOpCode.get(ins);
                // consults the table to find the correct type for this operation
                type = htblInstructionType.get(ins);
            } catch (NullPointerException npe) {
                throw new AssemblerException(String.format("Unsupported instruction `%s` at line %d", ins, iCount + 1));
            }
            // initializes the mcode with 0 which will be set later
            int mcode = 0;
            switch (type) {
                case 'R': {
                    // every R-type instruction has 3 fields other than operation
                    // R1 R2 R3 in case of all R-type except for SLL and SRL
                    // R1 R2 IMM in of SLL and SRL
                    int field1, field2, field3;
                    try {
                        // gets the register addresses of the first two fields
                        field1 = htblRegisterNameNumber.get(aFileScanner.next().toUpperCase());
                        field2 = htblRegisterNameNumber.get(aFileScanner.next().toUpperCase());
                    } catch (NullPointerException npe) {
                        throw new AssemblerException(String.format("Unknown register used at line %d", iCount + 1));
                    }

                    // sets the machine code field by field
                    mcode |= (opcode << 28); // add OPCODE
                    mcode |= (field1 << 23); // add R1
                    mcode |= (field2 << 18); // add R2

                    // add R3 or SHAMT
                    if (ins.equals("SLL") || ins.equals("SRL")) {
                        // add SHAMT if SLL or SRL instruction
                        field3 = aFileScanner.nextInt();
                        mcode |= field3;
                    } else {
                        // add R3 if not SLL and not SRL instruction
                        field3 = htblRegisterNameNumber.get(aFileScanner.next().toUpperCase());
                        mcode |= (field3 << 13);
                    }

                    break;
                }
                case 'I': {
                    // every I-type instruction has 3 fields other than operation
                    int field1, field2, field3;
                    try {
                        // gets the register addresses of the first two fields
                        field1 = htblRegisterNameNumber.get(aFileScanner.next().toUpperCase());
                        field2 = htblRegisterNameNumber.get(aFileScanner.next().toUpperCase());
                    } catch (NullPointerException npe) {
                        throw new AssemblerException(String.format("Unknown register used at line %d", iCount + 1));
                    }
                    // gets the immediate value
                    field3 = aFileScanner.nextInt();

                    // sets the machine code field by field
                    mcode |= (opcode << 28); // add OPCODE
                    mcode |= (field1 << 23); // add R1
                    mcode |= (field2 << 18); // add R2
                    mcode |= field3; // add IMM

                    break;
                }
                case 'J': {
                    // every J-type instruction has 1 field other than operation
                    // gets the address
                    int field1 = aFileScanner.nextInt();

                    // sets the machine code field by field
                    mcode |= (opcode << 28); // add OPCODE
                    mcode |= field1; // add ADDRESS

                    break;
                }
            }
            // just for debugging
            System.out.println(convertToBinary32(mcode));

            // sets the memory address at iCount to the mcode calculated
            simSimulator.getMemory().setWord(iCount++, mcode);
        }
        System.out.println("FILE ASSEMBLED");
    }

    private void normalize() {

    }

    /**
     * Helper method that converts an integer to its 32-bit binary representation as
     * Integer.toBinaryString(int) does not show left most 0s
     * 
     * @param x
     * @return
     */
    private String convertToBinary32(int x) {
        String res = "";
        for (int i = 0; i < 32; ++i) {
            res = (((x >> i) & 1) == 1 ? 1 : 0) + res;
        }
        return res;
    }

    public static void main(String[] args) throws IOException, AssemblerException, MemoryWriteException {
        Assembler a = new Assembler("src/program", null);
        a.assemble();
    }

}
