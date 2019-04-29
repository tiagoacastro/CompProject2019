import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CodeGenerator {
    private SimpleNode root;
    private PrintWriter out;
    private StringBuilder builder;
    private SimpleNode node;

    public CodeGenerator(SimpleNode root) {
        this.root = root.getChild(0);
        this.builder = new StringBuilder();
        this.node = this.root.next();
        String filename = this.node.getName() + ".j";

		try {
            FileWriter fileWriter = new FileWriter(filename, false);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            this.out = new PrintWriter(bufferedWriter);
		} catch (IOException e) {
			e.printStackTrace();
        }
    }

    public void generate() {
        generateHeader();
        nl();

        if(generateGlobals())
            nl();

        generateMethods();

        // outputs the final string builded to the file
        out.println(this.builder);
        out.close();
    }

    private void generateHeader() {
        write(".class public ");
        write(this.node.getName());
        nl();

        SimpleNode extend = this.root.next();

        if(extend.getName().equals("extends")){
            write(".super ");
            write(extend.next().getName());
        } else {
            this.root.previous();
            write(".super java/lan/Object");
        }

        nl();
    }

    private boolean generateGlobals() {
        SimpleNode global;
        boolean has = false;

        while((global = this.root.next()) != null && global.getName().equals("varDeclaration")) {
            generateGlobalDeclaration(global);
            has = true;
        }  
        
        this.root.previous();
        
        return has;
    }

    private void generateGlobalDeclaration(SimpleNode var) {
        write(".field public ");

        write(var.next(2).getName());

        space();
        write(getType(var.previous().getName()));

        nl();
    }

    private void generateMethods(){
        generateConstructor();

        SimpleNode method;

        while((method = this.root.next()) != null) {
            nl();
            method = method.next();
            generateFunctionHeader(method);
		    generateFunctionBody(method);
            generateFunctionFooter(method);
        }
    }

    private void generateConstructor(){
        write(".method <init>()V");
        nl();
        write(".limit stack 1");
        nl();
        write(".limit locals 1");
        nl();
        write(".var0 is this L");
        write(this.node.getName());
        write("; from Label0 to Label1");
        nl();
        write("Label0:");
        nl();
        tab();
        write("aload_0");
        nl();
        tab();
        write("invokespecial java/lang/Object/<init>()V");
        nl();
        write("Label1:");
        nl();
        tab();
        write("return");
        nl();
        write(".end method");
        nl();
    }

    private void generateFunctionHeader(SimpleNode func){
        if(func.getName().equals("mainDeclaration"))
            generateMainHeader(func);
        else
            generateMethodHeader(func);
        nl();

        //TODO limits
        write(".limit stack 10");
        nl();

        write(".limit locals 10");
        nl();
    }

    private void generateMainHeader(SimpleNode func){
        write(".method public static main([Ljava/lang/String;)V");
        func.next(2);
    }

    private void generateMethodHeader(SimpleNode func){
        write(".method public ");

        write(getType(func.next(2).getName()));

        SimpleNode args = func.next();
        write("(");
        if(args.getName().equals("parameterDeclaration")){
            SimpleNode arg;
            while((arg = args.next()) != null){
                write(getType(arg.getName()));
                args.next();
            }
        }
        func.previous();
        write(")");

        write(getType(func.previous().getName()));
    }

    private void generateFunctionBody(SimpleNode func){
        
        SimpleNode methodBody = null;
        for(int i = 0; i < func.jjtGetNumChildren(); i++){
            if(func.getChild(i).getName().equals("methodBody")) {
                methodBody = func.getChild(i);
            }
        }

        for(int j = 0; j < methodBody.jjtGetNumChildren(); j++) {
            switch(methodBody.getChild(j).getName()){
                case "varDeclaration":
                    break;
                case "=":
                    generateEqualSign(methodBody.getChild(j));
                    break;
                default:
                    break;
            
            }
        }
    }

    private void generateEqualSign(SimpleNode equalsNode) {
        SimpleNode left = equalsNode.getChild(0);
        storeLocalVariable(left);
        generateRight(equalsNode);
    }

    private void generateRight(SimpleNode equalsNode){
        
    }

    private void storeLocalVariable(SimpleNode leftNode) {
        //need acess to symbol table
    }

    private void generateFunctionFooter(SimpleNode func){
        tab();
        write(getType2(func.same().getName()));
		write("return");
        nl();
        
		write(".end method");
        nl();
    }

    private String getType(String type){
        switch(type){
            case "int":
                return "I";
            case "boolean":
                return "Z";
        }

        return "ND";
    }

    private String getType2(String type){
        switch(type){
            case "int":
                return "i";
            case "boolean":
                return "z";
            case "void":
                return "";
        }

        return "nd";
    }

    private void nl(){
        this.builder.append("\n");
    }

    private void tab(){
        this.builder.append("\t");
    }

    private void space(){
        this.builder.append(" ");
    }

    private void write(String content){
        this.builder.append(content);
    }
}
