
//#define CONFIG_C 1

int myfunc(int a, int b) {
#if CONFIG_A == 1
	return a;
#elif CONFIG_A == 2
	return a + 1;
#else
	return b;
#endif

	if (0 > CONFIG_C) {
		
	}
}
