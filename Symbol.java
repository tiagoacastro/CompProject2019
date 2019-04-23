public class Symbol {
    private String type;
    private String identifier;
    
    public Symbol(String type, String identifier){
        this.type = type;
        this.identifier = identifier;
    }

    public String getType() {return this.type;}

    public String getIdentifier() {return this.identifier;}
} 