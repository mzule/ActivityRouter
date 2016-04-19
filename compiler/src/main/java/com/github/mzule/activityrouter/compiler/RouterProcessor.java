package com.github.mzule.activityrouter.compiler;

import com.github.mzule.activityrouter.annotation.Router;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.Collections;
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
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {

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
        return Collections.singleton(Router.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> activities = roundEnv.getElementsAnnotatedWith(Router.class);

        MethodSpec.Builder mapMethod = MethodSpec.methodBuilder("map")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .addStatement("java.util.Map<String,String> transfer = null")
                .addStatement("com.github.mzule.activityrouter.router.ExtraTypes extraTypes")
                .addCode("\n");

        for (Element activity : activities) {
            if (activity.getKind() != ElementKind.CLASS) {
                error("Router can only apply on class");
            }
            Router router = activity.getAnnotation(Router.class);

            String[] transfer = router.transfer();
            if (transfer != null && transfer.length > 0 && !"".equals(transfer[0])) {
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
            String extras = join(router.intExtra());
            if (extras.length() > 0) {
                mapMethod.addStatement("extraTypes.setIntExtra($S.split(\",\"))", extras);
            }
            extras = join(router.longExtra());
            if (extras.length() > 0) {
                mapMethod.addStatement("extraTypes.setLongExtra($S.split(\",\"))", extras);
            }
            extras = join(router.boolExtra());
            if (extras.length() > 0) {
                mapMethod.addStatement("extraTypes.setBoolExtra($S.split(\",\"))", extras);
            }
            extras = join(router.shortExtra());
            if (extras.length() > 0) {
                mapMethod.addStatement("extraTypes.setShortExtra($S.split(\",\"))", extras);
            }
            extras = join(router.floatExtra());
            if (extras.length() > 0) {
                mapMethod.addStatement("extraTypes.setFloatExtra($S.split(\",\"))", extras);
            }
            extras = join(router.doubleExtra());
            if (extras.length() > 0) {
                mapMethod.addStatement("extraTypes.setDoubleExtra($S.split(\",\"))", extras);
            }
            extras = join(router.byteExtra());
            if (extras.length() > 0) {
                mapMethod.addStatement("extraTypes.setByteExtra($S.split(\",\"))", extras);
            }
            extras = join(router.charExtra());
            if (extras.length() > 0) {
                mapMethod.addStatement("extraTypes.setCharExtra($S.split(\",\"))", extras);
            }
            for (String format : router.value()) {
                mapMethod.addStatement("com.github.mzule.activityrouter.router.Routers.map($S, $T.class, extraTypes)", format, ClassName.get((TypeElement) activity));
            }
            mapMethod.addCode("\n");
        }
        mapMethod.addStatement("com.github.mzule.activityrouter.router.Routers.sort()");

        TypeSpec routerMapping = TypeSpec.classBuilder("RouterMapping")
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

    private static String join(String[] args) {
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
}
