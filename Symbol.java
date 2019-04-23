/**
 * Symbol class
 */
public class Symbol { 
    private String type; 
    private String identifier;
    private Access access; 

    /**
     * Access enum
     */
    public enum Access {local, parameter, global} 

    /**
     * Symbol constructor
     * @param type symbol type
     * @param identifier symbol identifier
     * @param access symbol access type
     */
    public Symbol(String type, String identifier, Access access){ 
        this.type = type; 
        this.identifier = identifier; 
        this.access = access;
    } 
    
    /**
     * Getter for type
     * @return type
     */
    public String getType() {return this.type;} 
    
    /**s
     * Getter for identifier
     * @return identifier
     */
    public String getIdentifier() {return this.identifier;} 

    /**
     * Getter for access type
     * @return access type
     */
    public Access getAccess() {return this.access;} 
}  