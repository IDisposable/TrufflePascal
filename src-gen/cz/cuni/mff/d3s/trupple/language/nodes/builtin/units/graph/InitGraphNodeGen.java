// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin.units.graph;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalString;

@GeneratedBy(InitGraphNode.class)
public final class InitGraphNodeGen extends InitGraphNode {

    @Child private ExpressionNode child0_;
    @Child private ExpressionNode child1_;
    @Child private ExpressionNode child2_;
    @CompilationFinal private boolean seenUnsupported0;

    private InitGraphNodeGen(ExpressionNode child0, ExpressionNode child1, ExpressionNode child2) {
        this.child0_ = child0;
        this.child1_ = child1;
        this.child2_ = child2;
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        long child0Value_;
        try {
            child0Value_ = child0_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            Object child1Value = child1_.executeGeneric(frameValue);
            Object child2Value = child2_.executeGeneric(frameValue);
            throw unsupported(ex.getResult(), child1Value, child2Value);
        }
        long child1Value_;
        try {
            child1Value_ = child1_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            Object child2Value = child2_.executeGeneric(frameValue);
            throw unsupported(child0Value_, ex.getResult(), child2Value);
        }
        PascalString child2Value_;
        try {
            child2Value_ = expectPascalString(child2_.executeGeneric(frameValue));
        } catch (UnexpectedResultException ex) {
            throw unsupported(child0Value_, child1Value_, ex.getResult());
        }
        this.initGraph(child0Value_, child1Value_, child2Value_);
        return;
    }

    private UnsupportedSpecializationException unsupported(Object child0Value, Object child1Value, Object child2Value) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {child0_, child1_, child2_}, child0Value, child1Value, child2Value);
    }

    private static PascalString expectPascalString(Object value) throws UnexpectedResultException {
        if (value instanceof PascalString) {
            return (PascalString) value;
        }
        throw new UnexpectedResultException(value);
    }

    public static InitGraphNode create(ExpressionNode child0, ExpressionNode child1, ExpressionNode child2) {
        return new InitGraphNodeGen(child0, child1, child2);
    }

}
