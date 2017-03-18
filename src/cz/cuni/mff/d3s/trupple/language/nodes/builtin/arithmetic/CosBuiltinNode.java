package cz.cuni.mff.d3s.trupple.language.nodes.builtin.arithmetic;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.builtin.BuiltinNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

@NodeInfo(shortName = "cos")
@NodeChild(value = "argument", type = ExpressionNode.class)
public abstract class CosBuiltinNode extends BuiltinNode {

    public CosBuiltinNode(PascalContext context) {
        super(context);
    }

    @Specialization
    double integerCosValue(long value) {
        return Math.cos(value);
    }

    @Specialization
    double doubleCosValue(double value) {
        return Math.cos(value);
    }

}