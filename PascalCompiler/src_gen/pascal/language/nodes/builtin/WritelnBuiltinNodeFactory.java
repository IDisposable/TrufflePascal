// CheckStyle: start generated
package pascal.language.nodes.builtin;

import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.dsl.GeneratedBy;
import com.oracle.truffle.api.dsl.NodeFactory;
import com.oracle.truffle.api.dsl.internal.NodeFactoryBase;
import com.oracle.truffle.api.dsl.internal.SpecializationNode;
import com.oracle.truffle.api.dsl.internal.SpecializedNode;
import com.oracle.truffle.api.frame.Frame;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeCost;
import pascal.language.nodes.ExpressionNode;
import pascal.language.runtime.PascalContext;

@GeneratedBy(WritelnBuiltinNode.class)
public final class WritelnBuiltinNodeFactory extends NodeFactoryBase<WritelnBuiltinNode> {

    private static WritelnBuiltinNodeFactory instance;

    private WritelnBuiltinNodeFactory() {
        super(WritelnBuiltinNode.class, new Class<?>[] {ExpressionNode.class}, new Class<?>[][] {new Class<?>[] {ExpressionNode[].class, PascalContext.class}});
    }

    @Override
    public WritelnBuiltinNode createNode(Object... arguments) {
        if (arguments.length == 2 && (arguments[0] == null || arguments[0] instanceof ExpressionNode[]) && (arguments[1] == null || arguments[1] instanceof PascalContext)) {
            return create((ExpressionNode[]) arguments[0], (PascalContext) arguments[1]);
        } else {
            throw new IllegalArgumentException("Invalid create signature.");
        }
    }

    public static NodeFactory<WritelnBuiltinNode> getInstance() {
        if (instance == null) {
            instance = new WritelnBuiltinNodeFactory();
        }
        return instance;
    }

    public static WritelnBuiltinNode create(ExpressionNode[] arguments, PascalContext context) {
        return new WritelnBuiltinNodeGen(arguments, context);
    }

    @GeneratedBy(WritelnBuiltinNode.class)
    public static final class WritelnBuiltinNodeGen extends WritelnBuiltinNode implements SpecializedNode {

        private final PascalContext context;
        @Child private ExpressionNode arguments0_;
        @Child private BaseNode_ specialization_;

        private WritelnBuiltinNodeGen(ExpressionNode[] arguments, PascalContext context) {
            this.context = context;
            this.arguments0_ = arguments != null && 0 < arguments.length ? arguments[0] : null;
            this.specialization_ = UninitializedNode_.create(this);
        }

        @Override
        public PascalContext getContext() {
            return this.context;
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
        public SpecializationNode getSpecializationNode() {
            return specialization_;
        }

        @Override
        public Node deepCopy() {
            return SpecializationNode.updateRoot(super.deepCopy());
        }

        @GeneratedBy(WritelnBuiltinNode.class)
        private abstract static class BaseNode_ extends SpecializationNode {

            @CompilationFinal protected WritelnBuiltinNodeGen root;

            BaseNode_(WritelnBuiltinNodeGen root, int index) {
                super(index);
                this.root = root;
            }

            @Override
            protected final void setRoot(Node root) {
                this.root = (WritelnBuiltinNodeGen) root;
            }

            @Override
            protected final Node[] getSuppliedChildren() {
                return new Node[] {root.arguments0_};
            }

            @Override
            public final Object acceptAndExecute(Frame frameValue, Object arguments0Value) {
                return this.execute_((VirtualFrame) frameValue, arguments0Value);
            }

            public abstract Object execute_(VirtualFrame frameValue, Object arguments0Value);

            public Object execute(VirtualFrame frameValue) {
                Object arguments0Value_ = root.arguments0_.executeGeneric(frameValue);
                return execute_(frameValue, arguments0Value_);
            }

            public void executeVoid(VirtualFrame frameValue) {
                execute(frameValue);
                return;
            }

            @Override
            protected final SpecializationNode createNext(Frame frameValue, Object arguments0Value) {
                if (arguments0Value instanceof String) {
                    return Println0Node_.create(root);
                }
                return Println1Node_.create(root);
            }

            @Override
            protected final SpecializationNode createPolymorphic() {
                return PolymorphicNode_.create(root);
            }

            protected final BaseNode_ getNext() {
                return (BaseNode_) this.next;
            }

        }
        @GeneratedBy(WritelnBuiltinNode.class)
        private static final class UninitializedNode_ extends BaseNode_ {

            UninitializedNode_(WritelnBuiltinNodeGen root) {
                super(root, 2147483647);
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object arguments0Value) {
                return uninitialized(frameValue, arguments0Value);
            }

            static BaseNode_ create(WritelnBuiltinNodeGen root) {
                return new UninitializedNode_(root);
            }

        }
        @GeneratedBy(WritelnBuiltinNode.class)
        private static final class PolymorphicNode_ extends BaseNode_ {

            PolymorphicNode_(WritelnBuiltinNodeGen root) {
                super(root, 0);
            }

            @Override
            public SpecializationNode merge(SpecializationNode newNode, Frame frameValue, Object arguments0Value) {
                return polymorphicMerge(newNode, super.merge(newNode, frameValue, arguments0Value));
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object arguments0Value) {
                return getNext().execute_(frameValue, arguments0Value);
            }

            static BaseNode_ create(WritelnBuiltinNodeGen root) {
                return new PolymorphicNode_(root);
            }

        }
        @GeneratedBy(methodName = "println(String)", value = WritelnBuiltinNode.class)
        private static final class Println0Node_ extends BaseNode_ {

            Println0Node_(WritelnBuiltinNodeGen root) {
                super(root, 1);
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object arguments0Value) {
                if (arguments0Value instanceof String) {
                    String arguments0Value_ = (String) arguments0Value;
                    return root.println(arguments0Value_);
                }
                return getNext().execute_(frameValue, arguments0Value);
            }

            static BaseNode_ create(WritelnBuiltinNodeGen root) {
                return new Println0Node_(root);
            }

        }
        @GeneratedBy(methodName = "println(Object)", value = WritelnBuiltinNode.class)
        private static final class Println1Node_ extends BaseNode_ {

            Println1Node_(WritelnBuiltinNodeGen root) {
                super(root, 2);
            }

            @Override
            public Object execute_(VirtualFrame frameValue, Object arguments0Value) {
                return root.println(arguments0Value);
            }

            static BaseNode_ create(WritelnBuiltinNodeGen root) {
                return new Println1Node_(root);
            }

        }
    }
}
