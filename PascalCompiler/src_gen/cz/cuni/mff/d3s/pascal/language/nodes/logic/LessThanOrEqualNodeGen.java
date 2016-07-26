// CheckStyle: start generated
package cz.cuni.mff.d3s.pascal.language.nodes.logic;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.UnsupportedSpecializationException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.pascal.language.nodes.ExpressionNode;

@GeneratedBy(LessThanOrEqualNode.class)
public final class LessThanOrEqualNodeGen extends LessThanOrEqualNode {

    @Child private ExpressionNode leftNode_;
    @Child private ExpressionNode rightNode_;
    @CompilationFinal private boolean seenUnsupported0;

    private LessThanOrEqualNodeGen(ExpressionNode leftNode, ExpressionNode rightNode) {
        this.leftNode_ = leftNode;
        this.rightNode_ = rightNode;
    }

    @Override
    public NodeCost getCost() {
        return NodeCost.MONOMORPHIC;
    }

    @Override
    public Object executeGeneric(VirtualFrame frameValue) {
        return executeBoolean(frameValue);
    }

    @Override
    public boolean executeBoolean(VirtualFrame frameValue) {
        long leftNodeValue_;
        try {
            leftNodeValue_ = leftNode_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            Object rightNodeValue = rightNode_.executeGeneric(frameValue);
            throw unsupported(ex.getResult(), rightNodeValue);
        }
        long rightNodeValue_;
        try {
            rightNodeValue_ = rightNode_.executeLong(frameValue);
        } catch (UnexpectedResultException ex) {
            throw unsupported(leftNodeValue_, ex.getResult());
        }
        return this.lessThanOrEqual(leftNodeValue_, rightNodeValue_);
    }

    @Override
    public void executeVoid(VirtualFrame frameValue) {
        executeBoolean(frameValue);
        return;
    }

    private UnsupportedSpecializationException unsupported(Object leftNodeValue, Object rightNodeValue) {
        if (!seenUnsupported0) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            seenUnsupported0 = true;
        }
        return new UnsupportedSpecializationException(this, new Node[] {leftNode_, rightNode_}, leftNodeValue, rightNodeValue);
    }

    public static LessThanOrEqualNode create(ExpressionNode leftNode, ExpressionNode rightNode) {
        return new LessThanOrEqualNodeGen(leftNode, rightNode);
    }

}
