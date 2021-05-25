package cz.cuni.mff.d3s.trupple.language.nodes.builtin.file;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.PascalLanguage;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.TextFileValue;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;

/**
 * Node representing Pascal's eoln built in functions. Returns true if we are the end of line in the input. If it is
 * called without arguments it uses standard input.
 *
 * This node uses specializations which means that it is not used directly but completed node is generated by Truffle.
 * {@link EolnBuiltinNodeGen}
 */
@NodeInfo(shortName = "eoln")
@NodeChild(value = "arguments", type = ExpressionNode[].class)
public abstract class EolnBuiltinNode extends ExpressionNode {

    @Specialization
    boolean isEoln(Object... arguments) {
        return (arguments.length == 0)? eoln() : eoln((TextFileValue) arguments[0]);
    }

    private boolean eoln() {
        return !PascalLanguage.INSTANCE.getInput().hasNext();
    }

    private boolean eoln(TextFileValue file) {
        return file.eoln();
    }

    @Override
    public TypeDescriptor getType() {
        return BooleanDescriptor.getInstance();
    }

}