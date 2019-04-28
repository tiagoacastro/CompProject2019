import java.util.Hashtable;

/**
 * Symbol table class
 */
public class SymbolTable {
    private SymbolTable parent;
    private Hashtable<String, Symbol> symbols;

    /**
     * SymbolTable default constructor
     */
    public SymbolTable() {
        parent = null;
        symbols = new Hashtable<>();
    }

    /**
     * SymbolTable constructor
     * @param parent
     */
    public SymbolTable(SymbolTable parent) {
        this.parent = parent;
        symbols = new Hashtable<>();
    }

    /**
     * Gets symbol table parent
     * @return parent parent table
     */
    public SymbolTable getParent() {
        return parent;
    }

    /**
     * Sets symbol table parent
     * @param parent parent table
     */
    public void setParent(SymbolTable parent){
        this.parent = parent;
    }

    /**
     * adds symbol
     * @param symbol symbol
     * @return returns true if the symbol was added, false if it already exists
     */
    public void addSymbol(Symbol symbol){
        if (symbols.containsKey(symbol.getIdentifier())) {
            System.out.println("A symbol with that identifier already exists.");
            return;
        }
        symbols.put(symbol.getIdentifier(), symbol);
    }

    /**
     * checks if symbol was already defined
     * @param identifier symbol identifier
     * @return returns true if the symbol is already defined, false if not
     */
    public boolean symbolDefined(String identifier){
        return symbols.containsKey(identifier);
    }

    /**
     * Initializes identifier
     * @param identifier token identifier
     */
    public void initializeSymbol(String identifier) {
        symbols.get(identifier).initialize();
    }

    /**
     * Gets symbol
     * @param identifier symbol identifier
     * @return returns symbol, null if it isn't defined
     */
    public Symbol getSymbol(String identifier){
        if (symbols.containsKey(identifier))
            return symbols.get(identifier);

        if (parent != null)
            return parent.getSymbol(identifier);

        return null;
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

    @Override
    public String toString() {
        String res = "table:\n";
        for (String s : symbols.keySet()) {
            res = res.concat(symbols.get(s).toString() + "\n");
        }
        return res;
    }
}