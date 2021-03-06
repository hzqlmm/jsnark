/*******************************************************************************
 * Author: Ahmed Kosba <akosba@cs.umd.edu>
 *******************************************************************************/
package circuit.operations.primitive;

import java.math.BigInteger;

import circuit.config.Config;
import circuit.structure.Wire;

public class ConstMulBasicOp extends BasicOp {

	private BigInteger constInteger;

	public ConstMulBasicOp(Wire w, Wire out, BigInteger constInteger,
			String...desc) {
		super(new Wire[] { w }, new Wire[] { out }, desc);
		boolean sign = constInteger.signum() == -1;
		if (!sign) {
			constInteger = constInteger.mod(Config.FIELD_PRIME);
			opcode = "const-mul-" + constInteger.toString(16);
			this.constInteger =constInteger;
		} else {
			constInteger = constInteger.negate();
			constInteger = constInteger.mod(Config.FIELD_PRIME);
			opcode = "const-mul-neg-" + constInteger.toString(16);
			this.constInteger = Config.FIELD_PRIME.subtract(constInteger);
		}
	}

	@Override
	public void compute(BigInteger[] assignment) {
		BigInteger result = assignment[inputs[0].getWireId()].multiply(constInteger);
		if (result.bitLength() >= Config.LOG2_FIELD_PRIME) {
			result = result.mod(Config.FIELD_PRIME);
		}
		assignment[outputs[0].getWireId()] = result;
	}

}