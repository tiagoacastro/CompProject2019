/**
 * Symbol class
 */
public class Symbol { 
    private String type; 
    private String identifier;
    private Access access; 
    private boolean isInitialized = false;

    /**
     * Access enum
     */
    public static enum Access {local, parameter, global} 

    /**
     * Symbol constructor
     * @param type symbol type
     * @param identifier symbol identifier
     * @param access symbol access type
     */
    public Symbol(String type, Access access) { 
        this.type = type;
        this.access = access;
    } 

    /**
     * Symbol constructor
     * @param type symbol type
     * @param identifier symbol identifier
     * @param access symbol access type
     */
    public Symbol(String type, String identifier, Access access) { 
        this.type = type; 
        this.identifier = identifier; 
        this.access = access;
    } 

    /**
     * Symbol constructor
     * @param access symbol access type
     */
    public Symbol(Access access) {
        this.access = access;
    } 
    
    /**
     * Setter for type
     * @param type
     */
    public void setType(String type) {this.type = type;} 
    
    /**
     * Getter for type
     * @return type
     */
    public String getType() {return this.type;} 
    
    /**
     * Setter for identifier
     * @param identifier
     */
    public void setIdentifier(String identifier) {this.identifier = identifier;} 
    
    /**s
     * Getter for identifier
     * @return identifier
     */
    public String getIdentifier() {return this.identifier;} 
    
    /**
     * Setter for access
     * @param access
     */
    public void setAccess(Access access) {this.access = access;} 

    /**
     * Getter for access type
     * @return access type
     */
    public Access getAccess() {return this.access;} 

    /**
     * Getter for isInitialized
     * @return boolean isInitialized
     */
    public boolean isInitialized() {return isInitialized;} 

    /**
     * Marks symbol as initialized.
     */
    public void initialize() {this.isInitialized = true;} 

    @Override
    public String toString() {
        return type + " " + identifier + " " + access;
    }
}  