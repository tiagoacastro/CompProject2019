import java.util.HashMap;

public class SymbolTable {
    private SymbolTable parent;
    private HashMap<String, String> symbols; //Identifier -> Type

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
    public boolean addSymbol(String type, String identifier){
        if(symbolAlreadyDefined(type, identifier))
            return false;
        else {
            symbols.put(identifier, type);
            return true;
        }
    }

    /**
     * checks if symbol was already defined
     * @param type symbol type
     * @param identifier symbol identifier
     * @return returns true if the symbol is already defined, false if not
     */
    private boolean symbolAlreadyDefined(String type, String identifier){
        String realType = symbols.get(identifier);
        
        if(realType == null) {
            if(parent == null){
                return false;
            } else
                return parent.symbolAlreadyDefined(type, identifier);
        } else 
            return true;
    }

    /**
     * Gets identifier type
     * @param identifier symbol identifier
     * @return returns symbol type, null if it isn't defined
     */
    public String getIdentifierType(String identifier){
        return symbols.get(identifier);
    }
}