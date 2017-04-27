package cz.cuni.mff.d3s.trupple.language.nodes.builtin.file;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import cz.cuni.mff.d3s.trupple.language.runtime.customvalues.FileValue;
import cz.cuni.mff.d3s.trupple.language.nodes.ExpressionNode;
import cz.cuni.mff.d3s.trupple.language.nodes.statement.StatementNode;

@NodeInfo(shortName = "reset")
@NodeChild(value = "file", type = ExpressionNode.class)
public abstract class ResetBuiltinNode extends StatementNode {

    @Specialization
    void reset(FileValue file) {
        file.openToRead();
    }

}
