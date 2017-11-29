package net.ssehub.kernel_haven.typechef.wrapper;

/**
 * An indication on what the next message will contain. The sub-process always sends one of this first, followed
 * by the content
 * 
 * @author Adam
 */
public enum Message {
    
    RESULT,
    LEXER_ERRORS,
    EXCEPTION,
    TIMINGS,
    END,
    CRASH,

}
