/* Generated By:JJTree: Do not edit this line. ASTfunctionCall.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
import java.util.ArrayList;

public
class ASTfunctionCall extends SimpleNode {
    private String methodName;

    public ASTfunctionCall(int id) {
        super(id);
    }

    public ASTfunctionCall(JmmParser p, int id) {
        super(p, id);
    }

    public String getMethodName() {
        return methodName;
    }

    public void applySemanticAnalysis(SymbolTable table) {
        methodName = ((SimpleNode) children[0]).name + "(";
 
        if (children.length == 1) {
            methodName += ")";
 
            return;
        }
 
        Node[] parameters = ((SimpleNode) children[1]).children;
 
        for (int i = 0; i < parameters.length; i++) {
            methodName += getParameterType(((SimpleNode) parameters[i]), table);
        }
 
        methodName += ")";
 
        return;
    }

    public String getParameterType(SimpleNode parameter, SymbolTable table) {
        if (parameter.name != null) {
            Symbol s = table.getSymbol(parameter.name);
            if (s != null) {
                if (!s.isInitialized()) {
                    System.out.println("Variable " + parameter.name + " not initialized on line " + parameter.getLine());
                    System.exit(0);
                }

                return s.getType();
            }

            if (parameter instanceof ASTMULDIV || parameter instanceof ASTADDSUB) {
                parameter.applySemanticAnalysis(table);
                return "int";
            }

            try {
                Integer.parseInt(parameter.name);
                return "int";
            } catch(NumberFormatException | NullPointerException e) {
                System.out.println("Parameters don't match method definition on line " + this.getLine());
                System.exit(0);
            }
        }

        if (parameter instanceof ASTDOT) {
            parameter.applySemanticAnalysis(table);
            SimpleNode var = (SimpleNode) parameter.children[0];
            SimpleNode rhs = (SimpleNode) parameter.children[1];
            Symbol s = table.getSymbol(var.name);
            if (rhs instanceof ASTfunctionCall && (s != null || var instanceof ASTNEW)) {
                String fName = ((SimpleNode) rhs.children[0]).name + "(";
                if(rhs.children.length > 1){
                    Node[] parameters = ((SimpleNode) rhs.children[1]).children;
                    for (int i = 0; i < parameters.length; i++) {
                        fName += getParameterType(((SimpleNode) parameters[i]), table);
                    }
                }
                fName += ")";
                if (JmmParser.getInstance().containsMethod(fName)) {
                    return JmmParser.getInstance().getMethod(fName).getType();
                }

                System.out.println("Unknown method on line " + parameter.getLine());
                System.exit(0);
            }

            return "int";
        }

        if (parameter instanceof ASTTRUE || parameter instanceof ASTFALSE || parameter instanceof ASTCOMMERCIALE || parameter instanceof ASTMINOR) {
            parameter.applySemanticAnalysis(table);
            return "boolean";
        }

        if (parameter instanceof ASTarray) {
            parameter.applySemanticAnalysis(table);
            return "int";
        }

        if (parameter instanceof ASTNEW) {
            SimpleNode type = (SimpleNode) parameter.children[0];
            if (type instanceof ASTarray) return "int[]";
            return type.name;
        }

        System.out.println("Parameters don't match method definition on line " + this.getLine());
        System.exit(0);
        return "";
    }

    public void applySemanticAnalysisOnParameters(SymbolTable table) {
        Node[] parameters;

        if(children.length > 1)
            parameters = ((SimpleNode) children[1]).children;
        else
            return;
 
        for (int i = 0; i < parameters.length; i++) {
            if(parameters[i] instanceof ASTDOT)
                ((ASTDOT)parameters[i]).applySemanticAnalysis(table);
        }
 
        return;
    }
}
/* JavaCC - OriginalChecksum=5e1d15c247f9152e546f566a421852a0 (do not edit this line) */
