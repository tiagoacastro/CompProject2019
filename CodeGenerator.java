import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class CodeGenerator {
    private SimpleNode root;
    private PrintWriter out;
    private StringBuilder builder;
    private String store;
    private String classe;
    private String extend;
    private ArrayList<String> globals = new ArrayList<>();
    private String[] locals = new String[999];
    private int localNum = 0;
    private String method;
    private int ifCounter = 0;
    private Stack<Integer> ifs = new Stack<>();
    private int whileCounter = 0;
    private int boolOpCounter = 0;
    private int temp = 0;
    private int stack = 0;

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
            this.extend = extend.same().getName();
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

        write("'");
        write(var.next(2).getName());
        write("'");

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

            ifs.empty();

            StringBuilder buffer = this.builder;
            this.builder = save;
            write(".limit stack ");
            write("" + this.stack);
            nl();
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
        write(".var 0 is 'this' L");
        write(this.classe);
        write(";");
        nl();
        tab();
        write("aload_0");
        nl();
        tab();
        write("invokespecial ");
        if(this.extend == null)
            write("java/lang/Object");
        else
            write(this.extend);
        write("/<init>()V");
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

        this.builder = new StringBuilder();

        Arrays.fill(this.locals, null);
        this.localNum = 0;
        this.temp = 0;
        this.stack = 0;

        if(func.getName().equals("mainDeclaration")) {
            write(".var 0 is args [Ljava/lang/String;");
            nl();
        }else{
            write(".var 0 is 'this' L");
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
                write("'");
                write(id);
                write("'");
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

        this.method = func.next(2).getName() + "(";
        for (int i = 0; i < func.children.length; i++) {
            if (func.children[i] instanceof ASTparameterDeclaration) {
                this.method += ((ASTparameterDeclaration) func.children[i]).getType();
            }
        }
        this.method += ")";

        write(func.same().getName());

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

        if(func.same().jjtGetNumChildren() >= 3)
            this.store = "int[]";
    }

    private void generateFunctionBody(SimpleNode func){
        SimpleNode body = null;
        do {
            body = func.next();
        } while(!body.getName().equals("methodBody"));

        SimpleNode node;
        while((node = body.next()) != null && node.getName().equals("varDeclaration")) {
            write(".var ");
            write(""+(this.localNum+1));
            write(" is ");
            write("'");
            write(node.next(2).getName());
            write("'");
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

    private void handle(SimpleNode node){
        if(isNumeric(node.getName())) {
            constant(node);
            inc();
        }
        else {
            switch(node.getName()){
                case "=":
                    attribution(node);
                    break;
                case "+":
                    if(!isOp(node.next()) && isOp(node.next())){
                        handle(node.same());
                        handle(node.previous());
                    } else {
                        node.reset();
                        handle(node.next());
                        handle(node.next());
                    }
                    tab();
                    write("iadd");
                    dec();
                    nl();
                    break;
                case "-":
                    if(!isOp(node.next()) && isOp(node.next())){
                        handle(node.same());
                        handle(node.previous());
                        tab();
                        write("swap");
                        nl();
                    } else {
                        node.reset();
                        handle(node.next());
                        handle(node.next());
                    }
                    tab();
                    write("isub");
                    dec();
                    nl();
                    break;
                case "*":
                    if(!isOp(node.next()) && isOp(node.next())){
                        handle(node.same());
                        handle(node.previous());
                    } else {
                        node.reset();
                        handle(node.next());
                        handle(node.next());
                    }
                    tab();
                    write("imul");
                    dec();
                    nl();
                    break;
                case "/":
                    if(!isOp(node.next()) && isOp(node.next())){
                        handle(node.same());
                        handle(node.previous());
                    } else {
                        node.reset();
                        handle(node.next());
                        handle(node.next());
                    }
                    tab();
                    write("idiv");
                    dec();
                    nl();
                    break;
                case "true":
                    tab();
                    write("iconst_1");
                    inc();
                    nl();
                    break;
                case "false":
                    tab();
                    write("iconst_0");
                    inc();
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
                    dec();
                    nl();
                    break;
                case "<": case "&&":
                    getCondition(node, "boolOp"+boolOpCounter, false);
                    boolOp();
                    break;
                case "!":
                    getCondition(node, "boolOp"+boolOpCounter, true);
                    boolOp();
                    break;
                case "if":
                    int j = ifCounter;
                    ifs.push(new Integer(ifCounter));
                    ifCounter++;
                    handle(node.next());
                    handle(node.next());
                    tab();
                    write("goto endif" + j);
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
                    whileCounter++;
                    break;
                case "condition":
                    if (((SimpleNode) node.parent).index == JmmParserConstants.IF) {
                        getCondition(node.next(), "else"+ifs.peek(), false);
                    }
                    else {
                        getCondition(node.next(), "endwhile" + whileCounter, false);
                    }
                    break;
                case "else":
                    tab();
                    int jump = ifs.pop();
                    write("else" + jump + ":");
                    nl();
                    handle(node.next());
                    tab();
                    write("endif" + jump + ":");
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
                    inc();
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

        if(node.getName().equals("this"))
            return 0;

        return 404;
    }

    private boolean isOp(SimpleNode node){
        String name = node.getName();
        return name.equals("+") || name.equals("-") || name.equals("*") || name.equals("/") || 
               name.equals("<") || name.equals("&&") || name.equals("!");
    }

    private void identifier(SimpleNode node){
        if(globals.contains(node.getName())){
            globalLoad(node);
        } else {
            tab(); 

            if(node.getName().equals("this")){
                write("a");
            } else {
                write(getType2(JmmParser.getInstance().getMethod(this.method).getSymbolType(node.getName()))); 
            }

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
        if(node.next().getName().equals("array")){
            handle(node.same().next(2));
            tab();
            write("newarray int");
            nl();
        } else {
            tab();
            write("new ");
            write(node.same().getName());
            inc();
            nl();
            tab();
            write("dup");
            inc();
            nl();
            tab();
            write("invokespecial ");
            write(node.same().getName());
            write("/<init>()V");
            dec();
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
                dec();
            }
        } else {
            handle(node.same().next());
            handle(node.same().next());
            handle(node.next());
            tab();
            write("iastore");
            sub(3);
        }
        nl();
    }

    private void globalStore(SimpleNode node){
        tab();
        write("aload_0");
        inc();
        nl();
        handle(node.next());
        tab();
        write("putfield ");
        sub(2);
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

        if(caller.getName().equals("new")){
            newOperator(caller);
            caller.reset();
            caller = caller.next();
            callerId = "new";
        } else {
            if (!callerId.equals("404"))
                handle(caller);
        }

        if (parameters != null){
            while((param = parameters.next()) != null)
                handle(param);
            
            parameters.reset();
        }

        tab();
        switch(callerId){
            case "404":
                write("invokestatic " + caller.getName() + "/" + call.previous().getName() + "(");
                break;
            case "new":
                write("invokevirtual " + caller.getName() + "/" + call.previous().getName() + "(");
                dec();
                break;
            default:
                write("invokevirtual ");
                if(caller.getName().equals("this"))
                    write(this.classe);
                else
                    write(JmmParser.getInstance().getMethod(this.method).getSymbolType(caller.getName()));
                write("/" + call.previous().getName() + "(");
                dec();
                break;
        }           

        SymbolTable method;
        if (parameters != null) {
            while((param = parameters.next()) != null) {
                String name = param.getName();
                if(name.equals("array")){
                    param.reset();
                    write("I");
                    continue;
                }
                if (isNumeric(name) || isOp(param))
                    write("I");
                else if (name.equals("true") || name.equals("false"))
                    write("Z");
                else if (!(""+find(param)).equals("404") || globals.contains(name))
                    write(getType(JmmParser.getInstance().getMethod(this.method).getSymbolType(name)));
                else if (name.equals(".")){
                    param.reset();
                    param.next(2).reset();
                    if(param.same().getName().equals("length")){
                        write("I");
                    } else {
                        method = JmmParser.getInstance().getMethod(((ASTfunctionCall) param.same()).getMethodName());
                        returns(method, param.same(), false);
                    }
                }
            }
            sub(parameters.children.length);
        }

        write(")");
        method = JmmParser.getInstance().getMethod(((ASTfunctionCall) call).getMethodName());
        returns(method, call, true);
        nl();
    }

    private void returns(SymbolTable method, SimpleNode call, boolean pop){
        String parentName = ((SimpleNode) call.jjtGetParent().jjtGetParent()).getName();
        String type;
        if (method != null) {
            type = getType(method.getType());
            if(!type.equals("V"))
                inc();
            write(type);
            if(!parentName.equals("!") && !parentName.equals("parameters") && !parentName.equals("return") && !parentName.equals("condition") && pop && !type.equals("V") && findReturnType(call).equals("void")){
                nl();
                tab();
                dec();
                write("pop");
            }
        } else {
            type = getType(findReturnType(call));
            if(!type.equals("V"))
                inc();
            write(type);
        }
    }

    private String findReturnType(SimpleNode call) {
        SimpleNode parentElement = (SimpleNode) call.jjtGetParent().jjtGetParent();
        String parentName = parentElement.getName();
        if (parentName.equals("+") || parentName.equals("-") || parentName.equals("*") || parentName.equals("/") || parentName.equals("<")) {
            return "int";
        } 
        else if(parentName.equals("array")){
            for(int i = 0; i < parentElement.children.length; i++){
                SimpleNode node = (SimpleNode)parentElement.children[i];
                if(node != null && node.getName().equals(".") && node.children[1] == call){
                    if(i == 0)
                        return "int[]";
                    else
                        return "int";
                }
            }
        }
        else if (parentName.equals("&&") || parentName.equals("!") || parentName.equals("condition")) {
            return "boolean";
        }
        else if (parentName.equals("=")) {
            if(((SimpleNode) parentElement.children[0]).getName().equals("array"))
                return "int";
            else
                return JmmParser.getInstance().getMethod(this.method).getSymbol(((SimpleNode) parentElement.children[0]).getName()).getType();
        }
        return "void";
    }

    private void boolOp() {
        tab();
        write("iconst_1");
        nl();
        tab();
        write("goto endBoolOp" + boolOpCounter);
        nl();
        tab();
        write("boolOp" + boolOpCounter + ":");
        nl();
        tab();
        write("iconst_0");
        nl();
        tab();
        write("endBoolOp" + boolOpCounter + ":");
        nl();
        boolOpCounter++;
        inc();
    }

    private void getCondition(SimpleNode node, String jump, boolean invert) {
        if (node.getName().equals("<")) {
            if(!isOp(node.next()) && isOp(node.next())){
                handle(node.same());
                handle(node.previous());
                tab();
                write("swap");
                nl();
            } else {
                node.reset();
                handle(node.next());
                handle(node.next());
            }
            tab();
            if (invert)
                write("if_icmplt " + jump);
            else
                write("if_icmpge " + jump);
            nl();
            sub(2);
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
            dec();
        }
    }

    private void generateFunctionFooter(SimpleNode func){
        SimpleNode n = func.next();
        if(n != null){
            handle(n.next());
            tab();
            write(getType2(this.store));
            dec();
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

    private void inc(){
        this.temp++;
        if(this.temp > this.stack)
            this.stack = this.temp;
    }

    private void dec(){
        this.temp--;
    }

    private void add(int add){
        this.temp += add;
        if(this.temp > this.stack)
            this.stack = this.temp;
    }

    private void sub(int sub){
        this.temp -= sub;
    }
}
