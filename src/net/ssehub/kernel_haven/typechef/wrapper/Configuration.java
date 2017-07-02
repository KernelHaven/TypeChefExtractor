package net.ssehub.kernel_haven.typechef.wrapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.ssehub.kernel_haven.SetUpException;
import net.ssehub.kernel_haven.config.CodeExtractorConfiguration;
import net.ssehub.kernel_haven.typechef.TypeChefExtractor;
import net.ssehub.kernel_haven.typechef.util.KbuildParamFile;
import net.ssehub.kernel_haven.typechef.util.OutputVoider;
import net.ssehub.kernel_haven.util.ExtractorException;
import net.ssehub.kernel_haven.util.Logger;
import net.ssehub.kernel_haven.variability_model.VariabilityVariable;

/**
 * A configuration for Typechef.
 * 
 * @author Adam
 */
public class Configuration {

    private static final Logger LOGGER = Logger.get();
    
    private File resDir;
    
    private File sourceDir;
    
    private File platformHeader;
    
    private File openVariablesFile;
    
    private File systemRoot;
    
    private List<File> staticIncludes;
    
    private List<File> postIncludeDirs;
    
    private List<File> sourceIncludeDirs;
    
    private List<String> preprocessorDefines;
    
    private KbuildParamFile kbuildParamFile;
    
    private boolean parseToAst;
    
    private String processRam;
    
    /**
     * Creates a Typechef configuration from the given {@link CodeExtractorConfiguration}.
     * It uses the following keys:
     * <ul>
     *  <li>
     *      <b>Required</b>
     *      <ul>
     *          <li>source_tree</li>
     *      </ul>
     *  </li>
     *  <li>
     *      <b>Required</b> (with default values)
     *      <ul>
     *          <li>code.extractor.system_root</li>
     *          <li>code.extractor.skip_default_include_dirs</li>
     *          <li>code.extractor.parse_to_ast</li>
     *          <li>code.extractor.process_ram</li>
     *      </ul>
     *  </li>
     *  <li>
     *      <b>Optional</b>
     *      <ul>
     *          <li>ccode.extractor.static_include.0</li>
     *          <li>code.extractor.post_include_dir.0</li>
     *          <li>code.extractor.source_include_dir.0</li>
     *          <li>code.extractor.add_linux_source_include_dirs</li>
     *          <li>code.extractor.preprocessor_define.0</li>
     *          <li>code.extractor.kbuildparam_file</li>
     *          <li>code.extractor.platform_header</li>
     *      </ul>
     *  </li>
     * </ul>
     * The keys ending with ".0" are lists, which can be expanded by another key ending ".1", etc.
     * This constructor also generates some required resource needed by Typechef (like the platform header). 
     * 
     * @param config The configuration to read the settings from.
     * 
     * @throws SetUpException If any of the mandatory settings can't be read, or some auto-generation fails.
     */
    public Configuration(CodeExtractorConfiguration config) throws SetUpException {
        staticIncludes = new ArrayList<>();
        postIncludeDirs = new ArrayList<>();
        sourceIncludeDirs = new ArrayList<>();
        preprocessorDefines = new ArrayList<>();
        
        loadFromConfig(config);
        checkParameters();
    }
    
    /**
     * Reads the settings from the config. Generates some files that are not supplied (like the platform header).
     * 
     * @param config The config to read from.
     * 
     * @throws SetUpException If generating files fails.
     */
    private void loadFromConfig(CodeExtractorConfiguration config) throws SetUpException {
        this.resDir = config.getExtractorResourceDir(TypeChefExtractor.class);
        this.sourceDir = config.getSourceTree();
        this.parseToAst = Boolean.parseBoolean(config.getProperty("code.extractor.parse_to_ast", "false"));
        this.processRam = config.getProperty("code.extractor.process_ram", "20g");
        
        String platformHeader = config.getProperty("code.extractor.platform_header");
        if (platformHeader != null) {
            this.platformHeader = new File(platformHeader);
        } else {
            this.platformHeader = new File(resDir, "platform.h");
            if (!this.platformHeader.isFile()) {
                generatePlatformHeader();
            }
        }
        if (!this.platformHeader.isFile() || !this.platformHeader.canRead()) {
            throw new SetUpException("Invalid platform header at " + this.platformHeader.getAbsolutePath());
        }
        
        this.systemRoot = new File(config.getProperty("code.extractor.system_root", "/"));
        
        for (String staticInclude : getSettingList(config, "code.extractor.static_include.")) {
            this.staticIncludes.add(new File(staticInclude));
        }
        
        for (String postIncludeDir : getSettingList(config, "code.extractor.post_include_dir.")) {
            this.postIncludeDirs.add(new File(postIncludeDir));
        }
        if (!Boolean.parseBoolean(config.getProperty("code.extractor.skip_default_include_dirs", "false"))) {
            this.postIncludeDirs.add(new File("usr/lib/gcc/x86_64-linux-gnu/5/include"));
            this.postIncludeDirs.add(new File("usr/include/x86_64-linux-gnu"));
            this.postIncludeDirs.add(new File("usr/include"));
        }
        
        for (String sourceIncludeDir : getSettingList(config, "code.extractor.source_include_dir.")) {
            this.sourceIncludeDirs.add(new File(sourceIncludeDir));
        }
        
        boolean addLinuxDirs = Boolean.parseBoolean(config.getProperty(
                "code.extractor.add_linux_source_include_dirs", "false"));
        if (addLinuxDirs) {
            if (config.getArch() == null) {
                throw new SetUpException(
                        "No arch specified; this is needed for code.extractor.add_linux_source_include_dirs");
            }
            this.sourceIncludeDirs.add(new File("include"));
            this.sourceIncludeDirs.add(new File("arch/" + config.getArch() + "/include"));
            this.sourceIncludeDirs.add(new File("arch/" + config.getArch() + "/include/generated"));
            this.sourceIncludeDirs.add(new File("arch/" + config.getArch() + "/include/uapi"));
            this.sourceIncludeDirs.add(new File("arch/" + config.getArch() + "/include/generated/uapi"));
            this.sourceIncludeDirs.add(new File("arch/" + config.getArch() + "/include/asm/mach-default"));
            this.sourceIncludeDirs.add(new File("arch/" + config.getArch() + "/include/asm/mach-generic"));
            this.sourceIncludeDirs.add(new File("arch/" + config.getArch() + "/include/asm/mach-voyager"));
            this.sourceIncludeDirs.add(new File("include/uapi"));
        }
        
        for (String preprocessorDefine : getSettingList(config, "code.extractor.preprocessor_define.")) {
            this.preprocessorDefines.add(preprocessorDefine);
        }
        
        String kbuildParamFile = config.getProperty("code.extractor.kbuildparam_file"); // TODO autogenerate?
        if (kbuildParamFile != null) {
            try {
                this.kbuildParamFile = new KbuildParamFile(new File(kbuildParamFile), config.getSourceTree());
            } catch (IOException e) {
                throw new SetUpException(e);
            }
        }
    }
    
    /**
     * Helper method for reading a list of keys that is denoted by ending in ".0", ".1", ".2", etc.
     * 
     * @param config The config to read from.
     * @param prefix The common prefix of the setting keys, up to the number (including the ".").
     * 
     * @return The list of setting values; never null; can be an empty list.
     */
    private List<String> getSettingList(CodeExtractorConfiguration config, String prefix) {
        int index = 0;
        
        List<String> result = new LinkedList<>();
        
        String setting;
        while ((setting = config.getProperty(prefix + index)) != null) {
            result.add(setting);
            index++;
        }
        
        return result;
    }

    /**
     * Checks the sanity of the given settings. This mostly checks for null and if files exists and are readable.
     * 
     * @throws SetUpException If any error is encountered during checks.
     */
    private void checkParameters() throws SetUpException {
        // TODO: go through messages and checks
        
        if (sourceDir == null || !sourceDir.isDirectory()) {
            throw new SetUpException("Source directory \"" + sourceDir + "\" is not a directory.");
        }
        if (!sourceDir.canRead()) {
            throw new SetUpException("Source directory \"" + sourceDir + "\" is not readable.");
        }
        
        if (platformHeader == null || !platformHeader.isFile()) {
            throw new SetUpException("Platform header \"" + platformHeader + "\" does not exist");
        }
        if (!platformHeader.canRead()) {
            throw new SetUpException("Platform header \"" + platformHeader + "\" is not readable");
        }
        
        if (openVariablesFile != null) {
            if (!openVariablesFile.isFile()) {
                throw new SetUpException("openFeatures file not specified");
            }
            if (!openVariablesFile.canRead()) {
                throw new SetUpException("openFeatures file can not be read");
            }
        }
        
        if (systemRoot == null || !systemRoot.isDirectory()) {
            throw new SetUpException("System root \"" + systemRoot + "\" is not a directory.");
        }
        
        if (staticIncludes.isEmpty()) {
            LOGGER.logWarning("No static includes defined");
        }
        for (File staticInclude : staticIncludes) {
            if (staticInclude == null || !staticInclude.isFile()) {
                throw new SetUpException("Static include header \"" + staticInclude + "\" does not exist");
            }
            if (!staticInclude.canRead()) {
                throw new SetUpException("Static include header \"" + staticInclude + "\" is not readable");
            }
        }
        
        if (postIncludeDirs.isEmpty()) {
            LOGGER.logWarning("No post include directories specified");
        }
        for (File postIncludeDir : postIncludeDirs) {
            postIncludeDir = new File(systemRoot, postIncludeDir.getPath());
            if (!postIncludeDir.isDirectory()) {
                LOGGER.logWarning("Post include directory \"" + postIncludeDir + "\" is not a directory");
            }
        }
        
        if (sourceIncludeDirs.isEmpty()) {
            LOGGER.logWarning("No source include directories specified");
        }
        for (File sourceIncludeDir : sourceIncludeDirs) {
            sourceIncludeDir = new File(sourceDir, sourceIncludeDir.getPath());
//            if (!sourceIncludeDir.isDirectory()) {
//                LOGGER.logWarning("Source include directory \"" + sourceIncludeDir + "\" is not a directory");
//            }
        }
        
        // TODO: preprocessorDefines
        
        if (kbuildParamFile == null) {
            LOGGER.logWarning("No kbuildParamFile specified");
        }
    }
    
    /**
     * Generates the platform header. Expects the platformHeader attribute to point to the location to generate to.
     * 
     * @throws SetUpException If generating the file fails.
     */
    private void generatePlatformHeader() throws SetUpException {
        LOGGER.logDebug("Generating platform header to " + platformHeader.getAbsolutePath());
        
        ProcessBuilder builder = new ProcessBuilder("gcc", "-dM", "-", "-E", "-std=gnu99");
        
        builder.redirectOutput(platformHeader);
        
        Process process;
        try {
            process = builder.start();
            new OutputVoider(process.getErrorStream());
            
            // gcc waits for input; close stdin so it can output and exit
            process.getOutputStream().close();
            
            process.waitFor();
            
        } catch (IOException e) {
            throw new SetUpException(e);
        } catch (InterruptedException e) {
        }
    }
    
    /**
     * Converts the configuration into a parameter list for Typechef.
     * 
     * @param sourceFile The source file inside the source tree to generate the config for.
     * 
     * @return A parameter list that can be passed to Typechef.
     */
    public List<String> buildParameters(File sourceFile) {
        List<String> params = new ArrayList<>();
        
        // environment stuff
        params.add("--platfromHeader=" + platformHeader.getAbsolutePath());
        params.add("--systemRoot=" + systemRoot.getAbsolutePath());
        
        // typechef behavior
        params.add("--lex");
        params.add("--lexNoStdout");
        params.add("--no-analysis");
        

        // Kconfig variables
        params.add("--prefixonly=CONFIG_");
        if (openVariablesFile != null) {
            params.add("--openFeat=" + openVariablesFile.getAbsolutePath());
        }
        
        // include stuff
        for (File staticInclude : staticIncludes) {
            params.add("--include=" + staticInclude.getAbsolutePath());
        }
        for (File sourceIncludeDir : sourceIncludeDirs) {
            params.add("--incdir=" + new File(sourceDir, sourceIncludeDir.getPath()).getAbsolutePath());
        }
        for (File postIncludeDir : postIncludeDirs) {
            params.add("--postIncludes=" + postIncludeDir.getPath());
        }
        
        // preprocessor definitions
        for (String symbol : preprocessorDefines) {
            params.add("-D" + symbol);
        }
        
        // kbuildParamFile
        if (kbuildParamFile != null) {
            params.addAll(kbuildParamFile.getExtraParameters(sourceFile));
        }
        
        // the source file to parse
        params.add(new File(sourceDir, sourceFile.getPath()).getAbsolutePath());
        
        return params;
    }
    
    /**
     * Whether to parse the result of Typechef into an AST or simply keeping a flat list of presence conditions.
     * 
     * @return The boolean setting read from the user.
     */
    public boolean isParseToAst() {
        return parseToAst;
    }
    
    /**
     * The amount of RAM the Typechef process is allowed to use.
     * This is in the form that the Java command line parameter expects this, so e.g. "20g" or "1024m".
     * 
     * @return The max amount of RAM.
     */
    public String getProcessRam() {
        return processRam;
    }
    
    /**
     * Sets the list of variables that are in the variability model. This is required to generate
     * a file to pass to Typechef that contains all open variables.
     * 
     * @param variables The {@link VariabilityVariable}s in the variability model.
     * 
     * @throws ExtractorException If writing the files to a file for Typechef fails.
     */
    public void setVariables(Set<VariabilityVariable> variables) throws ExtractorException {
        try {
            this.openVariablesFile = File.createTempFile("open_variables", ".txt");
            
            LOGGER.logDebug("Writing open variables file to " + this.openVariablesFile.getAbsolutePath());
            
            FileWriter out = null;
            try {
                out = new FileWriter(openVariablesFile);
                
                for (VariabilityVariable var : variables) {
                    out.write(var.getName());
                    out.write('\n');
                    if (var.getType().equals("tristate")) {
                        out.write(var.getName() + "_MODULE");
                        out.write('\n');
                    }
                }
                
            } finally {
                if (out != null) {
                    out.close();
                }
            }
            
        } catch (IOException e) {
            throw new ExtractorException("Can't write open variables file", e);
        }
    }
    
}
