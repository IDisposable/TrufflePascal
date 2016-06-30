package pascal.language.nodes.control;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameSlotTypeException;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.UnexpectedResultException;

import pascal.language.nodes.ExpressionNode;
import pascal.language.nodes.StatementNode;
import pascal.language.nodes.call.InvokeNodeGen;
import pascal.language.nodes.literals.FunctionLiteralNode;
import pascal.language.nodes.variables.AssignmentNode;
import pascal.language.nodes.variables.AssignmentNodeGen;

@NodeInfo(shortName = "for", description = "The node implementing a for loop")
public class ForNode extends StatementNode {

	private final boolean ascending;
	private FrameSlot slot;
	@Child ExpressionNode startValue;
	@Child ExpressionNode finalValue;
	@Child AssignmentNode assignment;
	@Child StatementNode body;
	
	public ForNode(boolean ascending, FrameSlot slot, ExpressionNode startValue, ExpressionNode finalValue,
			StatementNode body){
		
		this.ascending = ascending;
		this.slot = slot;
		this.startValue = startValue;
		this.finalValue = finalValue;
		this.assignment = AssignmentNodeGen.create(startValue, slot);
		this.body = body;
	}
	
	@Override
	public void executeVoid(VirtualFrame frame) {
		try {
			assignment.executeVoid(frame);
			if(ascending){
				while(frame.getLong(slot) <= finalValue.executeLong(frame)){
					body.executeVoid(frame);
					frame.setLong(slot, frame.getLong(slot) + 1);
				}
			}
			else{
				while(frame.getLong(slot) >= finalValue.executeLong(frame)){
					body.executeVoid(frame);
					frame.setLong(slot, frame.getLong(slot) - 1);
				}
			}
		} catch (UnexpectedResultException | FrameSlotTypeException e) {
			// TODO HANDLE THIS ERROR
		}
	}
}