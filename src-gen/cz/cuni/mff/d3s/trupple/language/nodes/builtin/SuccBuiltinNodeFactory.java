// CheckStyle: start generated
package cz.cuni.mff.d3s.trupple.language.nodes.builtin;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.internal.SpecializationNode;
import com.oracle.truffle.api.dsl.internal.SpecializedNode;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import com.oracle.truffle.api.nodes.UnexpectedResultException;
import cz.cuni.mff.d3s.trupple.language.PascalTypesGen;
import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import java.util.Arrays;
import java.util.List;

@GeneratedBy(SuccBuiltinNode.class)
@SuppressWarnings({"unchecked", "rawtypes"})
public final class SuccBuiltinNodeFactory implements NodeFactory<SuccBuiltinNode> {

    private static SuccBuiltinNodeFactory instance;

    private SuccBuiltinNodeFactory() {
    }

    @Override
    public Class<SuccBuiltinNode> getNodeClass() {
        return SuccBuiltinNode.class;
    }

    @Override
    public List getExecutionSignature() {
        return Arrays.asList(ExpressionNode.class);
    }

    @Override
    public List getNodeSignatures() {
        return Arrays.asList(Arrays.asList(PascalContext.class, ExpressionNode.class));
    }

    @Override
    public SuccBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof PascalContext) && (arguments[1] == null || arguments[1] instanceof ExpressionNode)) {
            return create((PascalContext) arguments[0], (ExpressionNode) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<SuccBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new SuccBuiltinNodeFactory();
        }
        return instance;
    }

    public static SuccBuiltinNode create(PascalContext context, ExpressionNode argument) {
        return new SuccBuiltinNodeGen(context, argument);
    }

    @GeneratedBy(SuccBuiltinNode.class)
    public static final class SuccBuiltinNodeGen extends SuccBuiltinNode implements SpecializedNode {

        @Child private ExpressionNode argument_;
        @CompilationFinal private Class<?> argumentType_;
        @Child private BaseNode_ specialization_;

        private SuccBuiltinNodeGen(PascalContext context, ExpressionNode argument) {
            super(context);
            this.argument_ = argument;
            this.specialization_ = UninitializedNode_.create(this);
        }

        @Override
        public NodeCost getCost() {
            return specialization_.getNodeCost();
        }

        @Override
        public Object executeGeneric(VirtualFrame frameValue) {
            return specialization_.execute(frameValue);
        }

        @Override
        public void executeVoid(VirtualFrame frameValue) {
            specialization_.executeVoid(frameValue);
            return;
        }

        @Override
        public boolean executeBoolean(VirtualFrame frameValue) throws UnexpectedResultException {
            return specialization_.executeBoolean(frameValue);
        }

        @Override
        public char executeChar(VirtualFrame frameValue) throws UnexpectedResultException {
            return specialization_.executeChar(frameValue);
        }

        @Override
        public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
            return specialization_.executeLong(frameValue);
        }

        @Override
        public SpecializationNode getSpecializationNode() {
            return specialization_;
        }

        @Override
        public Node deepCopy() {
            return SpecializationNode.updateRoot(super.deepCopy());
        }

        @GeneratedBy(SuccBuiltinNode.class)
        private abstract static class BaseNode_ extends SpecializationNode {

            @CompilationFinal protected SuccBuiltinNodeGen root;

            BaseNode_(SuccBuiltinNodeGen root, int index) {
                super(index);
                this.root = root;
            }

            @Override
            protected final void setRoot(Node root) {
                this.root = (SuccBuiltinNodeGen) root;
            }

            @Override
            protected final Node[] getSuppliedChildren() {
                return new Node[] {root.argument_};
            }

            @Override
            public final Object acceptAndExecute(Frame frameValue, Object argumentValue) {
                return this.execute_((VirtualFrame) frameValue, argumentValue);
            }

            public abstract Object execute_(VirtualFrame frameValue, Object argumentValue);

            public Object execute(VirtualFrame frameValue) {
                Object argumentValue_ = executeArgument_(frameValue);
                return execute_(frameValue, argumentValue_);
            }

            public void executeVoid(VirtualFrame frameValue) {
                execute(frameValue);
                return;
            }

            public boolean executeBoolean(VirtualFrame frameValue) throws UnexpectedResultException {
                return PascalTypesGen.expectBoolean(execute(frameValue));
            }

            public char executeChar(VirtualFrame frameValue) throws UnexpectedResultException {
                return PascalTypesGen.expectCharacter(execute(frameValue));
            }

            public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
                return PascalTypesGen.expectLong(execute(frameValue));
            }

            @Override
            protected final SpecializationNode createNext(Frame frameValue, Object argumentValue) {
                if (argumentValue instanceof Long) {
                    return Succ0Node_.create(root);
                }
                if (argumentValue instanceof Character) {
                    return Succ1Node_.create(root);
                }
                if (argumentValue instanceof Boolean) {
                    return Succ2Node_.create(root);
                }
                if (argumentValue instanceof EnumValue) {
                    return Succ3Node_.create(root);
                }
                return null;
            }

            @Override
            protected final SpecializationNode createPolymorphic() {
                return PolymorphicNode_.create(root);
            }

            protected final BaseNode_ getNext() {
                return (BaseNode_) this.next;
            }

            protected final Object executeArgument_(Frame frameValue) {
                Class<?> argumentType_ = root.argumentType_;
                try {
                    if (argumentType_ == boolean.class) {
                        return root.argument_.executeBoolean((VirtualFrame) frameValue);
                    } else if (argumentType_ == char.class) {
                        return root.argument_.executeChar((VirtualFrame) frameValue);
                    } else if (argumentType_ == long.class) {
                        return root.argument_.executeLong((VirtualFrame) frameValue);
                    } else if (argumentType_ == null) {
                        CompilerDirectives.transferToInterpreterAndInvalidate();
                        Class<?> _type = Object.class;
                        try {
                            Object _value = root.argument_.executeGeneric((VirtualFrame) frameValue);
                            if (_value instanceof Boolean) {
                                _type = boolean.class;
                            } else if (_value instanceof Character) {
                                _type = char.class;
                            } else if (_value instanceof Long) {
                                _type = long.class;
                            } else {
                                _type = Object.class;
                            }
                            return _value;
                        } finally {
                            root.argumentType_ = _type;
                        }
                    } else {
                        return root.argument_.executeGeneric((VirtualFrame) frameValue);
                    }
                } catch (UnexpectedResultException ex) {
                    root.argumentType_ = Object.class;
                    return ex.getResult();
                }
            }

        }
        @GeneratedBy(SuccBuiltinNode.class)
        private static final class UninitializedNode_ extends BaseNode_ {

            UninitializedNode_(SuccBuiltinNodeGen root) {
                super(root, 2147483647);
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object argumentValue) {
                return uninitialized(frameValue, argumentValue);
            }

            static BaseNode_ create(SuccBuiltinNodeGen root) {
                return new UninitializedNode_(root);
            }

        }
        @GeneratedBy(SuccBuiltinNode.class)
        private static final class PolymorphicNode_ extends BaseNode_ {

            PolymorphicNode_(SuccBuiltinNodeGen root) {
                super(root, 0);
            }

            @Override
            public SpecializationNode merge(SpecializationNode newNode, Frame frameValue, Object argumentValue) {
                return polymorphicMerge(newNode, super.merge(newNode, frameValue, argumentValue));
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object argumentValue) {
                return getNext().execute_(frameValue, argumentValue);
            }

            static BaseNode_ create(SuccBuiltinNodeGen root) {
                return new PolymorphicNode_(root);
            }

        }
        @GeneratedBy(methodName = "succ(long)", value = SuccBuiltinNode.class)
        private static final class Succ0Node_ extends BaseNode_ {

            Succ0Node_(SuccBuiltinNodeGen root) {
                super(root, 1);
            }

            @Override
            public Object execute(VirtualFrame frameValue) {
                try {
                    return executeLong(frameValue);
                } catch (UnexpectedResultException ex) {
                    return ex.getResult();
                }
            }

            @Override
            public long executeLong(VirtualFrame frameValue) throws UnexpectedResultException {
                long argumentValue_;
                try {
                    argumentValue_ = root.argument_.executeLong(frameValue);
                } catch (UnexpectedResultException ex) {
                    return PascalTypesGen.expectLong(getNext().execute_(frameValue, ex.getResult()));
                }
                return root.succ(argumentValue_);
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object argumentValue) {
                if (argumentValue instanceof Long) {
                    long argumentValue_ = (long) argumentValue;
                    return root.succ(argumentValue_);
                }
                return getNext().execute_(frameValue, argumentValue);
            }

            static BaseNode_ create(SuccBuiltinNodeGen root) {
                return new Succ0Node_(root);
            }

        }
        @GeneratedBy(methodName = "succ(char)", value = SuccBuiltinNode.class)
        private static final class Succ1Node_ extends BaseNode_ {

            Succ1Node_(SuccBuiltinNodeGen root) {
                super(root, 2);
            }

            @Override
            public Object execute(VirtualFrame frameValue) {
                try {
                    return executeChar(frameValue);
                } catch (UnexpectedResultException ex) {
                    return ex.getResult();
                }
            }

            @Override
            public char executeChar(VirtualFrame frameValue) throws UnexpectedResultException {
                char argumentValue_;
                try {
                    argumentValue_ = root.argument_.executeChar(frameValue);
                } catch (UnexpectedResultException ex) {
                    return PascalTypesGen.expectCharacter(getNext().execute_(frameValue, ex.getResult()));
                }
                return root.succ(argumentValue_);
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object argumentValue) {
                if (argumentValue instanceof Character) {
                    char argumentValue_ = (char) argumentValue;
                    return root.succ(argumentValue_);
                }
                return getNext().execute_(frameValue, argumentValue);
            }

            static BaseNode_ create(SuccBuiltinNodeGen root) {
                return new Succ1Node_(root);
            }

        }
        @GeneratedBy(methodName = "succ(boolean)", value = SuccBuiltinNode.class)
        private static final class Succ2Node_ extends BaseNode_ {

            Succ2Node_(SuccBuiltinNodeGen root) {
                super(root, 3);
            }

            @Override
            public Object execute(VirtualFrame frameValue) {
                try {
                    return executeBoolean(frameValue);
                } catch (UnexpectedResultException ex) {
                    return ex.getResult();
                }
            }

            @Override
            public boolean executeBoolean(VirtualFrame frameValue) throws UnexpectedResultException {
                boolean argumentValue_;
                try {
                    argumentValue_ = root.argument_.executeBoolean(frameValue);
                } catch (UnexpectedResultException ex) {
                    return PascalTypesGen.expectBoolean(getNext().execute_(frameValue, ex.getResult()));
                }
                return root.succ(argumentValue_);
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object argumentValue) {
                if (argumentValue instanceof Boolean) {
                    boolean argumentValue_ = (boolean) argumentValue;
                    return root.succ(argumentValue_);
                }
                return getNext().execute_(frameValue, argumentValue);
            }

            static BaseNode_ create(SuccBuiltinNodeGen root) {
                return new Succ2Node_(root);
            }

        }
        @GeneratedBy(methodName = "succ(EnumValue)", value = SuccBuiltinNode.class)
        private static final class Succ3Node_ extends BaseNode_ {

            Succ3Node_(SuccBuiltinNodeGen root) {
                super(root, 4);
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object argumentValue) {
                if (argumentValue instanceof EnumValue) {
                    EnumValue argumentValue_ = (EnumValue) argumentValue;
                    return root.succ(argumentValue_);
                }
                return getNext().execute_(frameValue, argumentValue);
            }

            static BaseNode_ create(SuccBuiltinNodeGen root) {
                return new Succ3Node_(root);
            }

        }
    }
}
