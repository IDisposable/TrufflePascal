package cz.cuni.mff.d3s.trupple.language.nodes.logic;

import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.EnumValue;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.SetTypeValue;
import cz.cuni.mff.d3s.trupple.language.nodes.BinaryArgumentPrimitiveTypes;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.BinaryNode;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.TypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.compound.GenericEnumTypeDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.BooleanDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.CharDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.LongDescriptor;
import cz.cuni.mff.d3s.trupple.parser.identifierstable.types.primitive.RealDescriptor;

@NodeInfo(shortName = "<=")
public abstract class LessThanOrEqualNode extends BinaryNode {

    LessThanOrEqualNode() {
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), LongDescriptor.getInstance()), BooleanDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(RealDescriptor.getInstance(), LongDescriptor.getInstance()), BooleanDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(LongDescriptor.getInstance(), RealDescriptor.getInstance()), BooleanDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(RealDescriptor.getInstance(), RealDescriptor.getInstance()), BooleanDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(CharDescriptor.getInstance(), CharDescriptor.getInstance()), BooleanDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(BooleanDescriptor.getInstance(), BooleanDescriptor.getInstance()), BooleanDescriptor.getInstance());
        this.typeTable.put(new BinaryArgumentPrimitiveTypes(GenericEnumTypeDescriptor.getInstance(), GenericEnumTypeDescriptor.getInstance()), BooleanDescriptor.getInstance());
    }

	@Specialization
	boolean lessThanOrEqual(long left, long right) {
		return left <= right;
	}

	@Specialization
	boolean lessThan(double left, long right) {
		return left <= right;
	}

	@Specialization
	boolean lessThan(long left, double right) {
		return left <= right;
	}

	@Specialization
	boolean lessThan(double left, double right) {
		return left <= right;
	}

	@Specialization
	boolean lessThan(char left, char right) {
		return left <= right;
	}

	@Specialization
	boolean lessThan(boolean left, boolean right) {
		return !left && right;
	}

	@Specialization
	boolean lessThanOrEqual(SetTypeValue left, SetTypeValue right) {
		return (left.getSize() < right.getSize()) || (left.getSize() == right.getSize());
	}

	@Specialization
	boolean lessThan(EnumValue left, EnumValue right) {
		return left.lesserThan(right) || left.equals(right);
	}

    @Override
    public boolean verifyNonPrimitiveArgumentTypes(TypeDescriptor leftType, TypeDescriptor rightType) {
        return this.verifyBothCompatibleSetTypes(leftType, rightType);
    }

    @Override
    public TypeDescriptor getType() {
        return BooleanDescriptor.getInstance();
    }

}
