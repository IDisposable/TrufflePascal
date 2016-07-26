package cz.cuni.mff.d3s.trupple.language.nodes.arithmetic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.BinaryNode;


@NodeInfo(shortName = "mod")
public abstract class ModuloNode extends BinaryNode{

	@Specialization(rewriteOn = ArithmeticException.class)
    protected long mod(long left, long right) {
		return left % right;
    }
}