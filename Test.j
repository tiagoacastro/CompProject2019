.class public Test
.super java/lan/Object

.method <init>()V
.limit stack 1
.limit locals 1
.var0 is this LTest; from Label0 to Label1
Label0:
	aload_0
	invokespecial java/lang/Object/<init>()V
Label1:
	return
.end method

.method public ola(I)I
.limit stack 10
.limit locals 10
.var0 is this LTest; from Label0 to Label1
Label0:
	iconst_2
Label1:
	ireturn
.end method

.method public static main([Ljava/lang/String;)V
.limit stack 10
.limit locals 10
.var0 is this LTest; from Label0 to Label1
.var 1 is arg0 I from Label0 to Label1
.var 2 is arg1 I from Label0 to Label1
.var 3 is arg2 LTest; from Label0 to Label1
Label0:
	iload_2
	iconst_1
	iadd
	istore_1
	iload_2
	iconst_2
	imul
	istore_1
Label1:
	return
.end method

