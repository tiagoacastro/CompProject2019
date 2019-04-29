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

        System.out.println("Found " + side.toString() + " and was expecting <, && or boolean value on line " + side.getLine());
        System.exit(0);
    }
}