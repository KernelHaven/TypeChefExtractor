
//#define CONFIG_C 1

#if defined(CONFIG_A)
	#define A 0
#else defined(CONFIG_B)
	#define A 1
#else
	#define A 2
#endif

int myfunc(int a, int b) {
#if A == 0
	return a;
#elif A == 2
	return a + 1;
#else
	return b;
#endif

	if (0 > CONFIG_C) {
		
	}
}
