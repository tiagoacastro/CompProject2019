import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class CodeGenerator {
    private SimpleNode root;
    private PrintWriter out;
    private StringBuilder builder;
    private String store;
    private String classe;
    private String[] locals = new String[10]; //hardcoded
    private int localNum = 0;
    private String method;

    public CodeGenerator(SimpleNode root) {
        this.root = root.getChild(0);
        this.builder = new StringBuilder();
        this.classe = this.root.next().getName();
        String filename = this.classe + ".j";

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
        write(this.classe);
        nl();

        SimpleNode extend = this.root.next();

        if(extend.getName().equals("extends")){
            write(".super ");
            write(extend.next().getName());
        } else {
            this.root.previous();
            write(".super java/lang/Object");
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
        write(".method public <init>()V");
        nl();
        write(".limit stack 1");
        nl();
        write(".limit locals 1");
        nl();
        write(".var 0 is this L");
        write(this.classe);
        write(";");
        nl();
        tab();
        write("aload_0");
        nl();
        tab();
        write("invokespecial java/lang/Object/<init>()V");
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

        //hardcoded
        write(".limit stack 10");
        nl();

        write(".limit locals 10");
        nl();

        if(func.getName().equals("mainDeclaration")) {
            write(".var 0 is args [Ljava/lang/String;");
        }else{
            write(".var 0 is this L");
            write(this.classe);
            write(";");
        }
        nl();
    }

    private void generateMainHeader(SimpleNode func){
        write(".method public static main([Ljava/lang/String;)V");
        this.method = "main";
        func.next(3);
    }

    private void generateMethodHeader(SimpleNode func){
        write(".method public ");


        this.method = func.next(2).getName();
        write(this.method);

        SimpleNode args = func.next();
        write("(");
        if(args.getName().equals("parameterDeclaration")){
            SimpleNode arg;
            while((arg = args.next()) != null){
                write(getType(arg.next().getName()));
                args.next();
            }
        }
        func.previous();
        write(")");

        this.store = func.previous().next().getName();
        write(getType(this.store));
    }

    private void generateFunctionBody(SimpleNode func){
        SimpleNode body = null;
        do {
            body = func.next();
        } while(!body.getName().equals("methodBody"));

        Arrays.fill(this.locals, null);
        this.localNum = 0;

        if(body.children != null){
            SimpleNode node;
            while((node = body.next()) != null && node.getName().equals("varDeclaration")) {
                write(".var ");
                write(""+(localNum+1));
                write(" is ");
                write(node.next(2).getName());
                node.previous(2);
                space();
                write(getType(node.next().next().getName()));
                write("");
                nl();
                locals[localNum] = node.next().getName();
                localNum++;
            }
            body.previous();
            while((node = body.next()) != null) {
                handle(node);
            }
        }
    }

    private void handle(SimpleNode node){
        String name = node.getName();
        if(isNumeric(name)) {
            tab();
            write("iconst_");
            write(name);
            nl();
        } else {
            switch(node.getName()){
                case "=":
                    String idx = find(node.next());
                    handle(node.next());
                    tab();
                    write("istore_");
                    write(idx);
                    nl();
                    break;
                case "+":
                    handle(node.next());
                    handle(node.next());
                    tab();
                    write("iadd");
                    nl();
                    break;
                case "-":
                    handle(node.next());
                    handle(node.next());
                    tab();
                    write("isub");
                    nl();
                    break;
                case "*":
                    handle(node.next());
                    handle(node.next());
                    tab();
                    write("imul");
                    nl();
                    break;
                case "/":
                    handle(node.next());
                    handle(node.next());
                    tab();
                    write("idiv");
                    nl();
                    break;
                default:
                    tab();
                    write(getType2(JmmParser.getInstance().getMethod(this.method).getSymbolType(node.getName()))); 
                    write("load_"); 
                    write(find(node));
                    nl();
                    break;
            }
        }
    }

    private String find(SimpleNode node){
        for(int i=1; i <= this.localNum; i++){
            if(this.locals[i-1].equals(node.getName())){
                return ""+i;
            }
        }
        return "404";
    }

    private static boolean isNumeric(String str) { 
        return str.matches("-?\\d+(\\.\\d+)?");
      }

    private void generateFunctionFooter(SimpleNode func){
        SimpleNode n = func.next();
        if(n != null){
            handle(n.next());
            tab();
            write(getType2(this.store));
        } else{
            tab();
        }
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

        return "L" + type + ";";
    }

    private String getType2(String type){
        switch(type){
            case "int":
                return "i";
            case "boolean":
                return "z";
        }

        return "a";
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
