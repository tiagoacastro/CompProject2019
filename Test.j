.class public Test
.super java/lang/Object

.method public <init>()V
.limit stack 1
.limit locals 1
.var 0 is this LTest;
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public ola()I
.limit stack 10
.limit locals 10
.var 0 is this LTest;
	iconst_2
	ireturn
.end method

.method public static main([Ljava/lang/String;)V
.limit stack 10
.limit locals 10
.var 0 is args [Ljava/lang/String;
.var 1 is i I
.var 2 is j I
.var 3 is t LTest;
	iconst_5
	istore_2
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

