package cz.cuni.mff.d3s.trupple.language.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;

import cz.cuni.mff.d3s.trupple.language.nodes.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.parser.NodeFactory.LexicalScope;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;

public class UnitInterface {
	public final String name;

	private LexicalScope lexicalScope;
	private final Map<String, List<VariableDeclaration>> interfaceProcedures;
	private final Map<String, FunctionFormalParameters> interfaceFunctions;

	public UnitInterface(String name) {
		this.name = name;
		this.interfaceProcedures = new HashMap<>();
		this.interfaceFunctions = new HashMap<>();
		this.lexicalScope = new LexicalScope(null, name);
	}
	
	public PascalContext getContext(){
		return lexicalScope.context;
	}

	public void addGlobalVariable(String identifier, FrameSlotKind slotKind) {
		FrameSlot newSlot = lexicalScope.frameDescriptor.addFrameSlot(identifier, slotKind);
		lexicalScope.locals.put(identifier, newSlot);
	}

	public boolean addProcedureInterface(String name, List<VariableDeclaration> parameters) {
		if (subroutineExists(name))
			return false;

		interfaceProcedures.put(name, parameters);
		return true;
	}

	public boolean addFunctionInterface(String name, List<VariableDeclaration> parameters, String returnType) {
		if (subroutineExists(name))
			return false;

		interfaceFunctions.put(name, new FunctionFormalParameters(parameters, returnType));
		return true;
	}

	private boolean subroutineExists(String name) {
		return interfaceProcedures.containsKey(name) || interfaceFunctions.containsKey(name);
	}

	public boolean subroutineImplementationExists(String name) {
		return lexicalScope.context.getGlobalFunctionRegistry().lookup(name) != null ||
				lexicalScope.context.getPrivateFunctionRegistry().lookup(name) != null;
	}

	public void startSubroutineImplementation(String identifier) {
		lexicalScope = new LexicalScope(lexicalScope, identifier);
		lexicalScope.frameDescriptor = NodeFactory.copyFrameDescriptor(lexicalScope.outer.frameDescriptor);
	}

	public boolean checkProcedureMatchInterface(String name, List<VariableDeclaration> params) {
		if (!interfaceProcedures.containsKey(name))
			return true;

		return compareFormalParametersList(interfaceProcedures.get(name), params);
	}

	public boolean checkFunctionMatchInterface(String name, List<VariableDeclaration> params, String returnType) {
		if (!interfaceFunctions.containsKey(name))
			return true;

		if (!compareFormalParametersList(interfaceFunctions.get(name).formalParameters, params))
			return false;

		return interfaceFunctions.get(name).typeName.equals(returnType);
	}

	private boolean compareFormalParametersList(List<VariableDeclaration> left, List<VariableDeclaration> right) {
		if (left.size() != right.size())
			return false;

		for (int i = 0; i < left.size(); i++) {
			if (!left.get(i).identifier.equals(right.get(i).identifier))
				return false;

			if (!left.get(i).type.equals(right.get(i).type))
				return false;
		}

		return true;
	}
	
	public void registerProcedure(String identifier, PascalRootNode rootNode){
		leaveLexicalScope();
		
		if(interfaceProcedures.containsKey(identifier))
			lexicalScope.context.getGlobalFunctionRegistry().register(identifier, rootNode);
		else
			lexicalScope.context.getPrivateFunctionRegistry().register(identifier, rootNode);
	}
	
	public void registerFunction(String identifier, PascalRootNode rootNode){
		leaveLexicalScope();
		
		if(interfaceProcedures.containsKey(identifier))
			lexicalScope.context.getGlobalFunctionRegistry().register(identifier, rootNode);
		else
			lexicalScope.context.getPrivateFunctionRegistry().register(identifier, rootNode);
	}

	private void leaveLexicalScope() {
		this.lexicalScope = lexicalScope.outer;
	}

	public FrameSlot getSlot(String identifier) {
		return lexicalScope.frameDescriptor.findFrameSlot(identifier);
	}

	public LexicalScope getLexicalScope() {
		return lexicalScope;
	}
}

class FunctionFormalParameters {
	public FunctionFormalParameters(List<VariableDeclaration> formalParameters, String typeName) {
		this.formalParameters = formalParameters;
		this.typeName = typeName;
	}

	public List<VariableDeclaration> formalParameters;
	public String typeName;
}
