package cz.cuni.mff.d3s.trupple.language.customtypes;

import cz.cuni.mff.d3s.trupple.language.customvalues.EnumValue;

public class EnumOrdinal implements IOrdinalType {

	private final EnumType sourceType;
	
	public EnumOrdinal(EnumType source) {
		this.sourceType = source;
	}
	
	@Override
	public int getFirstIndex() {
		return sourceType.getFirstIndex();
	}

	@Override
	public int getSize() {
		return sourceType.getSize();
	}

	@Override
	public Type getType() {
		return IOrdinalType.Type.ENUM;
	}

	@Override
	public int getRealIndex(Object index) {
		return ((EnumValue) index).getIndex();
	}
}