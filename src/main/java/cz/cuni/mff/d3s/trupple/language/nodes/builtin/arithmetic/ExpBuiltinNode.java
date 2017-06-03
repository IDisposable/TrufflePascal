package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

@NodeInfo(shortName = "exp")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class ExpBuiltinNode extends ExpressionNode {

    @Specialization
    double exp(double value) {
        return Math.exp(value);
    }

    @Override
    public TypeDescriptor getType() {
        return RealDescriptor.getInstance();
    }

}