.class public Checkpoint2
.super java/lang/Object

.field public olaglobal I
.field public ar [I

.method public <init>()V
.limit stack 1
.limit locals 1
.var 0 is this LCheckpoint2;
	aload_0
	invokespecial java/lang/Object/<init>()V
	return
.end method

.method public ola(II)I
.limit stack 100
.limit locals 3
.var 0 is this LCheckpoint2;
.var 1 is f I
.var 2 is j I
	aload_0
	iconst_1
	putfield Checkpoint2/olaglobal I
	aload_0
	iconst_5
	newarray int
	putfield Checkpoint2/ar [I
	aload_0
	getfield Checkpoint2/ar [I
	iconst_0
	bipush 6
	iastore
	iload_2
	iload_1
	iadd
	aload_0
	getfield Checkpoint2/olaglobal I
	iadd
	aload_0
	getfield Checkpoint2/ar [I
	iconst_0
	iaload
	iadd
	ireturn
.end method


.method public check([I)Z
.limit stack 100
.limit locals 4
.var 0 is this LCheckpoint2;
.var 1 is a [I
.var 2 is b Z
.var 3 is n I
	iconst_1
	istore_2
	iconst_1
	iconst_2
	invokestatic this/ola(II)I
	istore_3
	iload_2
	ireturn
.end method


.method public static main([Ljava/lang/String;)V
.limit stack 100
.limit locals 5
.var 0 is args [Ljava/lang/String;
.var 1 is i I
.var 2 is j I
.var 3 is b Z
.var 4 is t LCheckpoint2;
	iconst_1
	iconst_2
	swap
	isub
	bipush 7
	imul
	iconst_1
	iadd
	iconst_1
	imul
	istore_2
	iload_2
	invokestatic io/println(I)V
	iconst_5
	istore_2
	iconst_1
	iload_2
	iadd
	istore_1
	iconst_2
	iload_2
	imul
	iload_1
	iadd
	istore_1
	new Checkpoint2
	dup
	invokespecial Checkpoint2/<init>()V
	astore 4
	aload 4
	iload_1
	iconst_1
	invokevirtual Checkpoint2/ola(II)I
	istore_1
	iload_1
	invokestatic io/println(I)V
	bipush 22
	istore_2
	while0:
	iload_1
	iload_2
	swap
	if_icmpge endwhile0
	iconst_1
	iload_2
	iadd
	istore_2
	iload_2
	invokestatic io/println(I)V
	goto while0
	endwhile0:
	iconst_3
	istore_1
	iconst_2
	istore_2
	iconst_1
	istore_3
	iload_1
	iload_2
	swap
	if_icmpge else0
	iconst_1
	invokestatic io/println(I)V
	goto endif0
	else0:
	iconst_2
	invokestatic io/println(I)V
	endif0:
	iload_1
	iload_2
	swap
	if_icmpge else1
	iload_3
	ifeq else1
	iconst_1
	invokestatic io/println(I)V
	goto endif1
	else1:
	iconst_2
	invokestatic io/println(I)V
	endif1:
	iconst_1
	ifeq else2
	iconst_1
	invokestatic io/println(I)V
	goto endif2
	else2:
	iconst_2
	invokestatic io/println(I)V
	endif2:
	iload_3
	ifeq else3
	iconst_1
	invokestatic io/println(I)V
	goto endif3
	else3:
	iconst_2
	invokestatic io/println(I)V
	endif3:
	iconst_0
	istore_3
	iload_3
	ifne else4
	iconst_1
	invokestatic io/println(I)V
	goto endif4
	else4:
	iconst_2
	invokestatic io/println(I)V
	endif4:
	iload_3
	ifeq else5
	iload_3
	ifeq else5
	iconst_1
	iconst_1
	iadd
	iconst_1
	imul
	iconst_2
	iadd
	iload_2
	swap
	if_icmplt else5
	iconst_1
	invokestatic io/println(I)V
	goto endif5
	else5:
	iconst_2
	invokestatic io/println(I)V
	endif5:
	return
.end method


