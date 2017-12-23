/**
 *
 * CS472 - Computer Architecture (Fall 2017)
 *         October3rd, 2017
 */

public class Disasembler {
	private static int HexNumb;
	private static int AddressNumb = 0x9A000 - 4;

	// Machine Instructions to decode	
	private static int[] HValues = {0x00000000,0xa1020000 ,0x810AFFFC, 0x00831820,0x01263820,0x01224820,0x81180000,0x81510010,0x00624022,0x00000000,0x00000000,0x00000000,0x00000000};

	public static void main(String[] args) {
		Disasembler dis = new Disasembler();

		for (int x = 0; x < HValues.length; x++) {

			AddressNumb = AddressNumb + 4; // Address number 
			HexNumb = HValues[x]; //holds Hex values individually 

			System.out.println();

			// If instruction is R-Type execute the methods below
			if (dis.getInstructionFormat(HValues, HexNumb) == "R-Type") {
				System.out.print(Integer.toHexString(AddressNumb) + " ");

				dis.RTypeInstructionType();

				System.out.println(" " 
				+ "$" + dis.rdRegister(HexNumb) 
				+ ", " 
				+ "$" 
				+ dis.rsRegister(HexNumb) 
				+ ", "
				+ "$" + dis.rtRegister(HexNumb));
			}

			// If instructions is I-Type BUT NOT a branch, then execute the
			// following
			if (dis.getInstructionFormat(HValues, HexNumb) == "I-Type" & dis.getIOpcode(HexNumb) != 0x4
					& dis.getIOpcode(HexNumb) != 0x5) {
				
				System.out.print(Integer.toHexString(AddressNumb) + " ");
				dis.ITypeInstructionType(); // prints instruction type
				
				//prints instruction
				System.out.println(" " 
				+ "$" + dis.rtRegister(HexNumb) 
				+ ", " 
				+ dis.IOpRegister(HexNumb) 
				+ "($"
				+ dis.IrsRegister(HexNumb) + ")");
			}

			// If instruction is BEQ or BNE print the string format below
			if (dis.getIOpcode(HexNumb) == 0x4 | dis.getIOpcode(HexNumb) == 0x5) {
				System.out.print(Integer.toHexString(AddressNumb) + " ");
				dis.ITypeInstructionType();  // prints instruction type
				
				//prints instruction
				System.out.println(" " 
				+ "$" 
				+ dis.rtRegister(HexNumb) 
				+ ", " 
				+ "$" 
				+ dis.IrsRegister(HexNumb) 
				+ ", "
				+ "address " + Integer.toHexString(AddressNumb + 4 + (dis.IOpRegister(HexNumb) << 2))); //prints address PC + 4 (offset << 2)

				// If instruction is BNE print string format below
			}
		}
	}

	// *** METHODS ***

	/*
	 * Check Format - Method checks the opcode to determine if hex number is a
	 * R-Type or I-Type format
	 */
	public String getInstructionFormat(int[] HValues, int opcode) {
		String type = null;

		for (int i = 0; i < HValues.length; i++) {
			int op = (opcode >>> 26);
			if (op == 0) {
				type = "R-Type";
			}
			if (op == 1 | op >= 4 & op <= 62) {
				type = "I-Type";
			}
		}
		return type;
	}

	/*
	 * Checks what R instruction type it uses. This method calls the getFunction
	 * method and uses the function to determine the instruction type
	 */
	public void RTypeInstructionType() {
		Disasembler dis = new Disasembler();
		if (dis.getFunction(HexNumb) == 0x20) {
			System.out.print("add ");
		}
		if (dis.getFunction(HexNumb) == 0x24) {
			System.out.print("and ");
		}

		if (dis.getFunction(HexNumb) == 0x25) {
			System.out.print("or ");
		}
		if (dis.getFunction(HexNumb) == 0x2A) {
			System.out.print("slt ");
		}

		if (dis.getFunction(HexNumb) == 0x00) {
			System.out.print("sll ");
		}

		if (dis.getFunction(HexNumb) == 0x22) {
			System.out.print("sub ");
		}
	}

	/*
	 * Checks what is the I-Type instruction. Unlike the method above, it calls
	 * the getIOpcode method to determine the instruction type using the opcode
	 * number.
	 */
	public void ITypeInstructionType() {
		Disasembler dis = new Disasembler();

		if (dis.getIOpcode(HexNumb) == 0x4) {
			System.out.print("beq ");
		}
		if (dis.getIOpcode(HexNumb) == 0x5) {
			System.out.print("bne ");
		}
		if (dis.getIOpcode(HexNumb) == 0x23) {
			System.out.print("lw ");
		}
		if (dis.getIOpcode(HexNumb) == 0x2B) {
			System.out.print("sw ");
		}
	}

	// Finds the I-Type Opcode
	public int getIOpcode(int HexNumb) {
		int opcode = HexNumb >>> 26;
		return opcode;
	}

	// Finds the R-Type function
	public int getFunction(int HexNumb) {
		int funct = (((HexNumb << 26) >>> 26));
		return funct;
	}

	// **** Finding Registers Numbers ****//

	// Finds R-Type & I-Type RD register
	public int rdRegister(int HValues) {
		int rd2 = ((HValues << 16) >>> 27);
		return rd2;
	}

	// Finds R-Type Register Only
	public int rsRegister(int HValues) {
		int rs = 0;
		rs = (HValues >>> 21);
		return rs;
	}

	// Finds I-Type RS Register Only
	public int IrsRegister(int HValues) {
		int rs = 0;
		rs = (((HValues << 6) >>> 27));
		return rs;
	}

	// Finds R-Type & I-Type RT register
	public int rtRegister(int HValues) {
		int rt = 0;
		rt = (((HValues >>> 16) << 27) >>> 27);
		return rt;
	}

	// Finds I-Type Opcode Register
	public short IOpRegister(int HValues) {
		short rs = 0;
		rs = (short) ((HValues << 15) >>> 15);
		return rs;
	}
}
