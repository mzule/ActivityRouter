package com.github.mzule.activityrouter.compiler;

import com.github.mzule.activityrouter.annotation.Router;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
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
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addStatement("com.github.mzule.activityrouter.router.ExtraTypes extraTypes");

        for (Element activity : activities) {
            if (activity.getKind() != ElementKind.CLASS) {
                error("Router can only apply on class");
            }
            // TODO check subclass of Activity
            Router router = activity.getAnnotation(Router.class);

            mapMethod.addStatement("extraTypes = new com.github.mzule.activityrouter.router.ExtraTypes()");
        }

        TypeSpec routerMapping = TypeSpec.classBuilder("RouterMapping")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(ClassName.get("com.github.mzule.activityrouter.router", "Routers"), "routers", Modifier.PRIVATE)
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(ParameterSpec.builder(ClassName.get("android.content", "Context"), "context").build())
                        .addStatement("this.routers = Routers.create(context)")
                        .build())
                .addMethod(mapMethod.build())
                .build();
        try {
            JavaFile.builder("com.github.mzule.activityrouter", routerMapping)
                    .build()
                    .writeTo(filer);
        } catch (Throwable e) {
            //e.printStackTrace();
        }
        return true;
    }

    private void error(String error) {
        messager.printMessage(Diagnostic.Kind.ERROR, error);
    }
}
