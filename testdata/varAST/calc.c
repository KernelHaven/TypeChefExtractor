void calc(int operand1, int operand2) {
    #if defined(CONFIG_ADDITION)
        int result = operand1 + operand2;
        char op = '+';
    #elif defined(CONFIG_SUBTRACTION)
        int result = operand1 - operand2;
        char op = '-';
    #endif
    
    printf("%i %c %i = %i\n", operand1, op, operand2, result);
}
