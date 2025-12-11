package mars.mips.instructions.customlangs;
import mars.mips.hardware.*;
import mars.*;
import mars.util.*;
import mars.mips.instructions.*;

public class EldenRing extends CustomAssembly {
    @Override
    public String getName() {
        return "EldenRing";
    }
    @Override
    public String getDescription() {
        return "Assembly language that allows you to simulate a basic EldenRing game";
    }

    @Override
    protected void populate() {
        instructionList.add(
                new BasicInstruction("crf $t1",
                        "Crismon Flask: Add 5 points to your HP($t1)",
                        BasicInstructionFormat.I_FORMAT,
                        "001000 fffff 00000 ssssssssssssssss",
                        new SimulationCode() {
                            public void simulate(ProgramStatement statement) throws ProcessingException {
                                int[] operands = statement.getOperands();
                                int reg = operands[0];
                                int hp = RegisterFile.getValue(reg);
                                RegisterFile.updateRegister(reg, hp + 5);
                            }
                        }));
        instructionList.add(
                new BasicInstruction("cef $t2",
                        "Cerulean Flask: Add 5 points to your FP($t2)",
                        BasicInstructionFormat.R_FORMAT,
                        "000001 fffff sssss 00000 00000 100000",
                        new SimulationCode() {
                            public void simulate(ProgramStatement statement) throws ProcessingException {
                                int[] operands = statement.getOperands();
                                int rd = operands[0];
                                int current = RegisterFile.getValue(rd);
                                RegisterFile.updateRegister(rd, current + 5);
                            }
                        }));
        instructionList.add(
                new BasicInstruction("lvlup $t1, $t2, $t3, $t4",
                        "LevelUp: Add 1 point to each stat($t1-$t4)",
                        BasicInstructionFormat.R_FORMAT,
                        "000002 fffff sssss ttttt 00000 000000",
                        new SimulationCode() {
                            public void simulate(ProgramStatement statement) throws ProcessingException {
                                int[] operands = statement.getOperands();
                                for (int reg : operands) {
                                    int currVal = RegisterFile.getValue(reg);
                                    RegisterFile.updateRegister(reg, currVal + 1);
                                }
                            }
                        }));
        instructionList.add(
                new BasicInstruction("grace $t1, $t2",
                        "Grace: Restores 10 points to HP($t1) and FP($t2)",
                        BasicInstructionFormat.R_FORMAT,
                        "000003 fffff sssss 00000 00000 100001",
                        new SimulationCode() {
                            public void simulate(ProgramStatement statement) throws ProcessingException {
                                int[] operands = statement.getOperands();
                                int hpReg = operands[0];
                                int fpReg = operands[1];
                                RegisterFile.updateRegister(hpReg, RegisterFile.getValue(hpReg) + 10);
                                RegisterFile.updateRegister(fpReg, RegisterFile.getValue(fpReg) + 10);
                            }
                        }));
        instructionList.add(
                new BasicInstruction("lat $t1, $t2, $t3, $t4",
                        "Larval Tear: Set all your stats($t1-$t4) back to 1",
                        BasicInstructionFormat.R_FORMAT,
                        "000004 fffff sssss ttttt 00000 010011",
                        new SimulationCode() {
                            public void simulate(ProgramStatement statement) throws ProcessingException {
                                int[] operands = statement.getOperands();
                                for (int reg : operands) {
                                    RegisterFile.updateRegister(reg, 1);
                                }
                            }
                        }));
        instructionList.add(
                new BasicInstruction("runea $t1, $t2, $t3, $t4",
                        "Rune Arc: Add 5 points to all of your stats($t1-$t4)",
                        BasicInstructionFormat.R_FORMAT,
                        "000005 fffff sssss ttttt 00000 010100",
                        new SimulationCode() {
                            public void simulate(ProgramStatement statement) throws ProcessingException {
                                int[] operands = statement.getOperands();
                                for (int reg : operands) {
                                    int current = RegisterFile.getValue(reg);
                                    RegisterFile.updateRegister(reg, current + 5);
                                }
                            }
                        }));
        instructionList.add(
                new BasicInstruction("che $t5, $t6",
                        "Change Enemy: Change the enemies stats($t5,$t6) to random values",
                        BasicInstructionFormat.R_FORMAT,
                        "000006 fffff sssss 00000 00000 100000",
                        new SimulationCode() {
                            public void simulate(ProgramStatement statement) throws ProcessingException {
                                int[] operands = statement.getOperands();
                                RegisterFile.updateRegister(operands[0], (int) (Math.random() * 100) + 1);
                                RegisterFile.updateRegister(operands[1], (int) (Math.random() * 100) + 1);
                            }
                        }));
        instructionList.add(
                new BasicInstruction("tad $t1, $t6",
                        "Take Damage: Subtract your HP($t1) depending on the enemies STR($t6)",
                        BasicInstructionFormat.R_FORMAT,
                        "000007 fffff sssss 00000 00000 100001",
                        new SimulationCode() {
                            public void simulate(ProgramStatement statement) throws ProcessingException {
                                int[] operands = statement.getOperands();
                                int hp = RegisterFile.getValue(operands[0]);
                                int str = RegisterFile.getValue(operands[1]);
                                RegisterFile.updateRegister(operands[0], Math.max(hp - str, 0));
                            }
                        }));
        instructionList.add(
                new BasicInstruction("strd $t3, $t5",
                        "STR Damage: Subtract the enemies HP($t5) depending on your STR($t3)",
                        BasicInstructionFormat.R_FORMAT,
                        "000008 fffff sssss 00000 00000 100002",
                        new SimulationCode() {
                            public void simulate(ProgramStatement statement) throws ProcessingException {
                                int[] operands = statement.getOperands();
                                int mySTR = RegisterFile.getValue(operands[0]);
                                int enemyHP = RegisterFile.getValue(operands[1]);
                                RegisterFile.updateRegister(operands[1], Math.max(enemyHP - mySTR, 0));
                            }
                        }));
        instructionList.add(
                new BasicInstruction("intd $t4, $t5",
                        "INT Damage: Subtract the enemies HP($t5) depending on your INT($t4) and subtract 2 points from your FP($t2)",
                        BasicInstructionFormat.R_FORMAT,
                        "000009 fffff sssss 00000 00000 100003",
                        new SimulationCode() {
                            public void simulate(ProgramStatement statement) throws ProcessingException {
                                int[] operands = statement.getOperands();
                                int myINT = RegisterFile.getValue(operands[0]);
                                int enemyHP = RegisterFile.getValue(operands[1]);
                                RegisterFile.updateRegister(operands[1], Math.max(enemyHP - myINT, 0));

                                int fpReg = 10;
                                RegisterFile.updateRegister(fpReg, Math.max(RegisterFile.getValue(fpReg) - 2, 0));
                            }
                        }));
        instructionList.add(
                new BasicInstruction("sms $t3",
                        "Smithing Stone: Add 2 points to your STR($t3)",
                        BasicInstructionFormat.R_FORMAT,
                        "000010 fffff 00000 00000 00000 100004",
                        new SimulationCode() {
                            public void simulate(ProgramStatement statement) throws ProcessingException {
                                int[] operands = statement.getOperands();
                                int currentSTR = RegisterFile.getValue(operands[0]);
                                RegisterFile.updateRegister(operands[0], currentSTR + 2);
                            }
                        }));
        instructionList.add(
                new BasicInstruction("mes $t4",
                        "Memory Stone: Add 2 points to your INT($t4)",
                        BasicInstructionFormat.R_FORMAT,
                        "000011 fffff 00000 00000 00000 100005",
                        new SimulationCode() {
                            public void simulate(ProgramStatement statement) throws ProcessingException {
                                int[] operands = statement.getOperands();
                                int currentINT = RegisterFile.getValue(operands[0]);
                                RegisterFile.updateRegister(operands[0], currentINT + 2);
                            }
                        }));
        instructionList.add(
                new BasicInstruction("grune $t1, $t2, $t3, $t4",
                        "Great Rune: Doubles all of your stats($t1-$t4)",
                        BasicInstructionFormat.R_FORMAT,
                        "000012 fffff sssss ttttt 00000 100006",
                        new SimulationCode() {
                            public void simulate(ProgramStatement statement) throws ProcessingException {
                                int[] operands = statement.getOperands();
                                for (int reg : operands) {
                                    RegisterFile.updateRegister(reg, RegisterFile.getValue(reg) * 2);
                                }
                            }
                        }));
        instructionList.add(
                new BasicInstruction("gvow $t1",
                        "Golden Vow: Doubles your HP($t1)",
                        BasicInstructionFormat.R_FORMAT,
                        "000013 fffff 00000 00000 00000 100007",
                        new SimulationCode() {
                            public void simulate(ProgramStatement statement) throws ProcessingException {
                                int[] operands = statement.getOperands();
                                int reg = operands[0];
                                RegisterFile.updateRegister(reg, RegisterFile.getValue(reg) * 2);
                            }
                        }));
        instructionList.add(
                new BasicInstruction("mfla $t1, $t3, $t4",
                        "Midra's Flame: Reduce your HP($t1) by half and double your STR($t3) and INT($t4)",
                        BasicInstructionFormat.R_FORMAT,
                        "000014 fffff sssss ttttt 00000 100008",
                        new SimulationCode() {
                            public void simulate(ProgramStatement statement) throws ProcessingException {
                                int[] operands = statement.getOperands();
                                int hpReg = operands[0];
                                int strReg = operands[1];
                                int intReg = operands[2];

                                RegisterFile.updateRegister(hpReg, RegisterFile.getValue(hpReg) / 2);
                                RegisterFile.updateRegister(strReg, RegisterFile.getValue(strReg) * 2);
                                RegisterFile.updateRegister(intReg, RegisterFile.getValue(intReg) * 2);
                            }
                        }));
    }
}