public class BinaryOperator extends SimpleNode {
    public BinaryOperator(int id) {
        super(id);
    }

    public BinaryOperator(JmmParser p, int id) {
        super(p, id);
    }

    public void applySemanticAnalysis(SymbolTable table) {
        SimpleNode lhs = ((SimpleNode) children[0]);
        SimpleNode rhs = ((SimpleNode) children[1]);

        checkSide(lhs, table);
        checkSide(rhs, table);
    }

    public void checkSide(SimpleNode side, SymbolTable table) {
        if (side.name != null) {
            if (side instanceof ASTADDSUB || side instanceof ASTMULDIV) {
                side.applySemanticAnalysis(table);
                return;
            }

            Symbol s = table.getSymbol(side.name);
            if (s != null) {
                if (!s.isInitialized()) {
                    System.out.println("Variable " + side.name + " not initialized on line " + side.getLine());
                    System.exit(0);
                }
                if (!s.getType().equals("int")) {
                    System.out.println("Incompatible type: " + side.name + " not of type int on line " + side.getLine());
                    System.exit(0);
                }
                return;
            }

            try { 
                Integer.parseInt(side.name); 
            } catch(NumberFormatException | NullPointerException e) { 
                System.out.println("Incompatible type: " + side.name + " not of type int on line " + side.getLine());
                System.exit(0);
            }

            return;
        }

        if (side instanceof ASTarray) {
            side.applySemanticAnalysis(table);
            return;
        }

        if (side instanceof ASTDOT) {
            side.applySemanticAnalysis(table);
            SimpleNode var = (SimpleNode) side.children[0];
            SimpleNode rhs = (SimpleNode) side.children[1];

            Symbol s = table.getSymbol(var.name);
            if (rhs instanceof ASTfunctionCall && s != null) {
                if (!s.getType().equals(JmmParser.getInstance().getClassTable().getType())) return;
                String fName = ((ASTfunctionCall) rhs).getMethodName();
                if (JmmParser.getInstance().containsMethod(fName)) {
                    if (JmmParser.getInstance().getMethod(fName).getType().equals("int")) return;
                    System.out.println("Incompatible type: " + fName + " return type not of type int on line " + side.getLine());
                    System.exit(0);
                }
                System.out.println("Unknown method on line " + side.getLine());
                System.exit(0);
            }

            if (rhs instanceof ASTfunctionCall && var instanceof ASTNEW) {
                if (!JmmParser.getInstance().getClassTable().getType().equals(((SimpleNode) var.children[0]).name)) return;
                String fName = ((ASTfunctionCall) rhs).getMethodName();
                if (JmmParser.getInstance().containsMethod(fName)) {
                    if (JmmParser.getInstance().getMethod(fName).getType().equals("int")) return;
                    System.out.println("Incompatible type: " + fName + " return type not of type int on line " + side.getLine());
                    System.exit(0);
                }
                System.out.println("Unknown method on line " + side.getLine());
                System.exit(0);
            }
            return;
        }

        System.out.println("Found " + side.toString() + " and was expecting *, /, +, -, or integer value on line " + side.getLine());
        System.exit(0);
    }
}