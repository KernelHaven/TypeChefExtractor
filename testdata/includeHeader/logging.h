struct {
    int a;
};

#if (defined CONFIG_LOGGING)

#if (defined CONFIG_LOG_TO_FILE)
    #define LOGFILENAME "/tmp/logfile.log"
#endif // CONFIG_LOG_TO_FILE


#if (defined CONFIG_LOG_TO_FILE)
#define LOG(str) do {                                      \
    FILE *fp = fopen(LOGFILENAME, "a");                    \
    if (fp) {                                              \
        fprintf(fp, "%s:%d %s\n", __FILE__, __LINE__, str);\
        fclose(fp);                                        \
    } else {                                               \
        perror("Opening '" LOGFILENAME "' failed");        \
    }                                                      \
} while (0)
#elif (defined CONFIG_LOG_TO_CONSOLE)
#define LOG(str) do {                                      \
    printf("%s:%d %s\n", __FILE__, __LINE__, str);         \
} while (0)
#endif // CONFIG_LOG_TO_CONSOLE

#endif // CONFIG_LOGGING
