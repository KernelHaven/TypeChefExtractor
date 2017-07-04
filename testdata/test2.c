#undef CONFIG_A

int myfunc(int a, int b) {
#ifdef CONFIG_A == 1
	return a;
#elif CONFIG_A == 2
	return a + 1;
#else
	return b;
#endif
}
