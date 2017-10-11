package net.ssehub.kernel_haven.typechef.wrapper;

import static net.ssehub.kernel_haven.config.Setting.Type.BOOLEAN;
import static net.ssehub.kernel_haven.config.Setting.Type.DIRECTORY;
import static net.ssehub.kernel_haven.config.Setting.Type.FILE;
import static net.ssehub.kernel_haven.config.Setting.Type.INTEGER;
import static net.ssehub.kernel_haven.config.Setting.Type.SETTING_LIST;
import static net.ssehub.kernel_haven.config.Setting.Type.STRING;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.ssehub.kernel_haven.SetUpException;
import net.ssehub.kernel_haven.config.Configuration;
import net.ssehub.kernel_haven.config.Setting;

/**
 * Settings used to configure typechef.
 *
 * @author Adam
 */
public class TypeChefSettings {
    
    public static final Setting<Boolean> IGNORE_OTHER_MODELS
        = new Setting<>("code.extractor.ignore_other_models", BOOLEAN, true, "false", "TODO");
    
    public static final Setting<Boolean> PARSE_TO_AST
        = new Setting<>("code.extractor.parse_to_ast", BOOLEAN, true, "false", "TODO");
    
    public static final Setting<File> SYSTEM_ROOT
        = new Setting<>("code.extractor.system_root", DIRECTORY, true, "/", "TODO");
    
    public static final Setting<Boolean> SKIP_DEFAULT_INCLUDE_DIRS
        = new Setting<>("code.extractor.skip_default_include_dirs", BOOLEAN, true, "false", "TODO");
    
    public static final Setting<String> PROCES_RAM
        = new Setting<>("code.extractor.process_ram", STRING, true, "15g", "TODO");

    public static final Setting<List<String>> STATIC_INCLUDES
        = new Setting<>("code.extractor.static_include", SETTING_LIST, false, null, "TODO");
    
    public static final Setting<List<String>> POST_INCLUDE_DIRS
        = new Setting<>("code.extractor.post_include_dir", SETTING_LIST, false, null, "TODO");
    
    public static final Setting<List<String>> SOURCE_INCLUDE_DIRS
        = new Setting<>("code.extractor.source_include_dir", SETTING_LIST, false, null, "TODO");
    
    public static final Setting<Boolean> ADD_LINUX_SOURCE_INCLUDE_DIRS
        = new Setting<>("code.extractor.add_linux_source_include_dirs", BOOLEAN, true, "false", "TODO");
    
    public static final Setting<List<String>> PREPROCESSOR_DEFINES
        = new Setting<>("code.extractor.preprocessor_define", SETTING_LIST, false, null, "TODO");
    
    public static final Setting<File> KBUILDPARAM_FILE
        = new Setting<>("code.extractor.kbuildparam_file", FILE, false, null, "TODO");
    
    public static final Setting<File> PLATFORM_HEADER
        = new Setting<>("code.extractor.platform_header", FILE, false, null, "TODO");
    
    public static final Setting<File> OPEN_VARIABLES_FILE
        = new Setting<>("code.extractor.open_variables", FILE, false, null, "TODO");
    
    public static final Setting<File> SMALL_FEATURE_MODEL_FILE
        = new Setting<>("code.extractor.small_feature_model", FILE, false, null, "TODO");
        
    public static final Setting<Integer> MAX_RECEIVING_THREADS
        = new Setting<>("code.extractor.max_receiving_threads", INTEGER, true, "0", "TODO");
    
    public static final Setting<Boolean> DEBUG_CALL_IN_SAME_VM
        = new Setting<>("code.extractor.debug.call_in_same_vm", BOOLEAN, true, "false", "TODO");
    
    public static final Setting<Boolean> DEBUG_LOG_CALL_PARAMS
        = new Setting<>("ode.extractor.debug.log_call_params", BOOLEAN, true, "false", "TODO");
    
    public static final Setting<Boolean> DEBUG_INHERIT_OUTPUT
        = new Setting<>("code.extractor.debug.inherit_output", BOOLEAN, true, "false", "TODO");
    
    /**
     * Holds all declared setting constants.
     */
    private static final Set<Setting<?>> SETTINGS = new HashSet<>();
    
    static {
        for (Field field : TypeChefSettings.class.getFields()) {
            if (Setting.class.isAssignableFrom(field.getType())
                    && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                try {
                    SETTINGS.add((Setting<?>) field.get(null));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Don't allow instance of this class.
     */
    private TypeChefSettings() {
    }
    
    /**
     * Registers all settings declared in this class to the given configuration object.
     * 
     * @param config The configuration to register the settings to.
     * 
     * @throws SetUpException If any setting restrictions are violated.
     */
    public static void registerAllSettings(Configuration config) throws SetUpException {
        for (Setting<?> setting : SETTINGS) {
            config.registerSetting(setting);
        }
    }
    
}