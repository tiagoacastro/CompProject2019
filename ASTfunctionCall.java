/* Generated By:JJTree: Do not edit this line. ASTfunctionCall.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
import java.util.ArrayList;

public
class ASTfunctionCall extends SimpleNode {
    public ASTfunctionCall(int id) {
        super(id);
    }

    public ASTfunctionCall(JmmParser p, int id) {
        super(p, id);
    }

    public void applySemanticAnalysis(SymbolTable table) {
        String methodName = ((SimpleNode) children[0]).name;
        if (!JmmParser.getInstance().containsMethod(methodName)) {
            System.out.println("Not a valid method on line " + this.getLine());
            System.exit(0);
        }

        ArrayList<Symbol> parameterSymbols = JmmParser.getInstance().getMethod(methodName).getParameters();

        if (children.length == 1) {
            if (parameterSymbols.size() == 0) 
                return;
            else {
                System.out.println("Parameters don't match method definition on line " + this.getLine());
                System.exit(0);
            }
        }

        Node[] parameters = ((SimpleNode) children[1]).children;

        if (parameterSymbols.size() != parameters.length) {
            System.out.println("Parameters don't match method definition on line " + this.getLine());
            System.exit(0);
        }

        for (int i = 0; i < parameters.length; i++) {
            if (!checkParameter((SimpleNode) parameters[i], parameterSymbols.get(i), table)) {
                System.out.println("Parameters don't match method definition on line " + this.getLine());
                System.exit(0);
            }
        }
    }

    public boolean checkParameter(SimpleNode parameter, Symbol parameterSymbol, SymbolTable table) {
        if (parameter.name != null) {
            Symbol s = table.getSymbol(parameter.name);
            if (s != null) {
                if (!s.isInitialized()) {
                    System.out.println("Variable " + parameter.name + " not initialized on line " + parameter.getLine());
                    System.exit(0);
                }
                if (s.getType().equals(parameterSymbol.getType())) return true;
                
                return false;
            }

            try { 
                Integer.parseInt(parameter.name);
                if (parameterSymbol.getType().equals("int")) return true;
                else return false;
            } catch(NumberFormatException | NullPointerException e) { 
                return false;
            }
        }

        if ((parameter instanceof ASTTRUE || parameter instanceof ASTFALSE) && parameterSymbol.getType().equals("boolean")) return true; 

        if (parameter instanceof ASTarray && parameterSymbol.getType().equals("int")) {
            parameter.applySemanticAnalysis(table);
            return true;
        }

        if (parameter instanceof ASTNEW) {
            SimpleNode type = (SimpleNode) parameter.children[0];
            if (type instanceof ASTarray && parameterSymbol.getType().equals("int[]")) return true;
            if (parameterSymbol.getType().equals(type.name)) return true;
            return false;
        }

        return false;
    }
}
/* JavaCC - OriginalChecksum=5e1d15c247f9152e546f566a421852a0 (do not edit this line) */
