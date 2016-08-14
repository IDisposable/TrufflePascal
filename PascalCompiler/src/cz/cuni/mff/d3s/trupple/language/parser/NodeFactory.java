package cz.cuni.mff.d3s.trupple.language.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotKind;

import cz.cuni.mff.d3s.trupple.language.nodes.BlockNode;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.NopNode;
import cz.cuni.mff.d3s.trupple.language.nodes.PascalRootNode;
import cz.cuni.mff.d3s.trupple.language.nodes.StatementNode;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.AddNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.DivideIntegerNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.DivideNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.ModuloNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.MultiplyNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.NegationNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.arithmetic.SubstractNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.call.InvokeNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.control.BreakNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.CaseNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.ForNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.IfNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.RepeatNode;
import cz.cuni.mff.d3s.trupple.language.nodes.control.WhileNode;
import cz.cuni.mff.d3s.trupple.language.nodes.function.FunctionBodyNode;
import cz.cuni.mff.d3s.trupple.language.nodes.function.FunctionBodyNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.function.ProcedureBodyNode;
import cz.cuni.mff.d3s.trupple.language.nodes.function.ReadSubroutineArgumentNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.CharLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.DoubleLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.FunctionLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.LogicLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.LongLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.literals.StringLiteralNode;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.AndNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.EqualsNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.LessThanNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.LessThanOrEqualNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.NotNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.logic.OrNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AssignmentNode;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.AssignmentNodeGen;
import cz.cuni.mff.d3s.trupple.language.nodes.variables.ReadVariableNodeGen;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalContext;
import cz.cuni.mff.d3s.trupple.language.runtime.PascalFunctionRegistry;

public class NodeFactory {

	static class LexicalScope {
		protected final LexicalScope outer;
		protected final Map<String, FrameSlot> locals;
		protected final Map<String, Constant> constants;
		protected final String name;
		protected final PascalContext context;
		
		protected final Map<String, ICustomType> customTypes = new HashMap<>();
		protected final Map<String, String> variablesOfCustomType = new HashMap<>();

		public FrameDescriptor frameDescriptor;
		public List<StatementNode> scopeNodes = new ArrayList<>();
		public FrameSlot returnSlot = null;

		LexicalScope(LexicalScope outer, String name) {
			this.name = name;
			this.outer = outer;
			this.locals = new HashMap<>();
			this.constants = new HashMap<>();
			this.frameDescriptor = new FrameDescriptor();
			
			if (outer != null) {
				locals.putAll(outer.locals);
				this.context = new PascalContext(outer.context);
			}
			else{
				this.context = new PascalContext(null);
			}
		}
		
		public boolean containsCustomType(String typeName){
			return customTypes.containsKey(typeName);
		}
		
		public boolean containsCustomValue(String identifier){
			for(ICustomType custom : customTypes.values()){
				if(custom.containsCustomValue(identifier))
					return true;
			}
			
			return false;
		}
		
		public long getCustomValue(String identifier){
			for(ICustomType custom : customTypes.values()){
				if(custom.containsCustomValue(identifier))
					return custom.getCustomValue(identifier);
			}
			
			return 0;
		}
		
		public void addCustomTypeVariable(String identifier, String type){
			this.variablesOfCustomType.put(identifier, type);
		}
		
		public String registerEnumType(String identifier, List<String> identifiers, boolean global){
			if(customTypes.containsKey(identifier))
				return identifier;

			customTypes.put(identifier, new ICustomType.EnumType(identifier, identifiers, global));
			locals.put(identifier, frameDescriptor.addFrameSlot(identifier));
			
			for(String value : identifiers){
				if(locals.containsKey(value))
					return value;
				
				locals.put(value, null);
			}
			
			return null;
		}
	}

	interface Constant {

	}

	class IntegerConstant implements Constant {
	}

	/**
	 * WTF -> FrameDescriptor.copy() function does not copy slot kinds
	 * 
	 * @see com.oracle.truffle.api.frame.FrameDescriptor#copy
	 * @param original descriptor to be copied
	 * @return new descriptor with the same default value and slots
	 */
	public static FrameDescriptor copyFrameDescriptor(FrameDescriptor original) {
		FrameDescriptor clonedFrameDescriptor = new FrameDescriptor(original.getDefaultValue());
		for (FrameSlot slot : original.getSlots()) {
			clonedFrameDescriptor.addFrameSlot(slot.getIdentifier(), slot.getInfo(), slot.getKind());
		}
		return clonedFrameDescriptor;
	}

	// Reference to parser -> needed for throwing semantic errors
	private Parser parser;

	// private final Source source;

	/* State while parsing a block. */
	private LexicalScope lexicalScope;

	/* State while parsing case statement */
	// --> TODO: this causes to be enable to create nested cases....
	private List<ExpressionNode> caseExpressions;
	private List<StatementNode> caseStatements;
	private StatementNode caseElse;

	/* List of units found in sources given (name -> function registry) */
	private Map<String, Unit> units = new HashMap<>();
	private Unit currentUnit = null;

	public NodeFactory(Parser parser) {
		this.parser = parser;

		this.lexicalScope = new LexicalScope(null, null);
		this.lexicalScope.frameDescriptor = new FrameDescriptor();
	}

	private FrameSlotKind getSlotByTypeName(String type) {
		// firstly check if it is not a custom type
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		if(ls.containsCustomType(type))
			return FrameSlotKind.Object;
		
		switch (type) {

		// ordinals
		case "integer":
		case "cardinal":
		case "shortint":
		case "smallint":
		case "longint":
		case "int64":
		case "byte":
		case "word":
		case "longword":
			return FrameSlotKind.Long;

		// floating points
		case "single":
		case "real":
		case "double":
			return FrameSlotKind.Double;

		// logical
		case "boolean":
			return FrameSlotKind.Boolean;

		// char
		case "char":
			return FrameSlotKind.Byte;

		default:
			return FrameSlotKind.Illegal;
		}
	}

	public void finishVariableLineDefinition(List<String> identifiers, Token variableType) {
		FrameSlotKind slotKind = getSlotByTypeName(variableType.val.toLowerCase());

		if (slotKind == FrameSlotKind.Illegal) {
			parser.SemErr("Unkown variable type: " + variableType.val);
		}

		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		boolean isCustomType = slotKind == FrameSlotKind.Object;
		if(isCustomType)
			slotKind = FrameSlotKind.Long;
		
		for (String identifier : identifiers) {
			try {
				FrameSlot newSlot = ls.frameDescriptor.addFrameSlot(identifier, slotKind);
				ls.locals.put(identifier, newSlot);
				if(isCustomType){
					ls.addCustomTypeVariable(identifier, variableType.val.toLowerCase());
				}
			} catch (IllegalArgumentException e) {
				parser.SemErr("Duplicate variable: " + identifier + ".");
				continue;
			}
		}
	}
	
	public void registerEnumType(String identifier, List<String> identifiers){
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		boolean global = (currentUnit==null)? true : currentUnit.isInInterfaceSection();
		
		String duplicity = ls.registerEnumType(identifier, identifiers, global);
		if(duplicity != null)
			parser.SemErr("Duplicate variable: " + duplicity + ".");
	}

	public void startProcedure(Token name) {
		startSubroutine(name);
	}

	public void finishProcedure(StatementNode bodyNode) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		StatementNode subroutineNode = finishSubroutine(bodyNode);
		final ProcedureBodyNode functionBodyNode = new ProcedureBodyNode(subroutineNode);
		final PascalRootNode rootNode = new PascalRootNode(ls.frameDescriptor, functionBodyNode);

		if (currentUnit == null) {
			lexicalScope = lexicalScope.outer;
			lexicalScope.context.getGlobalFunctionRegistry().setFunctionRootNode(ls.name, rootNode);
		} else {
			currentUnit.registerProcedure(rootNode);
		}
	}

	public void startFunction(Token name) {
		startSubroutine(name);
	}

	public void setFunctionReturnValue(Token type) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		ls.returnSlot = ls.frameDescriptor.addFrameSlot(ls.name, getSlotByTypeName(type.val));
		ls.locals.put(ls.name, ls.returnSlot);
	}

	public void finishFunction(StatementNode bodyNode) {
		StatementNode subroutineNode = finishSubroutine(bodyNode);
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		final FunctionBodyNode functionBodyNode = FunctionBodyNodeGen.create(subroutineNode, ls.returnSlot);
		final PascalRootNode rootNode = new PascalRootNode(ls.frameDescriptor, functionBodyNode);

		if (currentUnit == null) {
			lexicalScope = lexicalScope.outer;
			lexicalScope.context.getGlobalFunctionRegistry().setFunctionRootNode(ls.name, rootNode);
		} else {
			currentUnit.registerFunction(rootNode);
		}
	}

	private void startSubroutine(Token name) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		if (ls.outer != null) {
			ls.context.getOutput().println("Nested subroutines are not supported.");
			return;
		}
		
		String identifier = name.val.toLowerCase();
		if(ls.context.containsIdentifier(identifier)){
			ls.context.getOutput().println("Duplicate identifier.");
			return;
		}

		if (currentUnit == null) {
			ls.context.getGlobalFunctionRegistry().registerFunctionName(identifier);
			lexicalScope = new LexicalScope(lexicalScope, identifier);
			lexicalScope.frameDescriptor = copyFrameDescriptor(lexicalScope.outer.frameDescriptor);
		} else {
			currentUnit.startSubroutineImplementation(identifier);
		}
	}

	private StatementNode finishSubroutine(StatementNode bodyNode) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		if (ls.outer == null) {
			ls.context.getOutput().println("Can't leave subroutine.");
			return null;
		}

		ls.scopeNodes.add(bodyNode);
		final StatementNode subroutineNode = new BlockNode(
				ls.scopeNodes.toArray(new StatementNode[ls.scopeNodes.size()]));
		
		return subroutineNode;
	}

	public void appendFormalParameter(List<VariableDeclaration> parameter, List<VariableDeclaration> params) {
		for (VariableDeclaration param : parameter) {
			params.add(param);
		}
	}

	public List<VariableDeclaration> createFormalParametersList(List<String> identifiers, String typeName) {
		List<VariableDeclaration> paramList = new ArrayList<>();
		for (String identifier : identifiers) {
			paramList.add(new VariableDeclaration(identifier, typeName));
		}

		return paramList;
	}

	public void addFormalParameters(List<VariableDeclaration> params) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		for (VariableDeclaration param : params) {
			FrameSlotKind slotKind = getSlotByTypeName(param.type);
			final ExpressionNode readNode = ReadSubroutineArgumentNodeGen.create(ls.scopeNodes.size(), slotKind);
			FrameSlot newSlot = ls.frameDescriptor.addFrameSlot(param.identifier, slotKind);
			final AssignmentNode assignment = AssignmentNodeGen.create(readNode, newSlot);
			ls.locals.put(param.identifier, newSlot);
			ls.scopeNodes.add(assignment);
		}
	}

	public void startMainFunction() {
	}

	public PascalRootNode finishMainFunction(StatementNode blockNode) {
		return new PascalRootNode(lexicalScope.frameDescriptor, new ProcedureBodyNode(blockNode));
	}

	public void startMainBlock() {
	}

	public StatementNode finishMainBlock(List<StatementNode> bodyNodes) {
		lexicalScope = lexicalScope.outer;
		return new BlockNode(bodyNodes.toArray(new StatementNode[bodyNodes.size()]));
	}

	public StatementNode finishBlock(List<StatementNode> bodyNodes) {
		return new BlockNode(bodyNodes.toArray(new StatementNode[bodyNodes.size()]));
	}

	public void startCaseList() {
		this.caseExpressions = new ArrayList<>();
		this.caseStatements = new ArrayList<>();
	}

	public void addCaseOption(ExpressionNode expression, StatementNode statement) {
		this.caseExpressions.add(expression);
		this.caseStatements.add(statement);
	}
	
	public void setCaseElse(StatementNode statement){
		this.caseElse = statement;
	}

	public CaseNode finishCaseStatement(ExpressionNode caseIndex) {
		CaseNode node = new CaseNode(caseIndex, caseExpressions.toArray(new ExpressionNode[caseExpressions.size()]),
				caseStatements.toArray(new StatementNode[caseStatements.size()]), caseElse);

		caseExpressions = null;
		caseStatements = null;
		caseElse = null;

		return node;
	}

	public ExpressionNode createFunctionNode(Token tokenName) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		String functionName = tokenName.val.toLowerCase();

		PascalContext context = ls.context;
		while(context != null){
			if(!context.containsIdentifier(functionName)){
				context = context.getOuterContext();
			} else {
				return new FunctionLiteralNode(context, functionName);
			}
		}
		parser.SemErr("Undefined function: " + functionName);
		return null;
	}

	public StatementNode createIfStatement(ExpressionNode condition, StatementNode thenNode, StatementNode elseNode) {
		return new IfNode(condition, thenNode, elseNode);
	}

	public StatementNode createWhileLoop(ExpressionNode condition, StatementNode loopBody) {
		return new WhileNode(condition, loopBody);
	}

	public StatementNode createRepeatLoop(ExpressionNode condition, StatementNode loopBody) {
		return new RepeatNode(condition, loopBody);
	}

	public StatementNode createForLoop(boolean ascending, Token variableToken, ExpressionNode startValue,
			ExpressionNode finalValue, StatementNode loopBody) {

		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();
		return new ForNode(ascending, ls.locals.get(variableToken.val.toLowerCase()), startValue, finalValue, loopBody);
	}

	public StatementNode createBreak() {
		return new BreakNode();
	}

	public ExpressionNode readSingleIdentifier(Token nameToken) {
		String identifier = nameToken.val.toLowerCase();
		
		// firstly try to read a variable
		FrameSlot frameSlot = getVisibleSlot(identifier);
		if (frameSlot != null){
			return ReadVariableNodeGen.create(frameSlot);
		} else {
			// secondly, try to return a custom value (enum or constant (constants are not currently supported although))
			LexicalScope ls = (currentUnit==null)? lexicalScope : currentUnit.getLexicalScope();
			
			if(ls.containsCustomValue(identifier)) {
				// TODO: for constants -> change this so it gets the type firtsly (it might not be long)
				return new LongLiteralNode(ls.getCustomValue(identifier));
			} else {
				// finally, try to create a procedure or function literal (with no arguments)
				if(ls.context.containsParameterlessSubroutine(identifier)) {
					ExpressionNode literal = this.createFunctionNode(nameToken);
					return this.createCall(literal, new ArrayList<>());
				}
				
				return null;
			}
		}
	}

	public ExpressionNode createCall(ExpressionNode functionLiteral, List<ExpressionNode> params) {
		return InvokeNodeGen.create(params.toArray(new ExpressionNode[params.size()]), functionLiteral);
	}

	public StatementNode createEmptyStatement() {
		return new NopNode();
	}

	public void createIntegerConstant(Token identifier, Token value) {
		LexicalScope ls = (currentUnit == null) ? lexicalScope : currentUnit.getLexicalScope();

		ls.locals.put(identifier.val.toLowerCase(), null);
		// ls.constants.put(identifier.val.toLowerCase(), new
		// IntegerConstant(value.val.toLowerCase()));
	}

	public void createFloatConstant(Token identifier, Token value) {

	}

	public void createStringOrCharConstant(Token identifier, Token value) {

	}

	public void createBooleanConstant(Token identifier, boolean value) {

	}

	public ExpressionNode createCharOrStringLiteral(Token literalToken) {
		String literal = literalToken.val;
		assert literal.length() >= 2 && literal.startsWith("'") && literal.endsWith("'");
		literal = literal.substring(1, literal.length() - 1);

		return (literal.length() == 1) ? new CharLiteralNode(literal.charAt(0)) : new StringLiteralNode(literal);
	}

	public ExpressionNode createNumericLiteral(Token literalToken) {
		try {
			return new LongLiteralNode(Long.parseLong(literalToken.val));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public ExpressionNode createFloatLiteral(Token token) {
		double value = Float.parseFloat(token.val.toString());
		return new DoubleLiteralNode(value);
	}

	public ExpressionNode createRealLiteral(Token integerPart, Token fractionalPart, Token exponentOp, Token exponent) {
		int integer = Integer.parseInt(integerPart.val);
		double fractional = (fractionalPart == null) ? 0 : Double.parseDouble(fractionalPart.val);
		int exponentMultiplier = (exponentOp != null && exponentOp.val == "-") ? -1 : 1;
		int exponentValue = (exponent == null) ? 0 : Integer.parseInt(exponent.val);

		double value = integer;

		while (fractional > 1)
			fractional /= 10;
		value += fractional;

		value = value * Math.pow(10, exponentValue * exponentMultiplier);

		return new DoubleLiteralNode(value);
	}

	public ExpressionNode createLogicLiteral(boolean value) {
		return new LogicLiteralNode(value);
	}

	public ExpressionNode createAssignment(Token nameToken, ExpressionNode valueNode) {
		FrameSlot slot = getVisibleSlot(nameToken.val.toLowerCase());
		if (slot == null)
			return null;

		return AssignmentNodeGen.create(valueNode, slot);
	}

	private FrameSlot getVisibleSlot(String identifier) {
		FrameSlot slot = null;
		if (currentUnit != null)
			slot = currentUnit.getSlot(identifier);
		else {
			LexicalScope ls = lexicalScope;
			while(ls != null && slot == null){
				slot = lexicalScope.frameDescriptor.findFrameSlot(identifier);
				ls = ls.outer;
			}
		}

		return slot;
	}

	public ExpressionNode createBinary(Token operator, ExpressionNode leftNode, ExpressionNode rightNode) {
		switch (operator.val.toLowerCase()) {

		// arithmetic
		case "+":
			return AddNodeGen.create(leftNode, rightNode);
		case "-":
			return SubstractNodeGen.create(leftNode, rightNode);
		case "*":
			return MultiplyNodeGen.create(leftNode, rightNode);
		case "/":
			return DivideNodeGen.create(leftNode, rightNode);
		case "div":
			return DivideIntegerNodeGen.create(leftNode, rightNode);
		case "mod":
			return ModuloNodeGen.create(leftNode, rightNode);

		// logic
		case "and":
			return AndNodeGen.create(leftNode, rightNode);
		case "or":
			return OrNodeGen.create(leftNode, rightNode);

		case "<":
			return LessThanNodeGen.create(leftNode, rightNode);
		case "<=":
			return LessThanOrEqualNodeGen.create(leftNode, rightNode);
		case ">":
			return NotNodeGen.create(LessThanOrEqualNodeGen.create(leftNode, rightNode));
		case ">=":
			return NotNodeGen.create(LessThanNodeGen.create(leftNode, rightNode));
		case "=":
			return EqualsNodeGen.create(leftNode, rightNode);
		case "<>":
			return NotNodeGen.create(EqualsNodeGen.create(leftNode, rightNode));

		default:
			parser.SemErr("Unexpected binary operator: " + operator.val);
			return null;
		}
	}

	public ExpressionNode createUnary(Token operator, ExpressionNode son) {
		switch (operator.val) {
		case "+":
			return son; // unary + in Pascal markss identity
		case "-":
			return NegationNodeGen.create(son);
		case "not":
			return NotNodeGen.create(son);
		default:
			parser.SemErr("Unexpected unary operator: " + operator.val);
			return null;
		}
	}
	
	public void importUnit(Token unitToken) {
		String importingUnit = unitToken.val.toLowerCase();

		if (!units.containsKey(importingUnit)) {
			parser.SemErr("Unknown unit. Did you imported it to compiler? - " + importingUnit);
			return;
		}
		
		Unit unit = units.get(importingUnit);
		// functions
		PascalFunctionRegistry fRegistry = unit.getContext().getGlobalFunctionRegistry();
		lexicalScope.context.getGlobalFunctionRegistry().addAll(fRegistry);
		
		// custom types
		for(String typeIdentifier : unit.getLexicalScope().customTypes.keySet()){
			ICustomType custom = unit.getLexicalScope().customTypes.get(typeIdentifier);
			if(custom.isGlobal()){
				lexicalScope.customTypes.put(typeIdentifier, custom);
			}
		}
	}

	/*****************************************************************************
	 * UNIT SECTION
	 *****************************************************************************/

	public void startUnit(Token t) {
		String unitName = t.val.toLowerCase();

		if (units.containsValue(t.val.toLowerCase())) {
			parser.SemErr("Unit with name " + unitName + " is already defined.");
			return;
		}

		currentUnit = new Unit(unitName);
		this.units.put(unitName, currentUnit);
	}

	public void endUnit() {
		currentUnit = null;
	}

	public void addProcedureInterface(Token name, List<VariableDeclaration> formalParameters) {
		if (!currentUnit.addProcedureInterface(name.val.toLowerCase(), formalParameters)) {
			parser.SemErr("Subroutine with this name is already defined: " + name);
		}
	}

	public void addFunctionInterface(Token name, List<VariableDeclaration> formalParameters, String returnType) {
		if (!currentUnit.addFunctionInterface(name.val.toLowerCase(), formalParameters, returnType)) {
			parser.SemErr("Subroutine with this name is already defined: " + name);
		}
	}
	
	public void finishFormalParameterListProcedure(Token name, List<VariableDeclaration> parameters) {
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		String identifier = name.val.toLowerCase();
		
		// the subroutine is in outer context because now the praser is in the subroutine's own context
		ls.outer.context.setMySubroutineParametersCount(identifier, parameters.size());
		
		if(currentUnit == null)
			return;
		
		if (!currentUnit.checkProcedureMatchInterface(identifier, parameters)) {
			parser.SemErr("Procedure heading for " + identifier + " does not match any procedure from the interface.");
		}
	}

	public void finishFormalParameterListFunction(Token name, List<VariableDeclaration> parameters, String returnType) {
		LexicalScope ls = (currentUnit == null)? lexicalScope : currentUnit.getLexicalScope();
		String identifier = name.val.toLowerCase();
		
		// the subroutine is in outer context because now the praser is in the subroutine's own context
		ls.outer.context.setMySubroutineParametersCount(identifier, parameters.size());
		
		if(currentUnit == null)
			return;
		
		if (!currentUnit.checkFunctionMatchInterface(identifier, parameters, returnType)) {
			parser.SemErr("Function heading for " + identifier + " does not match any function from the interface.");
		}
	}
	
	public void leaveUnitInterfaceSection(){
		assert currentUnit != null;
		
		currentUnit.leaveInterfaceSection();
	}
}
