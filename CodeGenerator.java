import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class CodeGenerator {
    private SimpleNode root;
    private PrintWriter out;
    private StringBuilder builder;
    private String store;
    private String classe;
    private ArrayList<String> globals = new ArrayList<>();
    private String[] locals = new String[999];
    private int localNum = 0;
    private String method;
    private int ifCounter = 0;
    private int whileCounter = 0;

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
            globals.add(global.next().getName());
            has = true;
        }  
        
        this.root.previous();
        
        return has;
    }

    private void generateGlobalDeclaration(SimpleNode var) {
        write(".field public ");

        write(var.next(2).getName());

        space();

        if(var.previous().jjtGetNumChildren() >= 3)
            write("[");

        write(getType(var.same().next().getName()));

        nl();
    }

    private void generateMethods(){
        generateConstructor();

        SimpleNode method;

        StringBuilder save = this.builder;

        while((method = this.root.next()) != null) {
            nl();
            method = method.next();

            generateFunctionHeader(method);
		    generateFunctionBody(method);
            generateFunctionFooter(method);

            StringBuilder buffer = this.builder;
            this.builder = save;
            write(".limit locals ");
            write("" + (this.localNum+1));
            nl();
            write(buffer.toString());
            nl();
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
        write(".limit stack 100");
        nl();

        this.builder = new StringBuilder();

        Arrays.fill(this.locals, null);
        this.localNum = 0;

        if(func.getName().equals("mainDeclaration")) {
            write(".var 0 is args [Ljava/lang/String;");
            nl();
        }else{
            write(".var 0 is this L");
            write(this.classe);
            write(";");
            nl();

            SimpleNode node;
            func.next();
            while((node = func.next()) != null && node.getName().equals("parameterDeclaration")) {
                node.reset();
                write(".var ");
                write(""+(this.localNum+1));
                write(" is ");
                String id = node.next(2).getName();
                write(id);
                space();
                node = node.previous();
                node.reset();
                if(node.jjtGetNumChildren() >= 3)
                    write("[");
                write(getType(node.next().getName()));
                nl();
                this.locals[this.localNum] = id;
                this.localNum++;
            }
            func.reset();
        }
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

        SimpleNode arg;
        write("(");
        while((arg = func.next()) != null && arg.getName().equals("parameterDeclaration")){
            if(arg.next().jjtGetNumChildren() >= 3)
                write("[");
            write(getType(arg.same().next().getName()));
        }
        func.reset();
        write(")");

        if(func.next().jjtGetNumChildren() >= 3)
            write("[");
        this.store = func.same().next().getName();
        write(getType(this.store));
    }

    private void generateFunctionBody(SimpleNode func){
        SimpleNode body = null;
        do {
            body = func.next();
        } while(!body.getName().equals("methodBody"));

        if(body.children != null){
            SimpleNode node;
            while((node = body.next()) != null && node.getName().equals("varDeclaration")) {
                write(".var ");
                write(""+(this.localNum+1));
                write(" is ");
                write(node.next(2).getName());
                space();
                if(node.previous().jjtGetNumChildren() >= 3)
                    write("[");
                write(getType(node.same().next().getName()));
                nl();
                this.locals[this.localNum] = node.next().getName();
                this.localNum++;
            }
            body.previous();
            while((node = body.next()) != null) {
                handle(node);
            }
        }
    }

    private void handle(SimpleNode node){
        if(isNumeric(node.getName()))
            constant(node);
        else {
            switch(node.getName()){
                case "=":
                    attribution(node);
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
                case "true":
                    tab();
                    write("iconst_1");
                    nl();
                    break;
                case "false":
                    tab();
                    write("iconst_0");
                    nl();
                    break;
                case "new":
                    newOperator(node);
                    break;
                case ".":
                    dotOperator(node);
                    break;
                case "array":
                    handle(node.next());
                    handle(node.next());
                    tab();
                    write("iaload");
                    nl();
                    break;
                case "if":
                    handle(node.next());
                    handle(node.next());
                    tab();
                    write("goto endif" + ifCounter);
                    nl();
                    break;
                case "while":
                    tab();
                    write("while" + whileCounter + ":");
                    nl();
                    handle(node.next());
                    handle(node.next());
                    tab();
                    write("goto while" + whileCounter);
                    nl();
                    tab();
                    write("endwhile" + whileCounter + ":");
                    nl();
                    break;
                case "condition":
                    if (((SimpleNode) node.parent).index == JmmParserConstants.IF) {
                        getCondition(node.next(), "else"+ifCounter, false);
                    }
                    else {
                        getCondition(node.next(), "endwhile" + whileCounter, false);
                    }
                    break;
                case "else":
                    tab();
                    write("else" + ifCounter + ":");
                    nl();
                    handle(node.next());
                    tab();
                    write("endif" + ifCounter + ":");
                    nl();
                    ifCounter++;
                    break;
                case "endwhile":
                    tab();
                    write("endwhile" + whileCounter + ":");
                    nl();
                    break;
                case "body":
                    SimpleNode child;
                    while ((child = node.next()) != null) {
                        handle(child);
                    }
                    break;
                default:
                    identifier(node);
                    break;
            }
        }
    }

    private int find(SimpleNode node){
        for(int i=1; i <= this.localNum; i++){
            if(this.locals[i-1].equals(node.getName())){
                return i;
            }
        }
        return 404;
    }

    private void identifier(SimpleNode node){
        if(globals.contains(node.getName())){
            globalLoad(node);
        } else {
            tab(); 
            write(getType2(JmmParser.getInstance().getMethod(this.method).getSymbolType(node.getName()))); 
            //if(((SimpleNode)node.jjtGetParent()).getName().equals("array"))
            //    write("i");
            int i = find(node);
            if(i > 3)
                write("load ");
            else
                write("load_");
            write("" + i);
        }
        nl();
    }

    private void constant(SimpleNode node){
        tab();
        if(Integer.parseInt(node.getName()) > 32767)
            write("ldc ");
        else {
            if(Integer.parseInt(node.getName()) > 127)
                write("sipush ");
            else{
                if(Integer.parseInt(node.getName()) > 5)
                    write("bipush ");
                else
                    write("iconst_");
            }
        }
        write(node.getName());
        nl();
    }

    private void newOperator(SimpleNode node){
        if(node.next().getName().equals(this.classe)) {
            tab();
            write("new ");
            write(classe);
            nl();
            tab();
            write("dup");
            nl();
            tab();
            write("invokespecial ");
            write(classe);
            write("/<init>()V");
            nl();
        } else if(node.same().getName().equals("array")){
            handle(node.same().next(2));
            tab();
            write("newarray int");
            nl();
        }
    }

    private void attribution(SimpleNode node){
        if(!node.next().getName().equals("array")){
            int idx = find(node.same());
            if(globals.contains(node.same().getName())){
                globalStore(node);
            } else {
                handle(node.next());
                tab();
                write(getType2(JmmParser.getInstance().getMethod(this.method).getSymbolType(node.previous().getName())));
                if(idx > 3)
                    write("store ");
                else
                    write("store_");
                write("" + idx);
            }
        } else {
            handle(node.same().next());
            handle(node.same().next());
            handle(node.next());
            tab();
            write("iastore");
        }
        nl();
    }

    private void globalStore(SimpleNode node){
        tab();
        write("aload_0");
        nl();
        handle(node.next());
        tab();
        write("putfield ");
        write(classe);
        write("/");
        write(node.previous().getName());
        space();
        write(getType(JmmParser.getInstance().getMethod(this.method).getSymbolType(node.same().getName())));
    }

    private void globalLoad(SimpleNode node){
        tab();
        write("aload_0");
        nl();
        tab();
        write("getfield ");
        write(classe);
        write("/");
        write(node.getName());
        space();
        write(getType(JmmParser.getInstance().getMethod(this.method).getSymbolType(node.getName())));
    }

    private static boolean isNumeric(String str) { 
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private void dotOperator(SimpleNode node) {
        if (node.next(2).index == JmmParserConstants.LENGTH) {
            handle(node.previous());
            tab();
            write("arraylength");
            nl();
            return;
        }

        functionCall(node.previous(), node.next());
    }

    private void functionCall(SimpleNode caller, SimpleNode call) {
        SimpleNode parameters = call.next(2);
        SimpleNode param;
        String callerId = "" + find(caller);

        if (!callerId.equals("404"))
            handle(caller);

        while((param = parameters.next()) != null)
            handle(param);
        
        parameters.reset();

        tab();
        if (callerId.equals("404"))
            write("invokestatic " + caller.getName() + "/" + call.previous().getName() + "(");
        else
            write("invokevirtual " + JmmParser.getInstance().getMethod(this.method).getSymbolType(caller.getName()) + "/" + call.previous().getName() + "(");

        while((param = parameters.next()) != null) {
            String name = param.getName();
            if (isNumeric(name))
                write(getType("int"));
            else if (name.equals("true") || name.equals("false"))
                write(getType("boolean"));
            else if (! (""+find(param)).equals("404")) {
                write(getType(JmmParser.getInstance().getMethod(this.method).getSymbolType(param.getName())));
            }
        }

        write(")");
        SymbolTable method = JmmParser.getInstance().getMethod(call.same().getName());
        if (method != null)
            write(getType(method.getType()));
        else
            write(getType(findReturnType(call)));
        nl();
    }

    private String findReturnType(SimpleNode call) {
        SimpleNode parentElement = (SimpleNode) call.jjtGetParent().jjtGetParent();
        String parentName = parentElement.getName();
        if (parentName.equals("+") || parentName.equals("-") || parentName.equals("*") || parentName.equals("/")) {
            return "int";
        }
        else if (parentName.equals("&&") || parentName.equals("<")) {
            return "boolean";
        }
        else if (parentName.equals("=")) {
            return JmmParser.getInstance().getMethod(this.method).getSymbol(((SimpleNode) parentElement.children[0]).getName()).getType();
        }

        return "void";
    }

    private void getCondition(SimpleNode node, String jump, boolean invert) {
        if (node.getName().equals("<")) {
            handle(node.next());
            handle(node.next());
            tab();
            if (invert)
                write("if_icmplt " + jump);
            else
                write("if_icmpge " + jump);
            nl();
        }
        else if (node.getName().equals("&&")) {
            getCondition(node.next(), jump, invert);
            getCondition(node.next(), jump, invert);
        }
        else if (node.getName().equals("!")) {
            getCondition(node.next(), jump, !invert);
        }
        else {
            handle(node);
            tab();
            if (invert)
                write("ifne " + jump);
            else
                write("ifeq " + jump);
            nl();
        }
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
            case "void":
                return "V";
            case "int[]":
                return "[I";
        }

        return "L" + type + ";";
    }

    private String getType2(String type){
        switch(type){
            case "int": case "boolean":
                return "i";
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
