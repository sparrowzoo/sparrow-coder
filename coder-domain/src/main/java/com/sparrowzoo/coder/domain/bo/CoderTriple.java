package com.sparrowzoo.coder.domain.bo;

import lombok.Data;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public class CoderTriple {
    private final Set<String> imports=new LinkedHashSet<>();
    private final Set<String> injects=new LinkedHashSet<>();
    private final Set<String> codes=new LinkedHashSet<>();

    public void addImport(String importPackage) {
        this.imports.add(importPackage);
    }

    public void inject(String inject) {
        this.injects.add(inject);
    }

    public void code(String code) {
        this.codes.add(code);
    }
    public String joinCode(){
        return String.join("\n",this.codes);
    }
    public String joinInjects(){
        return String.join("\n",this.injects);
    }
    public String joinImports(){
        return String.join("\n",this.imports);
    }
}
