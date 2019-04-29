/* Generated By:JJTree: Do not edit this line. ASTparameterDeclaration.java Version 6.0 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class ASTparameterDeclaration extends SimpleNode {
    public ASTparameterDeclaration(int id) {
        super(id);
    }

    public ASTparameterDeclaration(JmmParser p, int id) {
        super(p, id);
    }

    public void createSymbolTable(SymbolTable table) {
        String type;

        ASTtype typeNode = (ASTtype) children[0];
        type = typeNode.getType();

        Symbol symbol = new Symbol(type, ((SimpleNode)children[1]).name, Symbol.Access.parameter);
        symbol.initialize();
        table.addSymbol(symbol);
    }
}
/* JavaCC - OriginalChecksum=ec3eb58a405b8508a5c9b3f8b493977e (do not edit this line) */