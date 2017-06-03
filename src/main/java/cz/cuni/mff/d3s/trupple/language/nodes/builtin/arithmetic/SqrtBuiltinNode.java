package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.runtime.exceptions.SqrtInvalidArgumentException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

@NodeInfo(shortName = "sqrt")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class SqrtBuiltinNode extends ExpressionNode {

    @Specialization
    double sqrt(double value) {
        return computeSquareRoot(value);
    }

    private double computeSquareRoot(double value) {
        if (value > 0) {
            return Math.sqrt(value);
        } else {
            throw new SqrtInvalidArgumentException(value);
        }
    }

    @Override
    public TypeDescriptor getType() {
        return RealDescriptor.getInstance();
    }

}