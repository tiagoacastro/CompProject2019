import java.util.HashMap;

public class SymbolTable {
    private SymbolTable parent;
    private HashMap<String, String> symbols; //Identifier -> Type

    public void setParent(SymbolTable parent){
        this.parent = parent;
    }

    public void addSymbol(String type, String identifier){
        symbols.put(identifier, type);
    }
}