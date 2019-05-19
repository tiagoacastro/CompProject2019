import java.util.LinkedHashMap;
import java.util.ArrayList;

/**
 * Symbol table class
 */
public class SymbolTable {
    private SymbolTable parent;
    private LinkedHashMap<String, Symbol> symbols;
    private String type;

    /**
     * SymbolTable default constructor
     */
    public SymbolTable() {
        parent = null;
        symbols = new LinkedHashMap<>();
    }

    /**
     * SymbolTable constructor
     * @param parent
     */
    public SymbolTable(SymbolTable parent, String type) {
        this.parent = parent;
        this.type = type;
        symbols = new LinkedHashMap<>();
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
     * Gets symbol table type
     * @return type table type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets symbol table type
     * @param type table type
     */
    public void setType(String type){
        this.type = type;
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
        if (symbols.containsKey(identifier))
            return symbols.get(identifier).getType();
        
        if (parent != null)
            return parent.getSymbol(identifier).getType();

        return null;
    }

    /**
     * Gets symbol access type
     * @param identifier symbol identifier
     * @return returns symbol access type, null if it isn't defined
     */
    public Symbol.Access getSymbolAccess(String identifier){
        if (symbols.containsKey(identifier))
            return symbols.get(identifier).getAccess();

        if (parent != null)
            return parent.getSymbol(identifier).getAccess();

        return null;
    }

    public ArrayList<Symbol> getParameters() {
        ArrayList<Symbol> parameters = new ArrayList<>();

        for (String id : symbols.keySet()) {
            if (symbols.get(id).getAccess() == Symbol.Access.parameter) {
                parameters.add(symbols.get(id));
            }
        }

        return parameters;
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