import java.util.HashMap;

/**
 * Symbol table class
 */
public class SymbolTable {
    private SymbolTable parent;
    private HashMap<String, Symbol> symbols; //Identifier -> Type

    /**
     * Sets symbol table parent
     * @param parent parent table
     */
    public void setParent(SymbolTable parent){
        this.parent = parent;
    }

    /**
     * adds symbol
     * @param type symbol type
     * @param identifier symbol identifier
     * @return returns true if the symbol was added, false if it already exists
     */
    public boolean addSymbol(String type, String identifier, Symbol.Access access){
        if(symbolDefined(identifier))
            return false;
        else {
            symbols.put(identifier, new Symbol(type, identifier, access));
            return true;
        }
    }

    /**
     * checks if symbol was already defined
     * @param identifier symbol identifier
     * @return returns true if the symbol is already defined, false if not
     */
    public boolean symbolDefined(String identifier){
        Symbol symbol = symbols.get(identifier);

        if(symbol == null) {
            if(this.parent == null){
                return false;
            } else
                return this.parent.symbolDefined(identifier);
        } else 
            return true;
    }

    /**
     * Gets symbol
     * @param identifier symbol identifier
     * @return returns symbol, null if it isn't defined
     */
    public Symbol getSymbol(String identifier){
        return symbols.get(identifier);
    }

    /**
     * Gets symbol type
     * @param identifier symbol identifier
     * @return returns symbol type, null if it isn't defined
     */
    public String getSymbolType(String identifier){
        return symbols.get(identifier).getType();
    }

    /**
     * Gets symbol access type
     * @param identifier symbol identifier
     * @return returns symbol access type, null if it isn't defined
     */
    public Symbol.Access getSymbolAccess(String identifier){
        return symbols.get(identifier).getAccess();
    }
}