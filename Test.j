.class public Test
.super java/lan/Object

.method <init>()V
.limit stack 1
.limit locals 1
.var0 is this LTest;
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public ola(I)I
.limit stack 10
.limit locals 10
.var0 is this LTest;
	iconst_2
	ireturn
.end method

.method public static main([Ljava/lang/String;)V
.limit stack 10
.limit locals 10
.var0 is arg0 [Ljava/lang/String;
.var1 is arg0 I
.var2 is arg1 I
.var3 is arg2 LTest;
	iload_2
	iconst_1
	iadd
	istore_1
	iload_2
	iconst_2
	imul
	istore_1
	return
.end method

