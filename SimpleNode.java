/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 6.0 */
/* JavaCCOptions:MULTI=false,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */

public class SimpleNode implements Node {

    protected Node parent;
    protected Node[] children;
    protected int id;
    protected Object value;
    protected JmmParser parser;
    protected int index = -1;
    protected String name = null;
    protected int idx = -1;
    protected String print;
    protected int line;
    public static String extend = null;

    public SimpleNode(int i) {
        id = i;
    }

    public SimpleNode(JmmParser p, int i) {
        this(i);
        parser = p;
    }

    public void jjtOpen() {
    }

    public void jjtClose() {
    }

    public void jjtSetParent(Node n) {
        parent = n;
    }

    public Node jjtGetParent() {
        return parent;
    }

    public void jjtAddChild(Node n, int i) {
        if (children == null) {
            children = new Node[i + 1];
        } else if (i >= children.length) {
            Node c[] = new Node[i + 1];
            System.arraycopy(children, 0, c, 0, children.length);
            children = c;
        }
        children[i] = n;
    }

    public Node jjtGetChild(int i) {
        return children[i];
    }

    public SimpleNode getChild(int i) {
        return (SimpleNode) children[i];
    }

    public int jjtGetNumChildren() {
        return (children == null) ? 0 : children.length;
    }

    public void jjtSetValue(Object value) {
        this.value = value;
    }

    public Object jjtGetValue() {
        return value;
    }

    public String getName() {
        return this.print;
    }

    public void reset() {         
        this.idx = -1;
    }

    public SimpleNode same() {         
        if(children == null)
            return null;

        return (SimpleNode) this.children[this.idx];
    }

    public SimpleNode next(int times) {
        SimpleNode n = null;

        if(children == null)
            return null;

        for(int i=0; i<times; i++)
            n = next();

        return n;
    }

    public SimpleNode next() {
        SimpleNode n = null;

        if(children == null)
            return null;

        while(n == null || n.getName() == null || n.getName() == "") {
            this.idx++;
            if(this.idx == this.children.length)
                return null;
            n = (SimpleNode) this.children[this.idx];
        }

        return n;
    }

    public SimpleNode previous(int times) {
        SimpleNode n = null;

        if(children == null)
            return null;

        for(int i=0; i<times; i++)
            n = previous();

        return n;
    }
    
    public SimpleNode previous() {       
        SimpleNode n = null;

        if(children == null)
            return null;

        while(n == null || n.getName() == null || n.getName() == "") {
            this.idx--;
            if(this.idx == -1){
                return null;
            }
            n = (SimpleNode) this.children[this.idx];
        }
    
        return n;
    }

    /*
     * You can override these two methods in subclasses of SimpleNode to customize
     * the way the node appears when the tree is dumped. If your output uses more
     * than one line you should override toString(String), otherwise overriding
     * toString() is probably all you need to do.
     */

    public String toString() {
        if (index == -1)
            this.print = JmmParserTreeConstants.jjtNodeName[id];
        else
            this.print = JmmParserConstants.tokenImage[index].replaceAll("\"", "");

        return this.print;
    }

    public String toString(String prefix) {
        return prefix + toString();
    }

    /*
     * Override this method if you want to customize how the node dumps out its
     * children.
     */

    public void dump(String prefix) {
        System.out.println(toString(prefix));

        if (children != null) {
            for (int i = 0; i < children.length; ++i) {
                SimpleNode n = (SimpleNode) children[i];
                if(n != null)
                    n.dump(prefix + "   ");
            }
        }
    }

    public int getId() {
        return id;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void createSymbolTable(SymbolTable table) {
        if (children == null) return;

        for (int i = 0; i < children.length; i++) {
            ((SimpleNode) children[i]).createSymbolTable(table);
        }
    }

    public void applySemanticAnalysis(SymbolTable table) {
        if (children == null) return;

        if(print.equals("extends")){
            extend = ((SimpleNode)children[0]).getName();
        }

        for (int i = 0; i < children.length; i++) {
            ((SimpleNode) children[i]).applySemanticAnalysis(table);
        }
    }
}

/* JavaCC - OriginalChecksum=2b7ec5aa5689d0e7377adde4da00c1d2 (do not edit this line) */
