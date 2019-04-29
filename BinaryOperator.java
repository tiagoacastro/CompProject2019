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
        //int int[expr] identifiers + - * / methods(?)
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

        System.out.println("Found " + side.toString() + " and was expecting *, /, +, -, or integer value on line " + side.getLine());
        System.exit(0);
    }
}