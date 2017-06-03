package cz.cuni.mff.d3s.trupple.language.nodes.variables.read;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.NodeField;
import com.oracle.truffle.api.dsl.Specialization;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.ReadIndexNode;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PCharValue;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.PascalString;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;

@NodeChildren({
        @NodeChild(value = "valueNode", type = ExpressionNode.class),
        @NodeChild(value = "indexNode", type = ReadIndexNode.class),
})
@NodeField(name = "returnType", type = TypeDescriptor.class)
public abstract class ReadFromArrayNode extends ExpressionNode {

    protected abstract TypeDescriptor getReturnType();

    @Specialization
    int readInt(int[] array, int index) {
        return array[index];
    }

    @Specialization
    long readLong(long[] array, int index) {
        return array[index];
    }

    @Specialization
    double readDouble(double[] array, int index) {
        return array[index];
    }

    @Specialization
    char readChar(char[] array, int index) {
        return array[index];
    }

    @Specialization
    boolean readBoolean(boolean[] array, int index) {
        return array[index];
    }

    // TODO: do we need this PascalString class?
    @Specialization
    char readString(PascalString string, int index) {
        return (char) string.getValueAt(index);
    }

    @Specialization
    char readPChar(PCharValue string, int index) {
        return (char) string.getValueAt(index);
    }

    @Specialization
    Object readGeneric(Object[] array, int index) {
        return array[index];
    }

    @Override
    public TypeDescriptor getType() {
        return this.getReturnType();
    }

}