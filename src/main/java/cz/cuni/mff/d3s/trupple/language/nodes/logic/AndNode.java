package cz.cuni.mff.d3s.trupple.language.nodes.logic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.nodes.utils.BinaryArgumentPrimitiveTypes;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;

/**
 * Node representing logical and operation.
 *
 * This node uses specializations which means that it is not used directly but completed node is generated by Truffle.
 * {@link AndNodeGen}
 */
@NodeInfo(shortName = "and")
public abstract class AndNode extends BinaryExpressionNode {

    AndNode() {
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(BooleanDescriptor.getInstance(), BooleanDescriptor.getInstance()), BooleanDescriptor.getInstance());
    }

	@Specialization
	boolean logicalAnd(boolean left, boolean right) {
		return left && right;
	}

}
