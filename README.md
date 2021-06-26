# Processor Simulator

A simulation of a microprocessor supporting pipelined design written in Java for CSEN601 Computer System Architecture course @ GUC

## Description

This project is a simulation of a processor for a fictional ISA, the pipelined datapath of the microarchitecture is simulated using register files, memory and pipeline register files and some control signals

## Stages of pipelining

1. Instruction Fetch Stage (IF)
1. Instruction Decode Stage (ID)
1. Instruction Execution Stage (IEX)
1. Memory Acess Stage (MA)
1. Write Back Stage (WB)

## How to run simulation

1. Make sure you include your program code in `./src/program` (a sample program that tests all instructions supported is already included there)
1. Navigate to `./src/main/Controller.java` and run the main method which will start the simulation

## Printings and Output

-   On every clk cycle, stages and instructions in every stage is printed
-   Output from every stage that is passed to the next stage is printed
-   Updates (if any) to registers
-   Reads/Writes (if any) to memory locations
-   The register file after the last clock cycle
-   The memory content after the last clock cycle (printed to `./src/memory` not to console)

## Technical implementation notes

-   Simulation runs in `7+(n+(j+b)*2-1)*2` clk cycles where

    -   `n`: number of instructions executed including jump and branch instructions
    -   `j`: number of jump instructions executed (each j operation adds 2 NOP instructions)
    -   `b`: number of branch instructions executed (each branch adds 2 NOP instructions)

-   Supported ISA could be found in `./src/ISA.csv`
-   Support against control hazards is added using NOP operations
-   No support is added against data hazards
    -   if instruction @ n has a destination register rx, the correct value of rx will be available for instrucions at locations >= @ n+3
    -   if instruction @ n that is writing to @ x in memory, the correct value of the word @ x in memory will be available for instrucions at locations >= @ n+2
