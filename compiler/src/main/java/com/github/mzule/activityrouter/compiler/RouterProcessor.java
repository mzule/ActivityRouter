package com.github.mzule.activityrouter.compiler;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import com.github.mzule.activityrouter.annotation.Module;
import com.github.mzule.activityrouter.annotation.Modules;
import com.github.mzule.activityrouter.annotation.Router;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {
    private static final boolean DEBUG = false;
    private Messager messager;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> ret = new HashSet<>();
        ret.add(Modules.class.getCanonicalName());
        ret.add(Module.class.getCanonicalName());
        ret.add(Router.class.getCanonicalName());
        return ret;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        debug("process apt with " + annotations.toString());
        if (annotations.isEmpty()) {
            return false;
        }
        boolean hasModule = false;
        boolean hasModules = false;
        // module
        String moduleName = "RouterMapping";
        Set<? extends Element> moduleList = roundEnv.getElementsAnnotatedWith(Module.class);
        if (moduleList != null && moduleList.size() > 0) {
            Module annotation = moduleList.iterator().next().getAnnotation(Module.class);
            moduleName = moduleName + "_" + annotation.value();
            hasModule = true;
        }
        // modules
        String[] moduleNames = null;
        Set<? extends Element> modulesList = roundEnv.getElementsAnnotatedWith(Modules.class);
        if (modulesList != null && modulesList.size() > 0) {
            Element modules = modulesList.iterator().next();
            moduleNames = modules.getAnnotation(Modules.class).value();
            hasModules = true;
        }
        // RouterInit
        if (hasModules) {
            debug("generate modules RouterInit");
            generateModulesRouterInit(moduleNames);
        } else if (!hasModule) {
            debug("generate default RouterInit");
            generateDefaultRouterInit();
        }
        // RouterMapping
        return handleRouter(moduleName, roundEnv);
    }

    private void generateDefaultRouterInit() {
        MethodSpec.Builder initMethod = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);
        initMethod.addStatement("RouterMapping.map()");
        TypeSpec routerInit = TypeSpec.classBuilder("RouterInit")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(initMethod.build())
                .build();
        try {
            JavaFile.builder("com.github.mzule.activityrouter.router", routerInit)
                    .build()
                    .writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateModulesRouterInit(String[] moduleNames) {
        MethodSpec.Builder initMethod = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);
        for (String module : moduleNames) {
            initMethod.addStatement("RouterMapping_" + module + ".map()");
        }
        TypeSpec routerInit = TypeSpec.classBuilder("RouterInit")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(initMethod.build())
                .build();
        try {
            JavaFile.builder("com.github.mzule.activityrouter.router", routerInit)
                    .build()
                    .writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean handleRouter(String genClassName, RoundEnvironment roundEnv) {
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Router.class);

        MethodSpec.Builder mapMethod = MethodSpec.methodBuilder("map")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .addStatement("java.util.Map<String,String> transfer = null")
                .addStatement("com.github.mzule.activityrouter.router.ExtraTypes extraTypes")
                .addCode("\n");

        for (Element element : elements) {
            Router router = element.getAnnotation(Router.class);
            String[] transfer = router.transfer();
            if (transfer.length > 0 && !"".equals(transfer[0])) {
                mapMethod.addStatement("transfer = new java.util.HashMap<String, String>()");
                for (String s : transfer) {
                    String[] components = s.split("=>");
                    if (components.length != 2) {
                        error("transfer `" + s + "` not match a=>b format");
                        break;
                    }
                    mapMethod.addStatement("transfer.put($S, $S)", components[0], components[1]);
                }
            } else {
                mapMethod.addStatement("transfer = null");
            }

            mapMethod.addStatement("extraTypes = new com.github.mzule.activityrouter.router.ExtraTypes()");
            mapMethod.addStatement("extraTypes.setTransfer(transfer)");

            addStatement(mapMethod, int.class, router.intParams());
            addStatement(mapMethod, long.class, router.longParams());
            addStatement(mapMethod, boolean.class, router.booleanParams());
            addStatement(mapMethod, short.class, router.shortParams());
            addStatement(mapMethod, float.class, router.floatParams());
            addStatement(mapMethod, double.class, router.doubleParams());
            addStatement(mapMethod, byte.class, router.byteParams());
            addStatement(mapMethod, char.class, router.charParams());

            for (String format : router.value()) {
                ClassName className;
                Name methodName = null;
                if (element.getKind() == ElementKind.CLASS) {
                    className = ClassName.get((TypeElement) element);
                } else if (element.getKind() == ElementKind.METHOD) {
                    className = ClassName.get((TypeElement) element.getEnclosingElement());
                    methodName = element.getSimpleName();
                } else {
                    throw new IllegalArgumentException("unknow type");
                }
                if (format.startsWith("/")) {
                    error("Router#value can not start with '/'. at [" + className + "]@Router(\"" + format + "\")");
                    return false;
                }
                if (format.endsWith("/")) {
                    error("Router#value can not end with '/'. at [" + className + "]@Router(\"" + format + "\")");
                    return false;
                }
                if (element.getKind() == ElementKind.CLASS) {
                    mapMethod.addStatement("com.github.mzule.activityrouter.router.Routers.map($S, $T.class, null, extraTypes)", format, className);
                } else {
                    mapMethod.addStatement("com.github.mzule.activityrouter.router.Routers.map($S, null, " +
                            "new MethodInvoker() {\n" +
                            "   public void invoke(android.content.Context context, android.os.Bundle bundle) {\n" +
                            "       $T.$N(context, bundle);\n" +
                            "   }\n" +
                            "}, " +
                            "extraTypes)", format, className, methodName);
                }
            }
            mapMethod.addCode("\n");
        }
        TypeSpec routerMapping = TypeSpec.classBuilder(genClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(mapMethod.build())
                .build();
        try {
            JavaFile.builder("com.github.mzule.activityrouter.router", routerMapping)
                    .build()
                    .writeTo(filer);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return true;
    }

    private void addStatement(MethodSpec.Builder mapMethod, Class typeClz, String[] args) {
        String extras = join(args);
        if (extras.length() > 0) {
            String typeName = typeClz.getSimpleName();
            String s = typeName.substring(0, 1).toUpperCase() + typeName.replaceFirst("\\w", "");

            mapMethod.addStatement("extraTypes.set" + s + "Extra($S.split(\",\"))", extras);
        }
    }

    private String join(String[] args) {
        if (args == null || args.length == 0) {
            return "";
        }
        if (args.length == 1) {
            return args[0];
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length - 1; i++) {
            sb.append(args[i]).append(",");
        }
        sb.append(args[args.length - 1]);
        return sb.toString();
    }

    private void error(String error) {
        messager.printMessage(Diagnostic.Kind.ERROR, error);
    }

    private void debug(String msg) {
        if (DEBUG) {
            messager.printMessage(Diagnostic.Kind.NOTE, msg);
        }
    }
}
