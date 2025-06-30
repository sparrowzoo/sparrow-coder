package com.sparrowzoo.coder.domain.bo;

import com.sparrow.core.spi.JsonFactory;
import com.sparrow.protocol.BO;
import com.sparrowzoo.coder.domain.service.ArchitectureGenerator;
import com.sparrowzoo.coder.domain.service.registry.ArchitectureRegistry;
import com.sparrowzoo.coder.enums.ArchitectureCategory;
import lombok.Getter;

import java.util.Map;

@Getter
public class ProjectArchsBO implements BO {
    public ProjectArchsBO(String configs) {
        this.archs = JsonFactory.getProvider().parse(configs, Map.class);
    }

    public ArchitectureGenerator getArch(ArchitectureCategory category) {
        String arch = this.archs.get(category.name());
        return ArchitectureRegistry.getInstance().getGenerator(category, arch);
    }

    public ArchitectureGenerator getArch(String category) {
        return this.getArch(ArchitectureCategory.valueOf(category));
    }

    private final Map<String, String> archs;
}
