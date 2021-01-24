package ru.itis.aivar;

import com.google.auto.service.AutoService;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@AutoService(Processor.class)
@SupportedAnnotationTypes(value = {"ru.itis.aivar.HtmlForm"})
public class HtmlProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        //Configuration
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
        configuration.setDefaultEncoding("UTF-8");
        try {
            configuration.setTemplateLoader(new FileTemplateLoader(new File("src/main/resources")));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        //Template
        Template template;
        try {
            template = configuration.getTemplate("form_template.ftlh");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        //Main logic
        Set<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(HtmlForm.class);
        for (Element element : annotatedElements) {
            Map<String, Object> attributes = new HashMap<>();
            String path = HtmlProcessor.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            path = path + element.getSimpleName().toString() + ".html";
            Path out = Paths.get(path);
            HtmlForm formAnnotation = element.getAnnotation(HtmlForm.class);
            String action = formAnnotation.action();
            String method = formAnnotation.method();
            attributes.put("action", action);
            attributes.put("method", method);

            List<? extends Element> elements = element.getEnclosedElements();
            List<InputRow> inputs = new ArrayList<>();
            for (Element element1 : elements) {
                HtmlInput inputAnnotation = element1.getAnnotation(HtmlInput.class);
                if (inputAnnotation == null){
                    continue;
                }
                inputs.add(new InputRow(
                   inputAnnotation.type(),
                   inputAnnotation.name(),
                   inputAnnotation.placeholder()
                ));
            }
            attributes.put("inputs", inputs);

            FileWriter fileWriter;
            try {
                fileWriter = new FileWriter(out.toFile());
                template.process(attributes, fileWriter);
            } catch (IOException | TemplateException e) {
                throw new IllegalStateException(e);
            }
        }
        return true;
    }
}
