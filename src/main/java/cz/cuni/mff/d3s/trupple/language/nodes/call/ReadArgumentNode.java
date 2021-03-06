package cz.cuni.mff.d3s.trupple.language.nodes.call;

import com.oracle.truffle.api.frame.VirtualFrame;

import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

import java.util.List;

/**
 * This node reads one argument from actual frame at specified index. It is used mainly with assignment node where this
 * node reads value of received argument and the assignment node assigns it to the variable representing the argument.
 *
 * {@link cz.cuni.mff.d3s.trupple.parser.NodeFactory#addParameterIdentifiersToLexicalScope(List)} ()}
 */
public class ReadArgumentNode extends ExpressionNode {

	private final int index;
	private final TypeDescriptor argumentType;

	public ReadArgumentNode(int index, TypeDescriptor argumentType) {
		this.index = index + 1;
        this.argumentType = argumentType;
    }

	@Override
	public Object executeGeneric(VirtualFrame frame) {
        return frame.getArguments()[index];
	}

    @Override
    public TypeDescriptor getType() {
        return this.argumentType;
    }

}
