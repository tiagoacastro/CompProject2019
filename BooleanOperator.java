public class BooleanOperator extends SimpleNode {
    public BooleanOperator(int id) {
        super(id);
    }

    public BooleanOperator(JmmParser p, int id) {
        super(p, id);
    }

    public void applySemanticAnalysis(SymbolTable table) {
        SimpleNode lhs = ((SimpleNode) children[0]);
        SimpleNode rhs = ((SimpleNode) children[1]);

        checkSide(lhs, table);
        checkSide(rhs, table);
    }

    public void checkSide(SimpleNode side, SymbolTable table) { //methods?
        if (side.name != null) {
            Symbol s = table.getSymbol(side.name);
            if (s != null) {
                if (!s.isInitialized()) {
                    System.out.println("Variable " + side.name + " not initialized on line " + side.getLine());
                    System.exit(0);
                }
                if (!s.getType().equals("boolean")) {
                    System.out.println("Incompatible type: " + side.name + " not of type boolean on line " + side.getLine());
                    System.exit(0);
                }
            }
            return;
        } 

        if (side instanceof ASTTRUE || side instanceof ASTFALSE) return; 

        if (side instanceof ASTMINOR || side instanceof ASTCOMMERCIALE || side instanceof ASTEXCLAMATION) {
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
                String fName = ((SimpleNode) rhs.children[0]).name;
                if (JmmParser.getInstance().containsMethod(fName)) {
                    if (JmmParser.getInstance().getMethod(fName).getType().equals("boolean")) return;
                    System.out.println("Incompatible type: " + fName + " return type not of type boolean on line " + side.getLine());
                    System.exit(0);
                }
                System.out.println("Unknown method on line " + side.getLine());
                System.exit(0);
            }

            if (rhs instanceof ASTfunctionCall && var instanceof ASTNEW) {
                if (!JmmParser.getInstance().getClassTable().getType().equals(((SimpleNode) var.children[0]).name)) return;
                String fName = ((SimpleNode) rhs.children[0]).name;
                if (JmmParser.getInstance().containsMethod(fName)) {
                    if (JmmParser.getInstance().getMethod(fName).getType().equals("boolean")) return;
                    System.out.println("Incompatible type: " + fName + " return type not of type boolean on line " + side.getLine());
                    System.exit(0);
                }
                System.out.println("Unknown method on line " + side.getLine());
                System.exit(0);
            }
            return;
        }

        System.out.println("Found " + side.toString() + " and was expecting <, && or boolean value on line " + side.getLine());
        System.exit(0);
    }
}