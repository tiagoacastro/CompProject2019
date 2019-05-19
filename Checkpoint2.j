.class public Checkpoint2
.super java/lang/Object

.field public olaglobal Ltype;

.method public <init>()V
.limit stack 1
.limit locals 1
.var 0 is this LCheckpoint2;
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public ola(II)I
.limit stack 10
.limit locals 10
.var 0 is this LCheckpoint2;
.var 1 is f I
.var 2 is j I
	iload_1
	iload_2
	iadd
	ireturn
.end method

.method public check()Z
.limit stack 10
.limit locals 10
.var 0 is this LCheckpoint2;
.var 1 is b Z
	iconst_1
	istore_1
	iload_1
	ireturn
.end method

.method public static main([Ljava/lang/String;)V
.limit stack 10
.limit locals 10
.var 0 is args [Ljava/lang/String;
.var 1 is i I
.var 2 is j I
.var 3 is t LCheckpoint2;
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
	new Checkpoint2
	dup
	invokespecial Checkpoint2/<init>()V
	astore_3
	aload_3
	iconst_1
	iconst_1
	invokevirtual Checkpoint2/ola(II)I
	istore_1
	iload_1
	invokestatic io/println(I)V
	return
.end method

